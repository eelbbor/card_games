package org.eelbbor.carddeck;

import static org.eelbbor.carddeck.TestUtils.randomInteger;
import static org.eelbbor.carddeck.TestUtils.randomString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

class DeckTest {
  @Test
  void shouldNotBeAbleToMutateDeckFromOriginalList() {
    int numberOfCards = 100;
    List<Card> cardsArg = createRandomCards(numberOfCards);
    Deck deck = new Deck(cardsArg);
    assertEquals(numberOfCards, deck.size());

    while (cardsArg.size() > 0) {
      cardsArg.remove(TestUtils.randomInteger(cardsArg.size()));
      assertEquals(numberOfCards, deck.size());
      assertEquals(numberOfCards, deck.remainingCount());
    }
  }

  @Test
  void shouldDealCardsInTheSameIterableOrderAsTheProvidedList() {
    int numberOfCards = 100;
    List<Card> cardsArg = createRandomCards(numberOfCards);
    Deck deck = new Deck(cardsArg);

    while (cardsArg.size() > 0) {
      assertEquals(cardsArg.remove(0), deck.deal().get());
    }
  }

  @Test
  void shouldThrowExceptionForNullListOfCardsToConstructor() {
    try {
      new Deck(null);
      fail("Should have thrown exception trying to create deck with null cards list.");
    } catch (IllegalArgumentException ex) {
      assertEquals("The card list cannot be null nor empty.", ex.getMessage());
    }
  }

  @Test
  void shouldThrowExceptionForEmptyListOfCardsToConstructor() {
    try {
      new Deck(new ArrayList<>());
      fail("Should have thrown exception trying to create deck with empty cards list.");
    } catch (IllegalArgumentException ex) {
      assertEquals("The card list cannot be null nor empty.", ex.getMessage());
    }
  }

  @Test
  void shouldRemoveDealtCardFromRemainingCardsAndAddToDealtCards() {
    int numberOfCards = 100;
    Deck deck = new Deck(createRandomCards(numberOfCards));
    validateDealingAllCards(numberOfCards, deck);
  }

  @Test
  void shouldRemoveSingleDealtCardOfMultipleIdenticalCardsFromRemainingCardsAndAddToDealtCards() {
    Card card = createRandomCard();
    List<Card> cardsArg = new ArrayList<>();
    int repeatedCardCount = 100;
    while (cardsArg.size() < repeatedCardCount) {
      cardsArg.add(card);
    }
    Deck deck = new Deck(cardsArg);
    assertEquals(repeatedCardCount, deck.size());
    assertEquals(repeatedCardCount, deck.remainingCount());
    validateDealingAllCards(repeatedCardCount, deck);
  }

  @Test
  void shouldReturnEmptyOptionalIfNoRemainingCardsToBeDealt() {
    List<Card> randomCards = createRandomCards(1);
    Card card = randomCards.get(0);
    Deck deck = new Deck(randomCards);
    assertEquals(card, deck.deal().get());
    assertEquals(0, deck.remainingCount());
    assertEquals(1, deck.dealtCount());

    assertFalse(deck.deal().isPresent());
  }

  @Test
  void shouldThrowExceptionTryingToDiscardWithANullArgument() {
    try {
      new Deck(createRandomCards(52)).discard(null);
      fail("Should have thrown exception trying to discard null card.");
    } catch (IllegalArgumentException ex) {
      assertEquals("Cannot discard a card not present in the dealt cards", ex.getMessage());
    }
  }

  @Test
  void shouldThrowExceptionTryingToDiscardACardNotInTheDealtCards() {
    int numberOfCards = 52;
    Deck deck = new Deck(createRandomCards(numberOfCards));
    List<Card> dealtCards = new ArrayList<>();
    for (int i = 0; i < numberOfCards / 2; i++) {
      dealtCards.add(deck.deal().get());
    }

    Card invalidDiscardCard;
    do {
      invalidDiscardCard = createRandomCard();
    } while (dealtCards.contains(invalidDiscardCard));

    try {
      deck.discard(invalidDiscardCard);
      fail("Should have thrown exception trying to discard null card.");
    } catch (IllegalArgumentException ex) {
      assertEquals("Cannot discard a card not present in the dealt cards", ex.getMessage());
    }
  }

  @Test
  void shouldPlaceCardOnDiscardPileAndRemoveFromDealtCards() {
    int numberOfCards = 100;
    Deck deck = new Deck(createRandomCards(numberOfCards));
    validateDealtCardsAreDiscarded(numberOfCards, deck);
  }

  @Test
  void shouldPlaceMultipleIdenticalCardsOnDiscardPileAndRemoveFromDealtCards() {
    int numberOfCards = 100;
    Card card = createRandomCard();
    List<Card> randomCards = new ArrayList<>();
    while (randomCards.size() < numberOfCards) {
      randomCards.add(card);
    }
    Deck deck = new Deck(randomCards);
    validateDealtCardsAreDiscarded(numberOfCards, deck);
  }

  @Test
  void shouldRemoveCardFromDiscardPileAndMoveItBackToTheDealtCards() {
    int numberOfCards = 100;
    Deck deck = new Deck(createRandomCards(numberOfCards));
    assertFalse(deck.drawDiscard().isPresent());

    // Deal and discard some of the cards.
    IntStream.range(0, 2 * numberOfCards / 3).forEach(i -> {
      Card card = deck.deal().get();
      if (i % 2 == 0) {
        deck.discard(card);
        assertEquals(card, deck.lastDiscard().get());
      }
    });

    int dealtCount = deck.dealtCount();
    int discardCount = deck.discardCount();
    while (discardCount > 0) {
      deck.drawDiscard();
      assertEquals(--discardCount, deck.discardCount(), "Should have removed discard.");
      assertEquals(++dealtCount, deck.dealtCount(), "Should have put card into dealt.");
    }
    assertEquals(0, deck.discardCount());
    assertFalse(deck.lastDiscard().isPresent());
    assertFalse(deck.drawDiscard().isPresent());
  }

