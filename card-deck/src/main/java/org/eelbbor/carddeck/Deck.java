package org.eelbbor.carddeck;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

/**
 * Generic deck enables basic functionality for maintaining a deck. Note: The implementation
 * allows for a single creation such that the cards can be shuffled and recombined back into
 * available cards.
 *
 * @author Robb Lee (robbmlee@gmail.com).
 */
public class Deck {
    private int size;
    private List<Card> remainingCards;
    private List<Card> dealtCards;
    private Stack<Card> discardPile;

    public Deck(List<Card> cardList) {
        if (cardList == null || cardList.size() < 1) {
            throw new IllegalArgumentException("The card list cannot be null nor empty.");
        }
        this.size = cardList.size();
        this.remainingCards = new ArrayList<>(cardList);
        this.dealtCards = new ArrayList<>();
        this.discardPile = new Stack<>();
    }

    public int size() {
        return size;
    }

    /**
     * Reports the number of cards remaining in the deck, which can be dealt.
     * @return
     */
    public int remainingCardsCount() {
        return remainingCards.size();
    }

    /**
     * Reports the number of cards dealt, but not in the discard pile.
     * @return
     */
    public int dealtCardsCount() {
        return dealtCards.size();
    }

    /**
     * Reports the number of cards returned to the discard pile.
     * @return
     */
    public int discardCardsCount() {
        return discardPile.size();
    }

    /**
     * Recombines all the cards back and shuffles them back into the remaining cards collection.
     */
    public void shuffle() {
        throw new UnsupportedOperationException("Method is not implemented yet.");
    }

    /**
     * Deals the next available card from the remaining cards as an {@link Optional} to account
     * for all cards removed.
     *
     * @return {@link Optional<Card>} next card in the remaining cards, no value if empty.
     */
    public Optional<Card> dealCard() {
        Optional<Card> card =
            Optional.ofNullable(remainingCards.isEmpty() ? null : remainingCards.remove(0));
        card.ifPresent(card1 -> dealtCards.add(card1));
        return card;
    }

    /**
     * Places the card on the top of the discard pile (stack). Note: Only works of the card is in
     * the dealt cards otherwise a runtime exception is thrown.
     *
     * @param card {@link Card} to be discarded.
     */
    public void discardCard(Card card) {
        if (card == null || !dealtCards.remove(card)) {
            throw new IllegalArgumentException(
                "Cannot discard a card not present in the dealt cards");
        }
        discardPile.push(card);
    }

    public Optional<Card> lastDiscardCard() {
        return Optional.ofNullable(discardPile.empty() ? null : discardPile.peek());
    }
}
