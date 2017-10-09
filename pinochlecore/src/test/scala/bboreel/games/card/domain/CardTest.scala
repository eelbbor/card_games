package bboreel.games.card.domain

import bboreel.games.card.BaseTest
import org.junit.Assert.{assertEquals, assertNotEquals, assertTrue}

import scala.util.Random

/**
  * @author Robb Lee (robbmlee@gmail.com).
  */
class CardTest extends BaseTest {
  private val TestIdentifier = "Card Tests"
  private val defaultFaceValueString = "foo"

  test(s"$TestIdentifier: should not be equal for different ordinal value.") {
    val cardOne = Card(Suite.Heart, FaceValue(0, defaultFaceValueString))
    val cardTwo = Card(Suite.Heart, FaceValue(1, defaultFaceValueString))
    assertNotEquals(cardOne, cardTwo)
  }

  test(s"$TestIdentifier: should not be equal for different string value.") {
    val cardOne = Card(Suite.Heart, FaceValue(1, defaultFaceValueString))
    val cardTwo = Card(Suite.Heart, FaceValue(1, defaultFaceValueString + "2"))
    assertNotEquals(cardOne, cardTwo)
  }

  test(s"$TestIdentifier: should not be equal for different suite.") {
    val faceValueOne = FaceValue(0, defaultFaceValueString)
    val faceValueTwo = FaceValue(0, defaultFaceValueString)
    assertEquals(faceValueOne, faceValueTwo)

    val cardOne = Card(Suite.Heart, faceValueOne)
    val cardTwo = Card(Suite.Diamond, faceValueTwo)
    assertNotEquals(cardOne, cardTwo)
  }

  test(s"$TestIdentifier: should be equal for value and suite.") {
    val cardOne = Card(Suite.Clubs, FaceValue(2, defaultFaceValueString))
    val cardTwo = Card(Suite.Clubs, FaceValue(2, defaultFaceValueString))
    assertEquals(cardOne, cardTwo)
  }

  test(s"$TestIdentifier: should sort by suite then by value.") {
    val numCardsPerSuite = 13
    val cards: List[Card] =
      Random.shuffle(Utils.createSetOfCardsForAllSuites(1 to numCardsPerSuite))
    assertEquals(4 * numCardsPerSuite, cards.size)

    assertTrue(s"Should have multiple sets in the first $numCardsPerSuite cards.",
      (0 until numCardsPerSuite).map(cards(_).suite).toSet.size > 1)

    val sortedCards: List[Card] = cards.sorted(Ordering[Card])
    // Validate each of the suits is in proper numeric order.
    var offset = 0
    Suite.values.toList.foreach((suite: Suite.Value) => {
      for {index <- 1 until numCardsPerSuite} {
        val card: Card = sortedCards(index + offset - 1)
        assertEquals(index, card.value.ordinal)
        assertEquals(index.toString, card.value.value)
        assertEquals(suite, card.suite)
      }
      offset += numCardsPerSuite
    })
  }
}
