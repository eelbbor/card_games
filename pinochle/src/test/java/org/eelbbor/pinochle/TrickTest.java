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
    assertTrue(trick.getCardsPlayed().isEmpty());
    assertEquals(trump, trick.getTrump());
    assertFalse(trick.getHighCard().isPresent());
    assertFalse(trick.getHighTrump().isPresent());
  }

  @Test
  void shouldNotMutateCardsPlayed() throws Exception {
    List<Card> playedCards = new ArrayList<>();
    while (playedCards.size() < 4) {
      Card card = new Card(randomEnum(Suite.class), randomEnum(PinochleFaceValue.class));
      trick.playCard(card, Collections.singletonList(card));
      playedCards.add(card);
    }

    List<Card> retrievedCards = trick.getCardsPlayed();
    while (!retrievedCards.isEmpty()) {
      retrievedCards.remove(0);
      List<Card> currentCards = trick.getCardsPlayed();
      assertNotEquals(retrievedCards, currentCards);
    }
    assertEquals(playedCards, trick.getCardsPlayed());
  }

  @Test
  void shouldReturnCardsInProperOrder() throws Exception {
    List<Card> playedCard = new ArrayList<>();
    while (playedCard.size() < 4) {
      Card card = new Card(randomEnum(Suite.class), randomEnum(PinochleFaceValue.class));
      trick.playCard(card, Collections.singletonList(card));
      playedCard.add(card);
    }

    List<Card> cardsOnTrick = trick.getCardsPlayed();
    assertEquals(playedCard.size(), cardsOnTrick.size());
    IntStream.range(0, playedCard.size())
        .forEach(index -> assertEquals(playedCard.get(index), cardsOnTrick.get(index)));
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
    trick.playCard(lesser, cardsInHand);
    assertEquals(lesser, trick.getHighCard().get());

    Card greater = new Card(suite,
        faceValueOne.ordinal() > faceValueTwo.ordinal() ? faceValueOne : faceValueTwo);
    trick.playCard(greater, cardsInHand);
    assertEquals(greater, trick.getHighCard().get());

    List<Card> cardsPlayed = trick.getCardsPlayed();
    assertEquals(2, cardsPlayed.size());
    assertEquals(lesser, cardsPlayed.get(0));
    assertEquals(greater, cardsPlayed.get(1));
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
      trick.playCard(card, cardsInHand);
      assertEquals(card, trick.getCardsPlayed().get(index));
      assertEquals(card, trick.getHighCard().get());
    }

    List<Card> cardsPlayed = trick.getCardsPlayed();
    assertEquals(4, cardsPlayed.size());
    IntStream.range(0, 4).forEach(index -> assertEquals(card, cardsPlayed.get(index)));
    assertEquals(card, trick.getHighCard().get());
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
    trick.playCard(greater, Collections.singletonList(greater));
    assertEquals(greater, trick.getHighCard().get());


    Card lesser = new Card(suite,
        faceValueOne.ordinal() > faceValueTwo.ordinal() ? faceValueTwo : faceValueOne);
    List<Card> lesserCardsInHand = cardsInHand.stream()
        .filter(card -> card.getOrdinal() <= lesser.getOrdinal()).collect(Collectors.toList());
    trick.playCard(lesser, lesserCardsInHand);
    assertEquals(greater, trick.getHighCard().get());

    assertFalse(trick.getHighTrump().isPresent());
    List<Card> cardsPlayed = trick.getCardsPlayed();
    assertEquals(2, cardsPlayed.size());
    assertEquals(greater, cardsPlayed.get(0));
    assertEquals(lesser, cardsPlayed.get(1));
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
    trick.playCard(greater, cardsInHand);
    assertEquals(greater, trick.getHighCard().get());
    assertFalse(trick.getHighTrump().isPresent());

    Card trumpCard = new Card(trump, randomEnum(PinochleFaceValue.class));
    trick.playCard(trumpCard, Collections.singletonList(trumpCard));
    assertEquals(greater, trick.getHighCard().get());
    assertEquals(trumpCard, trick.getHighTrump().get());

    Card lesser = new Card(suite,
        faceValueOne.ordinal() > faceValueTwo.ordinal() ? faceValueTwo : faceValueOne);
    trick.playCard(lesser, cardsInHand);
    assertEquals(greater, trick.getHighCard().get());
    assertEquals(trumpCard, trick.getHighTrump().get());

    trick.playCard(lesser, cardsInHand);
    assertEquals(greater, trick.getHighCard().get());
    assertEquals(trumpCard, trick.getHighTrump().get());

    List<Card> cardsPlayed = trick.getCardsPlayed();
    assertEquals(4, cardsPlayed.size());
    assertEquals(greater, cardsPlayed.get(0));
    assertEquals(trumpCard, cardsPlayed.get(1));
    assertEquals(lesser, cardsPlayed.get(2));
    assertEquals(lesser, cardsPlayed.get(3));
  }

  @Test
  void shouldPlayLargerTrumpThanPreviousTrump() throws Exception {
    Suite suite = getNonTrumpSuite();
    Card lead = new Card(suite, randomEnum(PinochleFaceValue.class));
    trick.playCard(lead, Collections.singletonList(lead));
    assertEquals(lead, trick.getHighCard().get());
    assertFalse(trick.getHighTrump().isPresent());

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

    trick.playCard(lesser, trumpInHand);
    assertEquals(lead, trick.getHighCard().get());
    assertEquals(lesser, trick.getHighTrump().get());

    trick.playCard(lead, Collections.singletonList(lead));
    assertEquals(lead, trick.getHighCard().get());
    assertEquals(lesser, trick.getHighTrump().get());

    trick.playCard(greater, trumpInHand);
    assertEquals(lead, trick.getHighCard().get());
    assertEquals(greater, trick.getHighTrump().get());

    List<Card> cardsPlayed = trick.getCardsPlayed();
    assertEquals(4, cardsPlayed.size());
    assertEquals(lead, cardsPlayed.get(0));
    assertEquals(lesser, cardsPlayed.get(1));
    assertEquals(lead, cardsPlayed.get(2));
    assertEquals(greater, cardsPlayed.get(3));
  }

  @Test
  void shouldPlayTrumpWithEqualCardWithoutLargerTrump() throws Exception {
    Suite suite = getNonTrumpSuite();
    Card lead = new Card(suite, randomEnum(PinochleFaceValue.class));

    Card trumpCard = new Card(trump, randomEnum(PinochleFaceValue.class));
    final List<Card> trumpInHand = Arrays.stream(PinochleFaceValue.values())
        .filter(value -> value.ordinal() <= trumpCard.getOrdinal())
        .map(value -> new Card(trump, value)).collect(Collectors.toList());

    trick.playCard(lead, Collections.singletonList(lead));
    assertEquals(lead, trick.getHighCard().get());
    assertFalse(trick.getHighTrump().isPresent());

    trick.playCard(trumpCard, trumpInHand);
    assertEquals(lead, trick.getHighCard().get());
    assertEquals(trumpCard, trick.getHighTrump().get());

    trick.playCard(trumpCard, trumpInHand);
    assertEquals(lead, trick.getHighCard().get());
    assertEquals(trumpCard, trick.getHighTrump().get());

    trick.playCard(trumpCard, trumpInHand);
    assertEquals(lead, trick.getHighCard().get());
    assertEquals(trumpCard, trick.getHighTrump().get());

    List<Card> cardsPlayed = trick.getCardsPlayed();
    assertEquals(4, cardsPlayed.size());
    assertEquals(lead, cardsPlayed.get(0));
    assertEquals(trumpCard, cardsPlayed.get(1));
    assertEquals(trumpCard, cardsPlayed.get(2));
    assertEquals(trumpCard, cardsPlayed.get(3));
  }

  @Test
  void shouldPlaySmallerTrumpWithoutLargerTrump() throws Exception {
    Suite suite = getNonTrumpSuite();
    Card lead = new Card(suite, randomEnum(PinochleFaceValue.class));
    trick.playCard(lead, Collections.singletonList(lead));
    assertEquals(lead, trick.getHighCard().get());
    assertFalse(trick.getHighTrump().isPresent());

    Card trumpMid = new Card(trump, PinochleFaceValue.King);
    trick.playCard(trumpMid, Arrays.stream(PinochleFaceValue.values())
        .filter(value -> value.ordinal() <= trumpMid.getOrdinal())
        .map(value -> new Card(trump, value)).collect(Collectors.toList()));
    assertEquals(lead, trick.getHighCard().get());
    assertEquals(trumpMid, trick.getHighTrump().get());

    Card trumpGreater = new Card(trump, PinochleFaceValue.Ten);
    trick.playCard(trumpGreater, Arrays.stream(PinochleFaceValue.values())
        .filter(value -> value.ordinal() <= trumpGreater.getOrdinal())
        .map(value -> new Card(trump, value)).collect(Collectors.toList()));
    assertEquals(lead, trick.getHighCard().get());
    assertEquals(trumpGreater, trick.getHighTrump().get());

    Card trumpLow = new Card(trump, PinochleFaceValue.Queen);
    trick.playCard(trumpLow, Arrays.stream(PinochleFaceValue.values())
        .filter(value -> value.ordinal() <= trumpLow.getOrdinal())
        .map(value -> new Card(trump, value)).collect(Collectors.toList()));
    assertEquals(lead, trick.getHighCard().get());
    assertEquals(trumpGreater, trick.getHighTrump().get());

    List<Card> cardsPlayed = trick.getCardsPlayed();
    assertEquals(4, cardsPlayed.size());
    assertEquals(lead, cardsPlayed.get(0));
    assertEquals(trumpMid, cardsPlayed.get(1));
    assertEquals(trumpGreater, cardsPlayed.get(2));
    assertEquals(trumpLow, cardsPlayed.get(3));
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

    trick.playCard(lead, Collections.singletonList(lead));
    assertEquals(lead, trick.getHighCard().get());
    assertFalse(trick.getHighTrump().isPresent());

    Card offCardOne = cardsInHand.get(randomInteger(cardsInHand.size()));
    trick.playCard(offCardOne, cardsInHand);
    assertEquals(lead, trick.getHighCard().get());
    assertFalse(trick.getHighTrump().isPresent());

    Card trumpCard = new Card(trump, randomEnum(PinochleFaceValue.class));
    trick.playCard(trumpCard, Collections.singletonList(trumpCard));
    assertEquals(lead, trick.getHighCard().get());
    assertEquals(trumpCard, trick.getHighTrump().get());

    Card offCardTwo = cardsInHand.get(randomInteger(cardsInHand.size()));
    trick.playCard(offCardTwo, cardsInHand);
    assertEquals(lead, trick.getHighCard().get());
    assertEquals(trumpCard, trick.getHighTrump().get());

    List<Card> cardsPlayed = trick.getCardsPlayed();
    assertEquals(4, cardsPlayed.size());
    assertEquals(lead, cardsPlayed.get(0));
    assertEquals(offCardOne, cardsPlayed.get(1));
    assertEquals(trumpCard, cardsPlayed.get(2));
    assertEquals(offCardTwo, cardsPlayed.get(3));
  }

  @Test
  void shouldThrowExceptionForTryingToPlayMoreThanFourCards() throws Exception {
    for (int i = 0; i < 4; i++) {
      Card card = new Card(randomEnum(Suite.class), randomEnum(PinochleFaceValue.class));
      trick.playCard(card, Collections.singletonList(card));
      assertEquals(card, trick.getCardsPlayed().get(i));
    }

    try {
      Card card = new Card(randomEnum(Suite.class), randomEnum(PinochleFaceValue.class));
      trick.playCard(card, Collections.singletonList(card));
      fail("Should have thrown exception trying to play a fifth card on a trick.");
    } catch (RuntimeException ex) {
      assertEquals("Unexpected exception trying to play more than 4 cards on a trick.",
          ex.getMessage());
    }
  }

  @Test
  void shouldThrowExceptionIfCardNotInPlayersHand() {
    List<Card> cardsInHand = new ArrayList<>();
    IntStream.range(0, 10).forEach(index ->
        cardsInHand.add(new Card(randomEnum(Suite.class), randomEnum(PinochleFaceValue.class))));
    Card card = cardsInHand.remove(randomInteger(10));
    while (cardsInHand.contains(card)) {
      cardsInHand.remove(card);
    }

    try {
      trick.playCard(card, cardsInHand);
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
    trick.playCard(lead, cardsInHand);
    assertEquals(lead, trick.getHighCard().get());
    assertFalse(trick.getHighTrump().isPresent());

    Suite offSuite = getOffSuite(suite);
    assertNotEquals(offSuite, trump);
    assertNotEquals(offSuite, suite);

    Card offSuiteCard = new Card(offSuite, randomEnum(PinochleFaceValue.class));
    Arrays.stream(PinochleFaceValue.values())
        .forEach(value -> cardsInHand.add(new Card(offSuiteCard.getSuite(), value)));

    try {
      trick.playCard(offSuiteCard, cardsInHand);
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

    trick.playCard(lead, cardsInHand);
    assertEquals(lead, trick.getHighCard().get());
    try {
      trick.playCard(invalid, cardsInHand);
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

    trick.playCard(lead, Collections.singletonList(lead));
    try {
      trick.playCard(offSuiteCard, List.of(offSuiteCard, trumpCard));
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

    trick.playCard(lead, Collections.singletonList(lead));
    trick.playCard(firstTrumpCard, Collections.singletonList(firstTrumpCard));
    try {
      trick.playCard(offSuiteCard, List.of(offSuiteCard, trumpCard));
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
    trick.playCard(lead, Collections.singletonList(lead));
    trick.playCard(firstTrump, cardsInHand);
    assertEquals(lead, trick.getHighCard().get());
    assertEquals(firstTrump, trick.getHighTrump().get());
    try {
      trick.playCard(secondTrump, cardsInHand);
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
  void shouldReportHighCardPlayerIndexAsWinner() {
    // TODO (robb): fail("");
  }

  @Test
  void shouldReportFirstHighCardPlayerIndexAsWinnerForIdenticalPlays() {
    // TODO (robb): fail("");
  }

  @Test
  void shouldReportHighTrumpPlayerIndexAsWinner() {
    // TODO (robb): fail("");
  }

  @Test
  void shouldReportFirstHighTrumpCardPlayerIndexAsWinnerForIdenticalPlays() {
    // TODO (robb): fail("");
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
}