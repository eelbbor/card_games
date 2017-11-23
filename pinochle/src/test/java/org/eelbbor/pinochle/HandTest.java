package org.eelbbor.pinochle;

import static org.eelbbor.pinochle.PinochleFaceValue.Ace;
import static org.eelbbor.pinochle.PinochleFaceValue.Jack;
import static org.eelbbor.pinochle.PinochleFaceValue.King;
import static org.eelbbor.pinochle.PinochleFaceValue.Queen;
import static org.eelbbor.pinochle.PinochleFaceValue.Ten;
import static org.eelbbor.pinochle.TestUtils.randomEnum;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.eelbbor.carddeck.standard.Suite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

class HandTest {
  private Hand hand;

  @BeforeEach
  void setUp() {
    hand = new Hand();
  }

  @Test
  void shouldOnlyAllowFourOfEachCard() {
    Suite suite = randomEnum(Suite.class);
    PinochleFaceValue faceValue = randomEnum(PinochleFaceValue.class);
    Card card = new Card(suite, faceValue);
    IntStream.rangeClosed(1, 4).forEach(i -> {
      hand.dealCard(card);
      assertEquals(i, hand.getCardCount(card));
      assertEquals(i, hand.getCardCountBySuite(suite));
    });
    assertEquals(4, hand.getCardCount(card));

    try {
      hand.dealCard(card);
      fail("Should have throw exception for trying to deal more than 4 of the same card type.");
    } catch (IllegalArgumentException ex) {
      assertEquals("Tried to deal more than 4 '" + faceValue.name() + "s' of '"
          + suite.name() + "s'.", ex.getMessage());
    }
  }

  @Test
  void shouldOnlyAllowTwentyCardsInAHand() {
    Arrays.stream(Suite.values()).forEach(suite -> {
      Arrays.stream(PinochleFaceValue.values()).forEach(faceValue -> {
        if (hand.numCards() < 18) {
          hand.dealCard(new Card(suite, faceValue));
        }
      });
    });
    assertEquals(18, hand.numCards());
    Card[] extraCards = new Card[3];
    IntStream.range(0, 3).forEach(i ->
        extraCards[i] = new Card(Suite.values()[i], PinochleFaceValue.values()[i]));
    try {
      hand.dealCard(extraCards);
      fail("Should have throw exception for trying to deal more than 20 total cards.");
    } catch (IllegalArgumentException ex) {
      assertEquals("Tried to add more than 20 cards to a hand.", ex.getMessage());
    }
  }

  @Test
  void shouldIncreaseAndReduceCardCountMapForDealtAndPlayedCard() {
    Set<Card> cardTypes = new HashSet<>();
    while (cardTypes.size() < 5) {
      cardTypes.add(new Card(randomEnum(Suite.class), randomEnum(PinochleFaceValue.class)));
    }
    Map<Suite, Integer> suiteCounts = new HashMap<>();
    cardTypes.forEach(card -> suiteCounts.put(card.getSuite(), 0));

    // Deal each card 4 times to the hand.
    IntStream.rangeClosed(1, 4).forEach(i -> cardTypes.stream().forEach(card -> {
      // Add new object references for a couple of each card.
      hand.dealCard(i % 2 == 0 ? card : new Card(card.getSuite(), card.getFaceValue()));
      suiteCounts.put(card.getSuite(), suiteCounts.get(card.getSuite()) + 1);
      assertTrue(hand.containsCard(card));
      assertEquals(i, hand.getCardCount(card));
      assertEquals(suiteCounts.get(card.getSuite()).intValue(),
          hand.getCardCountBySuite(card.getSuite()));
    }));
    assertEquals(20, hand.numCards());

    for (Card card : cardTypes) {
      assertEquals(4, hand.getCardCount(card));
      assertEquals(suiteCounts.get(card.getSuite()).intValue(),
          hand.getCardCountBySuite(card.getSuite()));
      IntStream.rangeClosed(1, 4).forEach(i -> {
        assertTrue(hand.playCard(card));
        assertEquals(4 - i, hand.getCardCount(card));
        suiteCounts.put(card.getSuite(), suiteCounts.get(card.getSuite()) - 1);
        assertEquals(suiteCounts.get(card.getSuite()).intValue(),
            hand.getCardCountBySuite(card.getSuite()));
      });
      assertEquals(0, hand.getCardCount(card));
      assertEquals(suiteCounts.get(card.getSuite()).intValue(),
          hand.getCardCountBySuite(card.getSuite()));
      assertFalse(hand.containsCard(card));
    }
    assertEquals(0, hand.numCards());

    for (Card card : cardTypes) {
      assertEquals(0, hand.getCardCount(card));
      assertEquals(0, hand.getCardCountBySuite(card.getSuite()));
      assertFalse(hand.playCard(card));
      assertEquals(0, hand.getCardCount(card));
      assertEquals(0, hand.getCardCountBySuite(card.getSuite()));
    }
  }

