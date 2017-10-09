package bboreel.games.card.domain

import bboreel.games.card.BaseTest
import org.junit.Assert.{assertEquals, assertNotEquals}

/**
  * @author Robb Lee (robbmlee@gmail.com).
  */
class DeckTest extends BaseTest {
  private val TestIdentifier = "Deck Tests"

  test(s"$TestIdentifier: should shuffle cards and maintain shuffled cards.") {
    val numCardsPerSuite = 13
    val cards = Utils.createSetOfCardsForAllSuites(1 to numCardsPerSuite)
    val deck = Deck(cards)

    val shuffleOne = deck.shuffle()
    assertNotEquals(deck.cards, shuffleOne)
    assertEquals(deck.deck.map((card: Card) => card), shuffleOne)

    val shuffleTwo = deck.shuffle()
    assertNotEquals(deck.cards, shuffleTwo)
    assertEquals(deck.deck.map((card: Card) => card), shuffleTwo)

    assertNotEquals(shuffleOne, shuffleTwo)
  }
}
