package org.eelbbor.pinochle;

import static org.eelbbor.pinochle.TestUtils.randomEnum;
import static org.eelbbor.pinochle.TestUtils.randomInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.eelbbor.carddeck.standard.Suite;
import org.eelbbor.pinochle.exceptions.CardPlayingErrorCode;
import org.eelbbor.pinochle.exceptions.InvalidCardException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class TrickTest {
  private Suite trump;
  private Trick trick;

  @BeforeEach
  void setUp() {
    trump = randomEnum(Suite.class);
    trick = new Trick(trump);
  }

  @Test
  void shouldInitializeAtConstruction() {
    List<Optional<Card>> cardsPlayed = trick.getCardsPlayed();
    assertEquals(4, cardsPlayed.size());
    cardsPlayed.forEach(card -> assertFalse(card.isPresent()));
    assertEquals(trump, trick.getTrump());
    assertEquals(-1, trick.getHighPlayerIndex());
    assertFalse(trick.getHighCard().isPresent());
    assertFalse(trick.getHighTrump().isPresent());
  }

  @Test
  void shouldNotMutateCardsPlayed() throws Exception {
    final List<Card> expectedCards = new ArrayList<>();
    while (expectedCards.size() < 4) {
      Card card = createRandomCard();
      trick.playCard(expectedCards.size(), card, Collections.singletonList(card));
      expectedCards.add(card);
    }

    try {
      trick.getCardsPlayed().remove(TestUtils.randomInteger(4));
      fail("Should have thrown exception trying to remove a card played on the trick.");
    } catch (UnsupportedOperationException ex) {
      assertEquals(expectedCards,
          trick.getCardsPlayed().stream().map(card -> card.get()).collect(Collectors.toList()));
    }

    try {
      trick.getCardsPlayed().add(Optional.of(createRandomCard()));
      fail("Should have thrown exception trying to add a card played on the trick.");
    } catch (UnsupportedOperationException ex) {
      assertEquals(expectedCards,
          trick.getCardsPlayed().stream().map(card -> card.get()).collect(Collectors.toList()));
    }

    List<Optional<Card>> cardsPlayed = trick.getCardsPlayed();
    IntStream.range(0, 4).forEach(index -> {
      assertEquals(expectedCards.get(index), cardsPlayed.get(index).get());
    });
  }

  @Test
  void shouldReturnCardsInProperOrder() throws Exception {
    Card[] playedCards = new Card[4];
    int playerIndex = TestUtils.randomInteger(3);
    for (int index = 0; index < 4; index++) {
      Card card = createRandomCard();
      trick.playCard(playerIndex, card, Collections.singletonList(card));
      playedCards[playerIndex] = card;
      playerIndex = playerIndex == 3 ? 0 : playerIndex + 1;
    }

    List<Optional<Card>> cardsOnTrick = trick.getCardsPlayed();
    IntStream.range(0, 4).forEach(
        index -> assertEquals(playedCards[index], cardsOnTrick.get(index).get()));
  }

  @Test
  void shouldPlayLargerCardFollowingSuiteThanPreviousCard() throws Exception {
    Suite suite = randomEnum(Suite.class);
    List<Card> cardsInHand = Arrays.stream(PinochleFaceValue.values())
        .map(faceValue -> new Card(suite, faceValue)).collect(Collectors.toList());
    PinochleFaceValue faceValueOne = randomEnum(PinochleFaceValue.class);
    PinochleFaceValue faceValueTwo;

    do {
      faceValueTwo = randomEnum(PinochleFaceValue.class);
    } while (faceValueTwo == faceValueOne);

    Card lesser = new Card(suite,
        faceValueOne.ordinal() > faceValueTwo.ordinal() ? faceValueTwo : faceValueOne);
    trick.playCard(0, lesser, cardsInHand);
    assertEquals(lesser, trick.getHighCard().get());
    assertEquals(0, trick.getHighPlayerIndex());

    Card greater = new Card(suite,
        faceValueOne.ordinal() > faceValueTwo.ordinal() ? faceValueOne : faceValueTwo);
    trick.playCard(1, greater, cardsInHand);
    assertEquals(greater, trick.getHighCard().get());
    assertEquals(1, trick.getHighPlayerIndex());

    List<Optional<Card>> cardsPlayed = trick.getCardsPlayed();
    assertEquals(lesser, cardsPlayed.get(0).get());
    assertEquals(greater, cardsPlayed.get(1).get());
    assertFalse(cardsPlayed.get(2).isPresent());
    assertFalse(cardsPlayed.get(3).isPresent());
  }

  @Test
  void shouldPlayIdenticalSizeCardFollowingSuiteWithoutLargerCard() throws Exception {
    Suite suite = randomEnum(Suite.class);
    PinochleFaceValue faceValue = randomEnum(PinochleFaceValue.class);
    List<Card> cardsInHand = Arrays.stream(PinochleFaceValue.values())
        .filter(value -> value.ordinal() <= faceValue.ordinal())
        .map(value -> new Card(suite, value)).collect(Collectors.toList());

    assertFalse(trick.getHighCard().isPresent());
    assertFalse(trick.getHighTrump().isPresent());
    Card card = new Card(suite, faceValue);
    for (int index = 0; index < 4; index++) {
      trick.playCard(index, card, cardsInHand);
      assertEquals(card, trick.getCardsPlayed().get(index).get());
      assertEquals(card, trick.getHighCard().get());
      assertEquals(0, trick.getHighPlayerIndex());
    }

    List<Optional<Card>> cardsPlayed = trick.getCardsPlayed();
    assertEquals(4, cardsPlayed.size());
    IntStream.range(0, 4).forEach(index -> assertEquals(card, cardsPlayed.get(index).get()));
    assertEquals(card, trick.getHighCard().get());
    assertEquals(0, trick.getHighPlayerIndex());
    assertFalse(trick.getHighTrump().isPresent());
  }

  @Test
  void shouldPlaySmallerCardFollowingSuiteWithoutLargerCard() throws Exception {
    Suite suite = randomEnum(Suite.class);
    final List<Card> cardsInHand = Arrays.stream(PinochleFaceValue.values())
        .map(value -> new Card(suite, value)).collect(Collectors.toList());

    PinochleFaceValue faceValueOne = randomEnum(PinochleFaceValue.class);
    PinochleFaceValue faceValueTwo;

    do {
      faceValueTwo = randomEnum(PinochleFaceValue.class);
    } while (faceValueTwo == faceValueOne);

    Card greater = new Card(suite,
        faceValueOne.ordinal() > faceValueTwo.ordinal() ? faceValueOne : faceValueTwo);
    trick.playCard(0, greater, Collections.singletonList(greater));
    assertEquals(greater, trick.getHighCard().get());
    assertEquals(0, trick.getHighPlayerIndex());


    Card lesser = new Card(suite,
        faceValueOne.ordinal() > faceValueTwo.ordinal() ? faceValueTwo : faceValueOne);
    List<Card> lesserCardsInHand = cardsInHand.stream()
        .filter(card -> card.getOrdinal() <= lesser.getOrdinal()).collect(Collectors.toList());
    trick.playCard(1, lesser, lesserCardsInHand);
    assertEquals(greater, trick.getHighCard().get());
    assertEquals(0, trick.getHighPlayerIndex());

    assertFalse(trick.getHighTrump().isPresent());
    List<Optional<Card>> cardsPlayed = trick.getCardsPlayed();
    assertEquals(greater, cardsPlayed.get(0).get());
    assertEquals(lesser, cardsPlayed.get(1).get());
    assertFalse(cardsPlayed.get(2).isPresent());
    assertFalse(cardsPlayed.get(3).isPresent());
  }

  @Test
  void shouldPlayLowerCardFollowingSuiteAfterTrumpPlayed() throws Exception {
    Suite suite = getNonTrumpSuite();
    List<Card> cardsInHand = Arrays.stream(PinochleFaceValue.values())
        .map(value -> new Card(suite, value)).collect(Collectors.toList());
    PinochleFaceValue faceValueOne = randomEnum(PinochleFaceValue.class);
    PinochleFaceValue faceValueTwo;

    do {
      faceValueTwo = randomEnum(PinochleFaceValue.class);
    } while (faceValueTwo == faceValueOne);

    Card greater = new Card(suite,
        faceValueOne.ordinal() > faceValueTwo.ordinal() ? faceValueOne : faceValueTwo);
    trick.playCard(0, greater, cardsInHand);
    assertEquals(greater, trick.getHighCard().get());
    assertFalse(trick.getHighTrump().isPresent());
    assertEquals(0, trick.getHighPlayerIndex());

    Card trumpCard = new Card(trump, randomEnum(PinochleFaceValue.class));
    trick.playCard(1, trumpCard, Collections.singletonList(trumpCard));
    assertEquals(greater, trick.getHighCard().get());
    assertEquals(trumpCard, trick.getHighTrump().get());
    assertEquals(1, trick.getHighPlayerIndex());

    Card lesser = new Card(suite,
        faceValueOne.ordinal() > faceValueTwo.ordinal() ? faceValueTwo : faceValueOne);
    trick.playCard(2, lesser, cardsInHand);
    assertEquals(greater, trick.getHighCard().get());
    assertEquals(trumpCard, trick.getHighTrump().get());
    assertEquals(1, trick.getHighPlayerIndex());

    trick.playCard(3, lesser, cardsInHand);
    assertEquals(greater, trick.getHighCard().get());
    assertEquals(trumpCard, trick.getHighTrump().get());
    assertEquals(1, trick.getHighPlayerIndex());

    List<Optional<Card>> cardsPlayed = trick.getCardsPlayed();
    assertEquals(4, cardsPlayed.size());
    assertEquals(greater, cardsPlayed.get(0).get());
    assertEquals(trumpCard, cardsPlayed.get(1).get());
    assertEquals(lesser, cardsPlayed.get(2).get());
    assertEquals(lesser, cardsPlayed.get(3).get());
  }

  @Test
  void shouldPlayLargerTrumpThanPreviousTrump() throws Exception {
    Suite suite = getNonTrumpSuite();
    Card lead = new Card(suite, randomEnum(PinochleFaceValue.class));
    trick.playCard(0, lead, Collections.singletonList(lead));
    assertEquals(lead, trick.getHighCard().get());
    assertFalse(trick.getHighTrump().isPresent());
    assertEquals(0, trick.getHighPlayerIndex());

    List<Card> trumpInHand = Arrays.stream(PinochleFaceValue.values())
        .map(value -> new Card(trump, value)).collect(Collectors.toList());
    Card trumpOne = trumpInHand.get(randomInteger(trumpInHand.size()));
    Card trumpTwo;
    do {
      trumpTwo = new Card(trump, randomEnum(PinochleFaceValue.class));
    } while (trumpTwo.equals(trumpOne));

    Card greater = trumpOne.getOrdinal() > trumpTwo.getOrdinal() ? trumpOne : trumpTwo;
    Card lesser = trumpOne.equals(greater) ? trumpTwo : trumpOne;
    assertNotEquals(greater, lesser);

    trick.playCard(1, lesser, trumpInHand);
    assertEquals(lead, trick.getHighCard().get());
    assertEquals(lesser, trick.getHighTrump().get());
    assertEquals(1, trick.getHighPlayerIndex());

    trick.playCard(2, lead, Collections.singletonList(lead));
    assertEquals(lead, trick.getHighCard().get());
    assertEquals(lesser, trick.getHighTrump().get());
    assertEquals(1, trick.getHighPlayerIndex());

    trick.playCard(3, greater, trumpInHand);
    assertEquals(lead, trick.getHighCard().get());
    assertEquals(greater, trick.getHighTrump().get());
    assertEquals(3, trick.getHighPlayerIndex());

    List<Optional<Card>> cardsPlayed = trick.getCardsPlayed();
    assertEquals(4, cardsPlayed.size());
    assertEquals(lead, cardsPlayed.get(0).get());
    assertEquals(lesser, cardsPlayed.get(1).get());
    assertEquals(lead, cardsPlayed.get(2).get());
    assertEquals(greater, cardsPlayed.get(3).get());
  }

  @Test
  void shouldPlayTrumpWithEqualCardWithoutLargerTrump() throws Exception {
    Suite suite = getNonTrumpSuite();
    Card lead = new Card(suite, randomEnum(PinochleFaceValue.class));

    Card trumpCard = new Card(trump, randomEnum(PinochleFaceValue.class));
    final List<Card> trumpInHand = Arrays.stream(PinochleFaceValue.values())
        .filter(value -> value.ordinal() <= trumpCard.getOrdinal())
        .map(value -> new Card(trump, value)).collect(Collectors.toList());

    trick.playCard(2, lead, Collections.singletonList(lead));
    assertEquals(lead, trick.getHighCard().get());
    assertFalse(trick.getHighTrump().isPresent());
    assertEquals(2, trick.getHighPlayerIndex());

    trick.playCard(3, trumpCard, trumpInHand);
    assertEquals(lead, trick.getHighCard().get());
    assertEquals(trumpCard, trick.getHighTrump().get());
    assertEquals(3, trick.getHighPlayerIndex());

    trick.playCard(0, trumpCard, trumpInHand);
    assertEquals(lead, trick.getHighCard().get());
    assertEquals(trumpCard, trick.getHighTrump().get());
    assertEquals(3, trick.getHighPlayerIndex());

    trick.playCard(1, trumpCard, trumpInHand);
    assertEquals(lead, trick.getHighCard().get());
    assertEquals(trumpCard, trick.getHighTrump().get());
    assertEquals(3, trick.getHighPlayerIndex());

    List<Optional<Card>> cardsPlayed = trick.getCardsPlayed();
    assertEquals(4, cardsPlayed.size());
    assertEquals(lead, cardsPlayed.get(2).get());
    assertEquals(trumpCard, cardsPlayed.get(3).get());
    assertEquals(trumpCard, cardsPlayed.get(0).get());
    assertEquals(trumpCard, cardsPlayed.get(1).get());
  }

  @Test
  void shouldPlaySmallerTrumpWithoutLargerTrump() throws Exception {
    Suite suite = getNonTrumpSuite();
    Card lead = new Card(suite, randomEnum(PinochleFaceValue.class));
    trick.playCard(1, lead, Collections.singletonList(lead));
    assertEquals(lead, trick.getHighCard().get());
    assertFalse(trick.getHighTrump().isPresent());
    assertEquals(1, trick.getHighPlayerIndex());

    Card trumpMid = new Card(trump, PinochleFaceValue.King);
    trick.playCard(2, trumpMid, Arrays.stream(PinochleFaceValue.values())
        .filter(value -> value.ordinal() <= trumpMid.getOrdinal())
        .map(value -> new Card(trump, value)).collect(Collectors.toList()));
    assertEquals(lead, trick.getHighCard().get());
    assertEquals(trumpMid, trick.getHighTrump().get());
    assertEquals(2, trick.getHighPlayerIndex());

    Card trumpGreater = new Card(trump, PinochleFaceValue.Ten);
    trick.playCard(3, trumpGreater, Arrays.stream(PinochleFaceValue.values())
        .filter(value -> value.ordinal() <= trumpGreater.getOrdinal())
        .map(value -> new Card(trump, value)).collect(Collectors.toList()));
    assertEquals(lead, trick.getHighCard().get());
    assertEquals(trumpGreater, trick.getHighTrump().get());
    assertEquals(3, trick.getHighPlayerIndex());

    Card trumpLow = new Card(trump, PinochleFaceValue.Queen);
    trick.playCard(0, trumpLow, Arrays.stream(PinochleFaceValue.values())
        .filter(value -> value.ordinal() <= trumpLow.getOrdinal())
        .map(value -> new Card(trump, value)).collect(Collectors.toList()));
    assertEquals(lead, trick.getHighCard().get());
    assertEquals(trumpGreater, trick.getHighTrump().get());
    assertEquals(3, trick.getHighPlayerIndex());

    List<Optional<Card>> cardsPlayed = trick.getCardsPlayed();
    assertEquals(4, cardsPlayed.size());
    assertEquals(lead, cardsPlayed.get(1).get());
    assertEquals(trumpMid, cardsPlayed.get(2).get());
    assertEquals(trumpGreater, cardsPlayed.get(3).get());
    assertEquals(trumpLow, cardsPlayed.get(0).get());
  }

  @Test
  void shouldAllowPlayingOffSuiteIfOutOfLedSuiteAndTrump() throws Exception {
    Suite suite = getNonTrumpSuite();
    Card lead = new Card(suite, randomEnum(PinochleFaceValue.class));

    Set<Suite> offSuites = Arrays.stream(Suite.values())
        .filter(offSuite -> offSuite != trump && offSuite != lead.getSuite())
        .collect(Collectors.toSet());

    List<Card> cardsInHand = offSuites.stream().flatMap(offSuite1 ->
        Arrays.stream(PinochleFaceValue.values()).map(value -> new Card(offSuite1, value)))
        .collect(Collectors.toList());
    assertEquals(2 * PinochleFaceValue.values().length, cardsInHand.size());
    assertEquals(cardsInHand.size(), new HashSet(cardsInHand).size());

    trick.playCard(3, lead, Collections.singletonList(lead));
    assertEquals(lead, trick.getHighCard().get());
    assertFalse(trick.getHighTrump().isPresent());
    assertEquals(3, trick.getHighPlayerIndex());

    Card offCardOne = cardsInHand.get(randomInteger(cardsInHand.size()));
    trick.playCard(0, offCardOne, cardsInHand);
    assertEquals(lead, trick.getHighCard().get());
    assertFalse(trick.getHighTrump().isPresent());
    assertEquals(3, trick.getHighPlayerIndex());

    Card trumpCard = new Card(trump, randomEnum(PinochleFaceValue.class));
    trick.playCard(1, trumpCard, Collections.singletonList(trumpCard));
    assertEquals(lead, trick.getHighCard().get());
    assertEquals(trumpCard, trick.getHighTrump().get());
    assertEquals(1, trick.getHighPlayerIndex());

    Card offCardTwo = cardsInHand.get(randomInteger(cardsInHand.size()));
    trick.playCard(2, offCardTwo, cardsInHand);
    assertEquals(lead, trick.getHighCard().get());
    assertEquals(trumpCard, trick.getHighTrump().get());
    assertEquals(1, trick.getHighPlayerIndex());

    List<Optional<Card>> cardsPlayed = trick.getCardsPlayed();
    assertEquals(4, cardsPlayed.size());
    assertEquals(lead, cardsPlayed.get(3).get());
    assertEquals(offCardOne, cardsPlayed.get(0).get());
    assertEquals(trumpCard, cardsPlayed.get(1).get());
    assertEquals(offCardTwo, cardsPlayed.get(2).get());
  }

  @Test
  void shouldThrowExceptionForTryingToPlayMoreThanFourCards() throws Exception {
    int playerIndex = TestUtils.randomInteger(4);
    Card card = createRandomCard();
    trick.playCard(playerIndex, card, Collections.singletonList(card));
    assertEquals(card, trick.getCardsPlayed().get(playerIndex).get());

    try {
      Card nextCard = createRandomCard();
      trick.playCard(playerIndex, nextCard, Collections.singletonList(card));
      fail("Should have thrown exception trying to play a card when a player has already played.");
    } catch (RuntimeException ex) {
      assertEquals("Unexpected exception trying to play more than one card for player "
              + playerIndex + " on a single trick.", ex.getMessage());
    }
  }

  @Test
  void shouldThrowExceptionIfCardNotInPlayersHand() {
    List<Card> cardsInHand = new ArrayList<>();
    IntStream.range(0, 10).forEach(index -> cardsInHand.add(createRandomCard()));
    Card card = cardsInHand.remove(randomInteger(10));
    while (cardsInHand.contains(card)) {
      cardsInHand.remove(card);
    }

    try {
      trick.playCard(TestUtils.randomInteger(4), card, cardsInHand);
      fail("Should have thrown exception for missing card.");
    } catch (InvalidCardException e) {
      CardPlayingErrorCode errorCode = CardPlayingErrorCode.NO_SUCH_CARD_ERROR;
      assertEquals(errorCode, e.getErrorCode());
      assertEquals(errorCode.createInvalidCardException(null, card).getMessage(),
          e.getMessage());
    }
  }

  @Test
  void shouldThrowExceptionIfCardDoesNotFollowSuite() throws Exception {
    Suite suite = randomEnum(Suite.class);
    Card lead = new Card(suite, randomEnum(PinochleFaceValue.class));
    List<Card> cardsInHand = Arrays.stream(PinochleFaceValue.values())
        .map(value -> new Card(suite, value)).collect(Collectors.toList());
    trick.playCard(1, lead, cardsInHand);
    assertEquals(lead, trick.getHighCard().get());
    assertFalse(trick.getHighTrump().isPresent());

    Suite offSuite = getOffSuite(suite);
    assertNotEquals(offSuite, trump);
    assertNotEquals(offSuite, suite);

    Card offSuiteCard = new Card(offSuite, randomEnum(PinochleFaceValue.class));
    Arrays.stream(PinochleFaceValue.values())
        .forEach(value -> cardsInHand.add(new Card(offSuiteCard.getSuite(), value)));

    try {
      trick.playCard(2, offSuiteCard, cardsInHand);
      fail("Should have thrown exception not following suite.");
    } catch (InvalidCardException ex) {
      CardPlayingErrorCode errorCode = CardPlayingErrorCode.FOLLOWING_SUITE_ERROR;
      assertEquals(errorCode, ex.getErrorCode());
      assertEquals(errorCode.createInvalidCardException(lead, offSuiteCard).getMessage(),
          ex.getMessage());
    }
  }

  @Test
  void shouldThrowExceptionIfCardDoesNotExceedLargestPreviousCard() throws Exception {
    Suite suite = randomEnum(Suite.class);
    Suite otherSuite = getOffSuite(suite);
    PinochleFaceValue[] faceValues = PinochleFaceValue.values();
    List<Card> cardsInHand = Arrays.stream(faceValues).flatMap(faceValue ->
        List.of(new Card(suite, faceValue), new Card(otherSuite, faceValue)).stream())
        .collect(Collectors.toList());

    int lowerIndex = randomInteger(faceValues.length - 1);
    PinochleFaceValue leadValue = faceValues[lowerIndex];
    assertTrue(leadValue.compareTo(PinochleFaceValue.Ace) < 0);

    Card lead = new Card(suite, leadValue);
    Card invalid = new Card(suite, faceValues[lowerIndex == 0 ? 0 : randomInteger(lowerIndex)]);
    assertTrue(invalid.compareTo(lead) <= 0);

    trick.playCard(2, lead, cardsInHand);
    assertEquals(lead, trick.getHighCard().get());
    try {
      trick.playCard(3, invalid, cardsInHand);
      fail("Should have throw an exception by playing a lower card " + invalid + " after " + lead);
    } catch (InvalidCardException ex) {
      CardPlayingErrorCode errorCode = CardPlayingErrorCode.CARD_TOO_LOW_FOLLOWING_SUITE_ERROR;
      assertEquals(errorCode, ex.getErrorCode());
      assertEquals(errorCode.createInvalidCardException(lead, invalid).getMessage(),
          ex.getMessage());
    }
  }

  @Test
  void shouldThrowExceptionIfCardIsNotTrumpWhenOutOfLedSuite() throws Exception {
    Suite suite = getNonTrumpSuite();
    Card lead = new Card(suite, randomEnum(PinochleFaceValue.class));

    Suite offSuite = Arrays.stream(Suite.values())
        .filter(ste -> ste != suite && ste != trump).findAny().get();
    Card offSuiteCard = new Card(offSuite, randomEnum(PinochleFaceValue.class));
    Card trumpCard = new Card(trump, randomEnum(PinochleFaceValue.class));

    trick.playCard(0, lead, Collections.singletonList(lead));
    try {
      trick.playCard(1, offSuiteCard, List.of(offSuiteCard, trumpCard));
      fail("Should have thrown trying to play offsuite " + offSuiteCard
          + " when had trump " + trump);
    } catch (InvalidCardException ex) {
      CardPlayingErrorCode errorCode = CardPlayingErrorCode.TRUMP_ERROR;
      assertEquals(errorCode, ex.getErrorCode());
      assertEquals(errorCode.createInvalidCardException(trumpCard, offSuiteCard).getMessage(),
          ex.getMessage());
    }
  }

  @Test
  void shouldThrowExceptionIfCardIsNotTrumpWhenOutOfLedSuiteAfterTrumpPlayed() throws Exception {
    Suite suite = getNonTrumpSuite();
    Suite offSuite = getOffSuite(suite);
    Card lead = new Card(suite, randomEnum(PinochleFaceValue.class));

    Card offSuiteCard = new Card(offSuite, randomEnum(PinochleFaceValue.class));
    Card firstTrumpCard = new Card(trump, randomEnum(PinochleFaceValue.class));
    Card trumpCard = new Card(trump, randomEnum(PinochleFaceValue.class));

    trick.playCard(2, lead, Collections.singletonList(lead));
    trick.playCard(3, firstTrumpCard, Collections.singletonList(firstTrumpCard));
    try {
      trick.playCard(0, offSuiteCard, List.of(offSuiteCard, trumpCard));
      fail("Should have thrown trying to play offsuite " + offSuiteCard
          + " when had trump " + trump + " and trump already played.");
    } catch (InvalidCardException ex) {
      CardPlayingErrorCode errorCode = CardPlayingErrorCode.TRUMP_ERROR;
      assertEquals(errorCode, ex.getErrorCode());
      assertEquals(errorCode.createInvalidCardException(trumpCard, offSuiteCard).getMessage(),
          ex.getMessage());
    }
  }

  @Test
  void shouldThrowExceptionIfTrumpCardDoesNotExceedLargestPreviousTrumpCard() throws Exception {
    Suite suite = getNonTrumpSuite();
    Card lead = new Card(suite, randomEnum(PinochleFaceValue.class));

    PinochleFaceValue[] values = PinochleFaceValue.values();
    int highIndex = randomInteger(3) + 1;
    Card firstTrump = new Card(trump, values[highIndex]);
    Card secondTrump = new Card(trump, values[randomInteger(highIndex)]);
    assertTrue(firstTrump.compareTo(secondTrump) >= 0,
        "Screwed something up in test code with random index.");

    Suite offSuite = getOffSuite(suite);
    List<Card> cardsInHand = Arrays.stream(PinochleFaceValue.values())
        .flatMap(val -> List.of(new Card(trump, val), new Card(offSuite, val)).stream())
        .collect(Collectors.toList());
    trick.playCard(1, lead, Collections.singletonList(lead));
    trick.playCard(2, firstTrump, cardsInHand);
    assertEquals(lead, trick.getHighCard().get());
    assertEquals(firstTrump, trick.getHighTrump().get());
    try {
      trick.playCard(3, secondTrump, cardsInHand);
      fail("Should have thrown exception playing lower trump " + secondTrump
          + " than min trump " + firstTrump);
    } catch (InvalidCardException ex) {
      CardPlayingErrorCode errorCode = CardPlayingErrorCode.TRUMP_CARD_TOO_LOW_ERROR;
      assertEquals(errorCode, ex.getErrorCode());
      assertEquals(errorCode.createInvalidCardException(firstTrump, secondTrump).getMessage(),
          ex.getMessage());
    }
  }

  @Test
  void shouldReportFirstHighCardPlayerIndexAsWinnerForIdenticalPlays() throws Exception {
    final Suite suite = getNonTrumpSuite();
    final Card lead = new Card(suite, randomEnum(PinochleFaceValue.class));
    trick.playCard(1, lead, Collections.singletonList(lead));
    assertEquals(lead, trick.getHighCard().get());
    assertFalse(trick.getHighTrump().isPresent());
    assertEquals(1, trick.getHighPlayerIndex());

    trick.playCard(2, lead, Collections.singletonList(lead));
    assertEquals(lead, trick.getHighCard().get());
    assertFalse(trick.getHighTrump().isPresent());
    assertEquals(1, trick.getHighPlayerIndex());

    trick.playCard(3, lead, Collections.singletonList(lead));
    assertEquals(lead, trick.getHighCard().get());
    assertFalse(trick.getHighTrump().isPresent());
    assertEquals(1, trick.getHighPlayerIndex());

    trick.playCard(0, lead, Collections.singletonList(lead));
    assertEquals(lead, trick.getHighCard().get());
    assertFalse(trick.getHighTrump().isPresent());
    assertEquals(1, trick.getHighPlayerIndex());
  }

  @Test
  void shouldReportHighTrumpPlayerIndexAsWinner() {
    fail("");
  }

  @Test
  void shouldReportFirstHighTrumpCardPlayerIndexAsWinnerForIdenticalPlays() {
    fail("");
  }

  private Suite getNonTrumpSuite() {
    return getOffSuite(null);
  }

  private Suite getOffSuite(Suite ledSuite) {
    Suite offSuite;
    do {
      offSuite = randomEnum(Suite.class);
    } while ((ledSuite != null && offSuite == ledSuite) || offSuite == trump);
    return offSuite;
  }

  private Card createRandomCard() {
    return new Card(randomEnum(Suite.class), randomEnum(PinochleFaceValue.class));
  }
}