  @Test
  void shouldReturnMeldForJacksAround() {
    // Jacks (A Jack in each suit):     4     40     60     80
    validateMeldAround(Jack, 4, 40, 60, 80);
  }

  @Test
  void shouldReturnMeldForQueensAround() {
    // Queens (A Queen in each suit):   6     60     90     120
    validateMeldAround(Queen, 6, 60, 90, 120);
  }

  @Test
  void shouldReturnMeldForKingsAround() {
    // Kings (A King in each suit):     8     80     120    160
    validateMeldAround(King, 8, 80, 120, 160);
  }

  @Test
  void shouldReturnMeldForAcesAround() {
    // Aces (An Ace in each suit):      10    100    150    200
    validateMeldAround(Ace, 10, 100, 150, 200);
  }

  @Test
  void shouldReturnMeldForPinochles() {
    // Pinochle(Jack of diamonds & Queen of spades):    4    30    60    90
    int[] expectedMelds = {4, 30, 60, 90};
    int[] actualMeldConstants = Hand.PINOCHLE_MELD;
    assertEquals(5, actualMeldConstants.length);
    assertEquals(0, actualMeldConstants[0]);

    Card jack = new Card(Suite.Diamond, Jack);
    Card queen = new Card(Suite.Spade, Queen);
    assertEquals(0, hand.countMeld(randomEnum(Suite.class)));
    for (int i = 0; i < 4; i++) {
      assertEquals(expectedMelds[i], actualMeldConstants[i + 1]);

      hand.dealCard(i % 2 == 0 ? jack : queen);
      assertEquals(i == 0 ? 0 : expectedMelds[i - 1], hand.countMeld(randomEnum(Suite.class)));

      hand.dealCard(i % 2 == 0 ? queen : jack);
      assertEquals(expectedMelds[i], hand.countMeld(randomEnum(Suite.class)));
    }
  }

  @Test
  void shouldReturnMeldForMarriageSingleSuiteNonTrump() {
    // Marriage (Kings and Queen of the same suit, not trump):  2     4      6      8
    int[] expectedMelds = {2, 4, 6, 8};
    assertEquals(2, Hand.MARRIAGE_MULTIPLIER);

    Suite suite = randomEnum(Suite.class);
    Card queen = new Card(suite, Queen);
    Card king = new Card(suite, King);

    Suite trump = Arrays.stream(Suite.values()).filter(ste -> ste != suite).findAny().get();
    assertEquals(0, hand.countMeld(suite));
    for (int i = 0; i < 4; i++) {
      hand.dealCard(i % 2 == 0 ? king : queen);
      assertEquals(i == 0 ? 0 : expectedMelds[i - 1], hand.countMeld(trump));

      hand.dealCard(i % 2 == 0 ? queen : king);
      assertEquals(expectedMelds[i], hand.countMeld(trump));
    }
  }

  @Test
  void shouldReturnMeldForMarriageInTrump() {
    // Royal Marriage (King and Queen of trump):  4     8      12     16
    int[] expectedMelds = {4, 8, 12, 16};
    assertEquals(2, Hand.MARRIAGE_MULTIPLIER);

    Suite trump = randomEnum(Suite.class);
    Card queen = new Card(trump, Queen);
    Card king = new Card(trump, King);

    assertEquals(0, hand.countMeld(trump));
    for (int i = 0; i < 4; i++) {
      hand.dealCard(i % 2 == 0 ? king : queen);
      assertEquals(i == 0 ? 0 : expectedMelds[i - 1], hand.countMeld(trump));

      hand.dealCard(i % 2 == 0 ? queen : king);
      assertEquals(expectedMelds[i], hand.countMeld(trump));
    }
  }

  @Test
  void shouldReturnMeldForMarriageInTrumpAndNonTrump() {
    Suite trump = randomEnum(Suite.class);
    Card queenTrump = new Card(trump, Queen);
    Card kingTrump = new Card(trump, King);

    Suite otherSuite =
        Arrays.stream(Suite.values()).filter(suite1 -> suite1 != trump).findAny().get();
    Card[] otherMarriages = {new Card(otherSuite, Queen), new Card(otherSuite, King)};

    for (int i = 0; i < 4; i++) {
      int expectedBase = i * 6;
      assertEquals(expectedBase, hand.countMeld(trump));

      hand.dealCard(queenTrump, kingTrump);
      assertEquals(expectedBase + 4, hand.countMeld(trump));

      hand.dealCard(otherMarriages);
      assertEquals((i + 1) * 6, hand.countMeld(trump));
    }
  }

