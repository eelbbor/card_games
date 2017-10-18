package bboreel.games.card.game

import bboreel.games.card.domain.Card

import scala.collection.mutable.ArrayBuffer

/**
  * Represents a players hand. Tracks the cards in the users hand and allow for dealing and
  * playing cards.
  * @author Robb Lee (robbmlee@gmail.com).
  */
class Hand extends {
  private var cards: ArrayBuffer[Card] = new ArrayBuffer[Card]()

  /**
    * Determines if the hand has any cards.
    *
    * @return [[Boolean]] true if the [[Hand]] has any cards else false.
    */
  def isEmpty(): Boolean = {
    cards.isEmpty
  }

  /**
    * Determines if the hand contains a card by definition.
    *
    * @param card [[Card]] object to check hand for.
    * @return [[Boolean]] true if the [[Card]] exists else false.
    */
  def contains(card: Card): Boolean = {
    cards.contains(card)
  }

  /**
    * Returns the number of cards in a hand.
    *
    * @return [[Int]] value of the number of cards in the hand.
    */
  def size(): Int = {
    cards.size
  }

  /**
    * Adds a card to the hand. Allows for adding multiple instances of the same card by reference
    * or as a seperate instance.
    *
    * @param card [[Card]] definition of a card to add to the hand.
    */
  def dealCard(card: Card): Unit = {
    cards.append(card)
  }

  /**
    * Plays a single card from the hand, if more than one card exists only a single card is played.
    * Note: This method does not protect against tying to play a non-existent card.
    *
    * @param card [[Card]] object to remove from the hand.
    */
  def playCard(card: Card): Card = {
    val cardIndex = cards.indexOf(card)
    if (cardIndex < 0) {
      throw new RuntimeException(s"Tried to remove invalid card: $card")
    }
    cards.remove(cardIndex)
  }
}
