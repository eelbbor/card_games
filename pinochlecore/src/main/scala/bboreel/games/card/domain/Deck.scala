package bboreel.games.card.domain

import scala.util.Random

/**
  * Generic card deck definition based on the provided [[Card]] definitions. The instance is
  * designed to provide convenience methods allowing for reuse such as shuffling.
  *
  * @param cards [[List]] of [[Card]] objects to define the cards present in a deck.
  * @author Robb Lee (robbmlee@gmail.com).
  */
case class Deck(cards: List[Card]) {
  var deck: List[Card] = cards

  /**
    * Shuffles the original card definitions and creates a new shuffle list of cards. The
    * [[Deck.deck]] is updated and tracks the new shuffle order.
    * @return [[List]] of [[Card]] objects with new suffled order.
    */
  def shuffle(): List[Card] = {
    deck = Random.shuffle(deck)
    deck
  }
}