  @Test
  void shouldReturnMeldForRunInTrump() {
    // Run (Ace, Ten, King, Queen, Jack of trump): 15    150    225    300
    int[] expectedMelds = {15, 150, 225, 300};
    int[] actualMeldConstants = Hand.RUN_MELD;
    assertEquals(5, actualMeldConstants.length);
    assertEquals(0, actualMeldConstants[0]);

    Suite trump = randomEnum(Suite.class);
    PinochleFaceValue[] faceValues = PinochleFaceValue.values();

    Card[] trumpRun = new Card[faceValues.length];
    IntStream.range(0, faceValues.length)
        .forEach(i -> trumpRun[i] = new Card(trump, faceValues[i]));

    assertEquals(0, hand.countMeld(trump));
    for (int i = 0; i < 4; i++) {
      assertEquals(expectedMelds[i], actualMeldConstants[i + 1]);

      hand.dealCard(trumpRun);
      assertEquals(expectedMelds[i], hand.countMeld(trump));

      Suite otherSuite;
      do {
        otherSuite = randomEnum(Suite.class);
      } while (otherSuite == trump);
      assertEquals(Hand.MARRIAGE_MULTIPLIER * (i + 1), hand.countMeld(otherSuite),
          "Meld should only be the sum of the marriages if not a run in trump.");
    }
  }

  @Test
  void shouldReturnMeldForRunInTrumpAccountingForRoyalMarriages() {
    // Run (Ace, Ten, King, Queen, Jack of trump): 15    150    225    300
    int[] expectedMelds = {15, 150, 225, 300};
    Suite trump = randomEnum(Suite.class);

    Card[] marriage = new Card[] {new Card(trump, Queen), new Card(trump, King)};
    Card[] others = new Card[] {new Card(trump, Jack), new Card(trump, Ten), new Card(trump, Ace)};

    assertEquals(0, hand.countMeld(trump));
    for (int i = 0; i < 4; i++) {
      hand.dealCard(marriage);
      assertEquals(i == 0 ? 4 : 4 + expectedMelds[i - 1], hand.countMeld(trump));

      hand.dealCard(others);
      assertEquals(expectedMelds[i], hand.countMeld(trump));
    }
  }

  @Test
  void shouldReturnMeldForMultipleRoyalMarriagesAboveRun() {
    // Run (Ace, Ten, King, Queen, Jack of trump): 15    150    225    300
    int[] expectedMelds = {15, 150, 225, 300};
    Suite trump = randomEnum(Suite.class);

    Card[] marriage = new Card[] {new Card(trump, Queen), new Card(trump, King)};
    IntStream.rangeClosed(1, 4).forEach(i -> {
      hand.dealCard(marriage);
      assertEquals(4 * i, hand.countMeld(trump));
    });
    int expectedMarriageExcess = 16;
    assertEquals(expectedMarriageExcess, hand.countMeld(trump));

    Card[] others = new Card[] {new Card(trump, Jack), new Card(trump, Ten), new Card(trump, Ace)};
    for (int i = 0; i < 4; i++) {
      expectedMarriageExcess -= 4;
      hand.dealCard(others);
      assertEquals(expectedMarriageExcess + expectedMelds[i], hand.countMeld(trump));
    }
  }

  private void validateMeldAround(PinochleFaceValue faceValue, int... values) {
    assertEquals(4, values.length);

    int[] meldAroundConstants = Hand.MELD_AROUND.get(faceValue);
    assertEquals(5, meldAroundConstants.length);
    assertEquals(0, meldAroundConstants[0]);

    for (int i = 0; i < values.length; i++) {
      assertEquals(values[i], meldAroundConstants[i + 1],
          "Expected '" + values[i] + "' for 2 " + faceValue.name() + "s around in constants map.");

      int expectedValue = i == 0 ? 0 : values[i - 1];
      Arrays.stream(Suite.values()).forEach(suite -> {
        assertEquals(expectedValue, hand.countMeld(randomEnum(Suite.class)));
        hand.dealCard(new Card(suite, faceValue));
      });

      assertEquals(values[i], hand.countMeld(randomEnum(Suite.class)),
          "Expected '" + values[i] + "' for " + (i + 1) + " " + faceValue.name() + "s around for "
              + "meld.");
    }
  }
}