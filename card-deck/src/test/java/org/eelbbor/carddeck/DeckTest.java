package org.eelbbor.carddeck;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.eelbbor.carddeck.TestUtils.randomInteger;
import static org.eelbbor.carddeck.TestUtils.randomString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class DeckTest {
    @Test
    void shouldNotBeAbleToMutateDeckFromOriginalList() {
        int numberOfCards = 100;
        List<Card> cardsArg = createRandomCards(numberOfCards);
        Deck deck = new Deck(cardsArg);
        assertEquals(numberOfCards, deck.size());

        while(cardsArg.size() > 0) {
            cardsArg.remove(TestUtils.randomInteger(cardsArg.size()));
            assertEquals(numberOfCards, deck.size());
            assertEquals(numberOfCards, deck.remainingCardsCount());
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
        while(cardsArg.size() < repeatedCardCount) {
            cardsArg.add(card);
        }
        Deck deck = new Deck(cardsArg);
        assertEquals(repeatedCardCount, deck.size());
        assertEquals(repeatedCardCount, deck.remainingCardsCount());
        validateDealingAllCards(repeatedCardCount, deck);
    }

    @Test
    void shouldReturnEmptyOptionalIfNoRemainingCardsToBeDealt() {
        List<Card> randomCards = createRandomCards(1);
        Card card = randomCards.get(0);
        Deck deck = new Deck(randomCards);
        assertEquals(card, deck.dealCard().get());
        assertEquals(0, deck.remainingCardsCount());
        assertEquals(1, deck.dealtCardsCount());

        assertFalse(deck.dealCard().isPresent());
    }

    @Test
    void shouldThrowExceptionTryingToDiscardWithANullArgument() {
        try {
            new Deck(createRandomCards(52)).discardCard(null);
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
        for (int i = 0; i < numberOfCards/ 2; i++) {
            dealtCards.add(deck.dealCard().get());
        }

        Card invalidDiscardCard;
        do {
            invalidDiscardCard = createRandomCard();
        } while (dealtCards.contains(invalidDiscardCard));

        try {
            deck.discardCard(invalidDiscardCard);
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

    private void validateDealtCardsAreDiscarded(int numberOfCards, Deck deck) {
        List<Card> dealtCards = new ArrayList<>();
        for (int i = 0; i < numberOfCards / 2; i++) {
            dealtCards.add(deck.dealCard().get());
        }
        int dealtCardsCount = deck.dealtCardsCount();
        assertEquals(dealtCards.size(), dealtCardsCount);

        int discardCardCount = 0;
        assertEquals(discardCardCount, deck.discardCardsCount());
        for (Card dealtCard : dealtCards) {
            deck.discardCard(dealtCard);
            assertEquals(dealtCard, deck.lastDiscardCard().get());
            dealtCardsCount--;
            assertEquals(dealtCardsCount, deck.dealtCardsCount());
            discardCardCount++;
            assertEquals(discardCardCount, deck.discardCardsCount());
        }
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
    void shouldShuffleDeckWithAllCardsRemaining() {
        fail("");
    }

    @Test
    void shouldShuffleDeckWithAllCardsDealt() {
        fail("");
    }

    @Test
    void shouldShuffleDeckWithAllCardsDiscarded() {
        fail("");
    }

    @Test
    void shouldCombineAllCardsBackToRemainingAndReshuffle() {
        fail("");
    }

    private void validateDealingAllCards(int repeatedCardCount, Deck deck) {
        for (int dealtCards = 1; dealtCards <= repeatedCardCount; dealtCards++) {
            assertTrue(deck.dealCard().isPresent());
            assertEquals(repeatedCardCount, deck.size());
            assertEquals(repeatedCardCount - dealtCards, deck.remainingCardsCount());
            assertEquals(dealtCards, deck.dealtCardsCount());
            assertEquals(0, deck.discardCardsCount());
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