package org.eelbbor.carddeck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

/**
 * Generic deck enables basic functionality for maintaining a deck. Note: The implementation allows
 * for a single creation such that the cards can be shuffled and recombined back into available
 * cards.
 *
 * @author Robb Lee (robbmlee@gmail.com).
 */
public class Deck<T extends Card> {
  private int size;
  private List<T> remainingCards;
  private List<T> dealtCards;
  private Stack<T> discardStack;

  /**
   * Creates a deck of cards based on the provided {@link List} of cards. The list must be greater
   * than zero in length.
   *
   * @param cardList {@link List} of cards present in the deck.
   */
  public Deck(List<T> cardList) {
    if (cardList == null || cardList.size() < 1) {
      throw new IllegalArgumentException("The card list cannot be null nor empty.");
    }
    this.size = cardList.size();
    this.remainingCards = new LinkedList<>(cardList);
    this.dealtCards = new ArrayList<>();
    this.discardStack = new Stack<>();
  }

  /**
   * Reports the number of cards in a deck.
   *
   * @return int value for the number of cards present.
   */
  public int size() {
    return size;
  }

  /**
   * Recombines all the cards back and shuffles them back into the remaining cards collection.
   */
  public void shuffle() {
    remainingCards.addAll(dealtCards);
    dealtCards.clear();

    remainingCards.addAll(discardStack);
    discardStack.clear();

    Collections.shuffle(remainingCards);
  }

  /**
   * Reports the number of cards remaining in the deck, which can be dealt.
   *
   * @return number of cards in remaining list.
   */
  public int remainingCount() {
    return remainingCards.size();
  }

  /**
   * Deals the next available card from the remaining cards as an {@link Optional} to account for
   * all cards removed.
   *
   * @return {@link Optional} with next {@link Card} in the remaining cards, no value if empty.
   */
  public Optional<T> deal() {
    Optional<T> card =
        Optional.ofNullable(remainingCards.isEmpty() ? null : remainingCards.remove(0));
    card.ifPresent(card1 -> dealtCards.add(card1));
    return card;
  }

  /**
   * Reports the number of cards dealt, but not on the discard stack.
   *
   * @return number of cards dealt but not discarded.
   */
  public int dealtCount() {
    return dealtCards.size();
  }

  /**
   * Places the card on the top of the discard pile (stack). Note: Only works if the card is
   * available in the dealt cards otherwise an exception is thrown.
   *
   * @param card {@link Card} to be discarded.
   */
  public void discard(T card) {
    if (card == null || !dealtCards.remove(card)) {
      throw new IllegalArgumentException("Cannot discard a card not present in the dealt cards");
    }
    discardStack.push(card);
  }

  /**
   * Reports the card on the top of the discard stack if one is present.
   *
   * @return {@link Optional} with {@link Card} on the top of the discard stack unless empty.
   */
  public Optional<T> lastDiscard() {
    return discardStack.isEmpty() ? Optional.empty() : Optional.of(discardStack.peek());
  }

  /**
   * Deals the top card from the discard if present and moves it back into the dealt cards.
   *
   * @return {@link Optional} with {@link Card} from the top of the discard stack unless empty.
   */
  public Optional<T> drawDiscard() {
    if (discardStack.isEmpty()) {
      return Optional.empty();
    }
    T card = discardStack.pop();
    dealtCards.add(card);
    return Optional.of(card);
  }

  /**
   * Reports the number of cards returned on the discard stack.
   *
   * @return number of cards on the discard stack.
   */
  public int discardCount() {
    return discardStack.size();
  }

}
