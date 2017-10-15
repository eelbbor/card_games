package bboreel.games.card.domain

import scala.util.Random

/**
  * Generic card deck definition based on the provided [[Card]] definitions. The instance is
  * designed to provide convenience methods allowing for reuse such as shuffling.
  *
  * @param cards [[Vector]] of [[Card]] objects to define the cards present in a deck.
  * @author Robb Lee (robbmlee@gmail.com).
  */
case class Deck(cards: Vector[Card]) {
  private final val deckSize: Int = cards.size
  var deck: Vector[Card] = cards

  /**
    * Shuffles the original card definitions and creates a new shuffle list of cards. The
    * [[Deck.deck]] is updated and tracks the new shuffle order.
    * @return [[Vector]] of [[Card]] objects with new suffled order.
    */
  def shuffle(): Vector[Card] = {
    deck = Random.shuffle(deck)
    deck
  }

  /**
    * Convenience method to get the number of cards in the deck.
    * @return [[Int]] number of cards in the deck.
    */
  def size(): Int = {
    deckSize
  }
}
