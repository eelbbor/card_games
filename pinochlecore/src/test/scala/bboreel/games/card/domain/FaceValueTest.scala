package bboreel.games.card.domain

import bboreel.games.card.BaseTest
import org.junit.Assert.assertTrue

/**
  * @author Robb Lee (robbmlee@gmail.com).
  */
class FaceValueTest extends BaseTest {
  private val TestIdentifier = "FaceValue Tests"

  test(s"$TestIdentifier: Face values should be ordered by ordinal number.") {
    val faceValue = "foo"
    val numberOfCards = 10
    val values = Range(numberOfCards, 0, -1).map((ordinal: Int) => FaceValue(ordinal, faceValue))

    for {ordinal <- 1 to numberOfCards - 1} {
      assertTrue(values(ordinal - 1) > values(ordinal))
    }

    val sortedValues = values.sorted
    for {ordinal <- 1 to numberOfCards - 1} {
      assertTrue(sortedValues(ordinal) > sortedValues(ordinal - 1))
    }
  }
}
