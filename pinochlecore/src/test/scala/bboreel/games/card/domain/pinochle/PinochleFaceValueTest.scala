package bboreel.games.card.domain.pinochle

import bboreel.games.card.BaseTest
import org.junit.Assert.{assertEquals}

/**
  * @author Robb Lee (robbmlee@gmail.com).
  */
class PinochleFaceValueTest extends BaseTest {
  private final val TestIdentifier = "PinochleFaceValue Tests"

  test(s"$TestIdentifier: should have valid ordinal values.") {
    val expectedValidValueCount = 5
    assertEquals(expectedValidValueCount, PinochleFaceValue.values.size)
    assertEquals(0, PinochleFaceValue.Jack.id)
    assertEquals(PinochleFaceValue.Jack.id + 1, PinochleFaceValue.Queen.id)
    assertEquals(PinochleFaceValue.Queen.id + 1, PinochleFaceValue.King.id)
    assertEquals(PinochleFaceValue.King.id + 1, PinochleFaceValue.Ten.id)
    assertEquals(PinochleFaceValue.Ten.id + 1, PinochleFaceValue.Ace.id)
  }
}
