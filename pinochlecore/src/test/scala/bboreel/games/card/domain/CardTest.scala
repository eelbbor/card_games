package bboreel.games.card.domain

import bboreel.games.card.BaseTest
import org.junit.Assert.{assertEquals, assertNotEquals}

/**
  * @author Robb Lee (robbmlee@gmail.com).
  */
class CardTest extends BaseTest {
  private val TestIdentifier = "Card Tests"
  private val defaultFaceValueString = "foo"

  test(s"$TestIdentifier: Card should not be equal for different ordinal value.") {
    val cardOne = Card(Suite.Heart, FaceValue(0, defaultFaceValueString))
    val cardTwo = Card(Suite.Heart, FaceValue(1, defaultFaceValueString))
    assertNotEquals(cardOne, cardTwo)
  }

  test(s"$TestIdentifier: Card should not be equal for different string value.") {
    val cardOne = Card(Suite.Heart, FaceValue(1, defaultFaceValueString))
    val cardTwo = Card(Suite.Heart, FaceValue(1, defaultFaceValueString + "2"))
    assertNotEquals(cardOne, cardTwo)
  }

  test(s"$TestIdentifier: Card should not be equal for different suite.") {
    val faceValueOne = FaceValue(0, defaultFaceValueString)
    val faceValueTwo = FaceValue(0, defaultFaceValueString)
    assertEquals(faceValueOne, faceValueTwo)

    val cardOne = Card(Suite.Heart, faceValueOne)
    val cardTwo = Card(Suite.Diamond, faceValueTwo)
    assertNotEquals(cardOne, cardTwo)
  }

  test(s"$TestIdentifier: Card should be equal for value and suite.") {
    val cardOne = Card(Suite.Club, FaceValue(2, defaultFaceValueString))
    val cardTwo = Card(Suite.Club, FaceValue(2, defaultFaceValueString))
    assertEquals(cardOne, cardTwo)
  }
}
