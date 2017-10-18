package bboreel.games.card.game

import bboreel.games.card.BaseTest
import bboreel.games.card.TestUtils.{MaxCardValue, randomPositiveInteger, randomSuite}
import bboreel.games.card.domain.{Card, FaceValue, Suite}
import org.junit.Assert.{assertEquals, assertFalse, assertTrue}

/**
  * @author Robb Lee (robbmlee@gmail.com).
  */
class HandTest extends BaseTest {
  private final val TestIdentifier = "Hand Tests"
  private var hand: Hand = _

  override protected def beforeEach(): Unit = {
    super.beforeEach()
    this.hand = new Hand()
    assertTrue(hand.isEmpty())
    assertEquals(0, hand.size())
  }

  test(s"$TestIdentifier: should add card.") {
    val card: Card = createRandomCard()
    hand.dealCard(card)
    assertEquals(1, hand.size())
    assertTrue(hand.contains(card))

    val cardCopy = new Card(card.suite, card.value)
    assertTrue(hand.contains(cardCopy))
  }

  test(s"$TestIdentifier: should allow multiple card definitions by reference.") {
    val card: Card = createRandomCard()
    val minCardCount = 5
    val maxCardCout = 100
    val numCards = randomPositiveInteger(max = maxCardCout, min = minCardCount)
    for (_ <- 1 to numCards) {
      hand.dealCard(card)
      hand.dealCard(createRandomDifferentCard(card))
    }

    assertEquals(2 * numCards, hand.size())
    assertTrue(hand.contains(card))
    assertFalse(hand.isEmpty())
  }

  test(s"$TestIdentifier: should allow multiple card definitions by value.") {
    val card: Card = createRandomCard()
    val minCardCount = 5
    val maxCardCout = 100
    val numCards = randomPositiveInteger(max = maxCardCout, min = minCardCount)
    for (_ <- 1 to numCards) {
      val copyOfCard = new Card(card.suite, card.value)
      hand.dealCard(copyOfCard)
      hand.dealCard(createRandomDifferentCard(card))
      assertTrue(hand.contains(card))
      assertTrue(hand.contains(copyOfCard))
    }
    assertEquals(2 * numCards, hand.size())
    assertFalse(hand.isEmpty())
  }

  test(s"$TestIdentifier: should remove card definitions by reference.") {
    val card: Card = createRandomCard()
    val minCardCount = 5
    val maxCardCout = 100
    val numCards = randomPositiveInteger(max = maxCardCout, min = minCardCount)
    for (_ <- 1 to numCards) {
      hand.dealCard(card)
      hand.dealCard(createRandomDifferentCard(card))
    }
    assertEquals(0, hand.size() % 2)
    assertEquals(2 * numCards, hand.size())

    for (_ <- 1 to hand.size() / 2) {
      assertTrue(hand.contains(card))
      hand.playCard(card)
    }
    assertEquals(numCards, hand.size())
    assertFalse(hand.contains(card))
    assertFalse(hand.isEmpty())
  }

  test(s"$TestIdentifier: should remove card definitions by value.") {
    val card: Card = createRandomCard()
    val minCardCount = 5
    val maxCardCout = 100
    val numCards = randomPositiveInteger(max = maxCardCout, min = minCardCount)
    for (_ <- 1 to numCards) {
      val copyOfCard = new Card(card.suite, card.value)
      hand.dealCard(copyOfCard)
      hand.dealCard(createRandomDifferentCard(card))
      assertTrue(hand.contains(card))
      assertTrue(hand.contains(copyOfCard))
    }
    assertEquals(2 * numCards, hand.size())

    val halfSize = hand.size() / 2
    for (_ <- 1 to halfSize) {
      assertTrue(hand.contains(card))
      assertTrue(hand.playCard(card).equals(card))
    }
    assertEquals(numCards, hand.size())
    assertFalse(hand.contains(card))
    assertFalse(hand.isEmpty())
  }

  test(s"$TestIdentifier: should throw exception for empty hand.") {
    val card: Card = createRandomCard()
    assertFalse(hand.contains(card))
    try {
      hand.playCard(card)
      fail("Should have thrown exception for missing card.")
    } catch {
      case ex: RuntimeException => {
        assertEquals(s"Tried to remove invalid card: $card", ex.getMessage())
      }
    }
  }

  test(s"$TestIdentifier: should throw exception for invalid card.") {
    val maxNumberOfCards = 10
    val minNumberOfCards = 2
    val randomCardToRemove = createRandomCard()
    val numCards = randomPositiveInteger(max = maxNumberOfCards, min = minNumberOfCards)
    for (_ <- 1 to numCards) {
      hand.dealCard(createRandomDifferentCard(randomCardToRemove))
    }
    assertEquals(numCards, hand.size())
    assertFalse(hand.contains(randomCardToRemove))
    try {
      hand.playCard(randomCardToRemove)
      fail("Should have thrown exception for missing card.")
    } catch {
      case ex: RuntimeException => {
        assertEquals(s"Tried to remove invalid card: $randomCardToRemove", ex.getMessage())
      }
    }
  }

  private def createRandomCard(): Card = {
    val cardValue = randomPositiveInteger(MaxCardValue)
    val suite: Suite.Value = randomSuite()
    Card(suite, new FaceValue(cardValue, cardValue.toString))
  }

  private def createRandomDifferentCard(cardToAvoid: Card): Card = {
    var card: Card = createRandomCard()
    while (card.equals(cardToAvoid)) {
      card = createRandomCard()
    }
    card
  }
}
