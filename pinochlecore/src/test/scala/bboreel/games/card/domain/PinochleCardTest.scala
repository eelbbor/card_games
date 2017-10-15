package bboreel.games.card.domain

import bboreel.games.card.BaseTest
import bboreel.games.card.domain.pinochle.{PinochleCard, PinochleFaceValue}
import org.junit.Assert.{assertEquals}

/**
  * @author Robb Lee (robbmlee@gmail.com).
  */
class PinochleCardTest extends BaseTest {
  private final val TestIdentifier = "PinochleCard Tests"

  test(s"$TestIdentifier: should sort by card ordinal value.") {
    val suite = Suite.Clubs
    val jack = new PinochleCard(suite, PinochleFaceValue.Jack)
    val queen = new PinochleCard(suite, PinochleFaceValue.Queen)
    val king = new PinochleCard(suite, PinochleFaceValue.King)
    val ten = new PinochleCard(suite, PinochleFaceValue.Ten)
    val ace = new PinochleCard(suite, PinochleFaceValue.Ace)

    val unsorted = List(ace, ten, king, queen, jack)
    assertEquals(ace, unsorted.head)
    assertEquals(ten, unsorted(1))
    assertEquals(king, unsorted(2))
    assertEquals(queen, unsorted(3))
    assertEquals(jack, unsorted.last)

    val sorted = unsorted.sorted(Ordering[Card])
    assertEquals(jack, sorted.head)
    assertEquals(queen, sorted(1))
    assertEquals(king, sorted(2))
    assertEquals(ten, sorted(3))
    assertEquals(ace, sorted.last)
  }
}
