package bboreel.games.card.game

import bboreel.games.card.domain.Card

import scala.collection.mutable.ArrayBuffer

/**
  * @author Robb Lee (robbmlee@gmail.com).
  */
class Hand {
  private var cards: ArrayBuffer[Card] = new ArrayBuffer[Card]()

  def isEmpty(): Boolean = {
    cards.isEmpty
  }

  def dealCard(card: Card): Unit = {
    cards.append(card)
  }

  def playCard(card: Card): Unit = {
    cards.remove(cards.indexOf(card))
  }
}
