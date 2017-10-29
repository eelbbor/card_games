package org.eelbbor.carddeck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Enables standard behaviors for a hand in a card game such as being dealt or playing a card.
 *
 * @author Robb Lee (robbmlee@gmail.com).
 */
public class Hand {
  private List<Card> cards;

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
  public boolean containsCard(Card card) {
    return cards.isEmpty() ? false : cards.contains(card);
  }

  /**
   * Adds a card or cards to the hand.
   *
   * @param card variable length number of cards to add to the hand.
   */
  public void dealCard(Card... card) {
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
  public boolean playCard(Card card) {
    return cards.remove(card);
  }
}
