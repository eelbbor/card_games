package org.eelbbor.carddeck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Enables standard behaviors for a hand in a card game such as being dealt or playing a card.
 *
 * @author Robb Lee (robbmlee@gmail.com).
 */
public class Hand<T extends Card> {
  private List<T> cards;

  public Hand() {
    this.cards = new ArrayList<>();
  }

  /**
   * Indicates the number of cards in the hand.
   *
   * @return int value representing the number of cards in the hand.
   */
  public int numCards() {
    return cards.size();
  }

  /**
   * Indicates if a card is present in the hand.
   *
   * @param card {#link Card} to check the hand for.
   * @return true if the card is in the hand, else false.
   */
  public boolean containsCard(T card) {
    return cards.isEmpty() ? false : cards.contains(card);
  }

  /**
   * Adds a card or cards to the hand.
   *
   * @param card variable length number of cards to add to the hand.
   */
  public void dealCard(T... card) {
    cards.addAll(Arrays.asList(card));
  }

  /**
   * Removes the card if it is present in the hand. Unopinionated method does not throw
   * exception if
   * the card is not in the hand. Use the return value to indicate if the card was played.
   *
   * @param card {@link Card} object to play.
   * @return true if the card was in the hand, else false
   */
  public boolean playCard(T card) {
    return cards.remove(card);
  }

  /**
   * Returns a list of the cards remaining in the hand. Note: The return value is not tied to the
   * cards in the hands so will mutating it will have no side effect.
   *
   * @return list containing the remaining cards.
   */
  public List<T> remainingCards() {
    return new ArrayList<>(cards);
  }
}
