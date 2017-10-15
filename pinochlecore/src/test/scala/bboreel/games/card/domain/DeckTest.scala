package bboreel.games.card.domain

import bboreel.games.card.BaseTest
import bboreel.games.card.TestUtils.{
  MaxCardValue,
  createVectorOfCardsForAllSuites,
  randomPositiveInteger}
import org.junit.Assert.{assertEquals, assertNotEquals}

/**
  * @author Robb Lee (robbmlee@gmail.com).
  */
class DeckTest extends BaseTest {
  private final val TestIdentifier = "Deck Tests"

  test(s"$TestIdentifier: should report the size of the deck.") {
    val numCardsPerSuite = randomPositiveInteger(MaxCardValue)
    val cards = createVectorOfCardsForAllSuites(1 to numCardsPerSuite)
    assertEquals(Suite.values.size * numCardsPerSuite, Deck(cards).size())
    assertEquals(cards.size, Deck(cards).size())
  }

  test(s"$TestIdentifier: should shuffle cards and maintain shuffled cards.") {
    val numCardsPerSuite = randomPositiveInteger(MaxCardValue)
    val cards = createVectorOfCardsForAllSuites(1 to numCardsPerSuite)
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
