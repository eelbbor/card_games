package bboreel.games.card.game

import bboreel.games.card.BaseTest

/**
  * @author Robb Lee (robb@uber.com).
  */
class HandTest extends BaseTest {
  private final val TestIdentifier = "Hand Tests"

  test(s"$TestIdentifier: should add card.") {
    val hand: Hand = new Hand()
    hand.dealCard(new Card())
  }

  test(s"$TestIdentifier: should allow multiple card definitions by reference.") {
    fail("Implement this test.")
  }

  test(s"$TestIdentifier: should allow multiple card definitions by value.") {
    fail("Implement this test.")
  }

  test(s"$TestIdentifier: should remove card definitions by reference.") {
    fail("Implement this test.")
  }

  test(s"$TestIdentifier: should remove card definitions by value.") {
    fail("Implement this test.")
  }
}
