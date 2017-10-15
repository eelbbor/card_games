package bboreel.games.card.domain

import bboreel.games.card.BaseTest
import org.junit.Assert.{assertEquals, assertTrue}

/**
  * @author Robb Lee (robbmlee@gmail.com).
  */
class FaceValueTest extends BaseTest {
  private final val TestIdentifier = "FaceValue Tests"

  test(s"$TestIdentifier: should be ordered by ordinal number.") {
    val faceValue = "foo"
    val numberOfCards = 10
    val values = Range(numberOfCards, 0, -1).map(FaceValue(_, faceValue))

    for {ordinal <- 1 until numberOfCards} {
      assertTrue(values(ordinal - 1) > values(ordinal))
    }

    val sortedValues = values.sorted
    for {ordinal <- 1 until numberOfCards} {
      assertTrue(sortedValues(ordinal) > sortedValues(ordinal - 1))
    }
  }

  test(s"$TestIdentifier: should be ordered by ordinal number, then by value.") {
    val valueOne = FaceValue(0, "foo")
    val valueTwo = FaceValue(0, "man")
    val valueThree = FaceValue(1, "zed")
    val valueFour = FaceValue(1, "chu")

    val unsorted = List(valueThree, valueTwo, valueFour, valueOne)
    assertEquals(valueThree, unsorted(0))
    assertEquals(valueTwo, unsorted(1))
    assertEquals(valueFour, unsorted(2))
    assertEquals(valueOne, unsorted(3))

    val sorted = unsorted.sorted
    assertEquals(valueOne, sorted(0))
    assertEquals(valueTwo, sorted(1))
    assertEquals(valueFour, sorted(2))
    assertEquals(valueThree, sorted(3))
  }
}