  @Test
  void shouldShuffleDeckWithAllCardsRemaining() {
    int numberOfCards = 100;
    List<Card> originalList = createRandomCards(numberOfCards);
    Deck deck = new Deck(originalList);
    assertEquals(numberOfCards, deck.remainingCount());
    deck.shuffle();
    int delta = 0;
    while (originalList.size() > 0) {
      delta += originalList.remove(0).equals(deck.deal().get()) ? 0 : 1;
    }
    assertTrue(delta > 0, "Shuffling the deck should have yielded a different order.");
  }

  @Test
  void shouldShuffleDeckWithAllCardsDealt() {
    int numberOfCards = 100;
    List<Card> originalList = createRandomCards(numberOfCards);
    Deck deck = new Deck(originalList);

    // Deal all the cards.
    IntStream.range(0, numberOfCards).forEach(i -> deck.deal());
    assertEquals(0, deck.remainingCount());
    assertEquals(numberOfCards, deck.dealtCount());

    // Reshuffle the deck.
    deck.shuffle();
    assertEquals(numberOfCards, deck.remainingCount());
    assertEquals(0, deck.dealtCount());

    int delta = 0;
    for (int i = 0; i < numberOfCards; i++) {
      delta += originalList.get(i).equals(deck.deal().get()) ? 0 : 1;
    }
    assertTrue(delta > 0, "Shuffling the deck should have yielded a different order.");
  }

  @Test
  void shouldShuffleDeckWithAllCardsDiscarded() {
    int numberOfCards = 100;
    List<Card> originalList = createRandomCards(numberOfCards);
    Deck deck = new Deck(originalList);

    // Deal and discard all the cards.
    IntStream.range(0, numberOfCards).forEach(i -> deck.discard(deck.deal().get()));
    assertEquals(0, deck.remainingCount());
    assertEquals(numberOfCards, deck.discardCount());

    // Reshuffle the deck.
    deck.shuffle();
    assertEquals(numberOfCards, deck.remainingCount());
    assertEquals(0, deck.discardCount());

    int delta = 0;
    for (int i = 0; i < numberOfCards; i++) {
      delta += originalList.get(i).equals(deck.deal().get()) ? 0 : 1;
    }
    assertTrue(delta > 0, "Shuffling the deck should have yielded a different order.");
  }

  @Test
  void shouldCombineAllCardsBackToRemainingAndReshuffle() {
    int numberOfCards = 99;
    List<Card> originalList = createRandomCards(numberOfCards);
    Deck deck = new Deck(originalList);

    // Deal and discard some of the cards.
    IntStream.range(0, 2 * numberOfCards / 3).forEach(i -> {
      Card card = deck.deal().get();
      if (i % 2 == 0) {
        deck.discard(card);
      }
    });
    assertEquals(33, deck.remainingCount());
    assertEquals(33, deck.dealtCount());
    assertEquals(33, deck.discardCount());

    // Reshuffle the deck.
    deck.shuffle();
    assertEquals(numberOfCards, deck.remainingCount());

    int delta = 0;
    for (int i = 0; i < numberOfCards; i++) {
      delta += originalList.get(i).equals(deck.deal().get()) ? 0 : 1;
    }
    assertTrue(delta > 0, "Shuffling the deck should have yielded a different order.");
  }

  private void validateDealingAllCards(int repeatedCardCount, Deck deck) {
    for (int dealtCards = 1; dealtCards <= repeatedCardCount; dealtCards++) {
      assertTrue(deck.deal().isPresent());
      assertEquals(repeatedCardCount, deck.size());
      assertEquals(repeatedCardCount - dealtCards, deck.remainingCount());
      assertEquals(dealtCards, deck.dealtCount());
      assertEquals(0, deck.discardCount());
    }
  }

  private void validateDealtCardsAreDiscarded(int numberOfCards, Deck deck) {
    assertTrue(numberOfCards >= 10);
    List<Card> dealtCards = new ArrayList<>();
    for (int i = 0; i < numberOfCards / 2; i++) {
      dealtCards.add(deck.deal().get());
    }
    int dealtCardsCount = deck.dealtCount();
    assertEquals(dealtCards.size(), dealtCardsCount);
    assertEquals(0, deck.discardCount());
    assertFalse(deck.lastDiscard().isPresent());

    int discardCardCount = 0;
    assertEquals(discardCardCount, deck.discardCount());
    for (Card dealtCard : dealtCards) {
      deck.discard(dealtCard);
      assertEquals(dealtCard, deck.lastDiscard().get());
      dealtCardsCount--;
      assertEquals(dealtCardsCount, deck.dealtCount());
      discardCardCount++;
      assertEquals(discardCardCount, deck.discardCount());
    }
  }

  private List<Card> createRandomCards(int numberOfCards) {
    List<Card> cardsArg = new ArrayList<>();
    while (cardsArg.size() < numberOfCards) {
      cardsArg.add(createRandomCard());
    }
    return cardsArg;
  }

  private Card createRandomCard() {
    Type type = new Type(randomInteger(), randomString());
    return new Card(type, randomInteger(), randomString());
  }
}