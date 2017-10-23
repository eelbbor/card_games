package org.eelbbor.carddeck;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.eelbbor.carddeck.TestUtils.randomInteger;
import static org.eelbbor.carddeck.TestUtils.randomString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HandTest {
    private Hand hand;

    @BeforeEach
    void setUp() {
        hand = new Hand();
    }

    @Test
    void shouldAllowAdditionalCardsToBeDealt() {
        Card[] initialCards = {createRandomCard(), createRandomCard()};
        hand.dealCard(initialCards);
        assertEquals(initialCards.length, hand.numCards());
        Arrays.stream(initialCards).forEach(card -> assertTrue(hand.containsCard(card)));

        Card additional = createRandomCard();
        hand.dealCard(additional);
        assertEquals(initialCards.length + 1, hand.numCards());
        assertTrue(hand.containsCard(additional));
        Arrays.stream(initialCards).forEach(card -> assertTrue(hand.containsCard(card)));
    }

    @Test
    void shouldAllowMultipleIdenticalCardsToBeDealt() {
        Card random = createRandomCard();
        hand.dealCard(random);
        hand.dealCard(random);
        assertEquals(2, hand.numCards());

        Card randomCopy = new Card(random.getType(), random.getOrdinal(), random.getFaceValue());
        hand.dealCard(randomCopy);
        assertEquals(3, hand.numCards());
    }

    @Test
    void shouldPlaySingleCard() {
        int numCards = 100;
        Set<Card> cardSet = new HashSet<>();
        while(cardSet.size() < numCards) {
            cardSet.add(createRandomCard());
        }
        cardSet.stream().forEach(card -> hand.dealCard(card));
        assertEquals(numCards, hand.numCards());

        cardSet.stream().forEach(card -> {
            assertTrue(hand.containsCard(card));
            assertTrue(hand.playCard(card));
        });
        assertEquals(0, hand.numCards());
    }

    @Test
    void shouldPlaySingleCardOfDuplicates() {
        Card random = createRandomCard();
        hand.dealCard(random);
        hand.dealCard(random);

        Card randomCopy = new Card(random.getType(), random.getOrdinal(), random.getFaceValue());
        hand.dealCard(randomCopy);
        assertEquals(3, hand.numCards());

        assertTrue(hand.playCard(random));
        assertEquals(2, hand.numCards());
        assertTrue(hand.containsCard(random));
        assertTrue(hand.containsCard(randomCopy));

        assertTrue(hand.playCard(randomCopy));
        assertEquals(1, hand.numCards());
        assertTrue(hand.containsCard(random));
        assertTrue(hand.containsCard(randomCopy));

        assertTrue(hand.playCard(random));
        assertEquals(0, hand.numCards());
    }

    @Test
    void playCardFromEmptyHand() {
        assertFalse(hand.playCard(createRandomCard()));
    }

    @Test
    void playCardNotInHand() {
        hand.dealCard(createRandomCard());
        assertFalse(hand.playCard(createRandomCard()));
    }

    private Card createRandomCard() {
        return new Card(new Type(randomInteger(), randomString()), randomInteger(), randomString());
    }
}