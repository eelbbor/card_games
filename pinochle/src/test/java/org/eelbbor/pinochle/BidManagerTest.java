package org.eelbbor.pinochle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.eelbbor.carddeck.standard.Suite;
import org.eelbbor.pinochle.exceptions.InvalidBiddingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

class BidManagerTest {
  private BidManager manager;
  private int dealerIndex;

  @BeforeEach
  void setUp() {
    dealerIndex = TestUtils.randomInteger(4);
    manager = new BidManager(dealerIndex);
  }

  @Test
  void shouldInitializeStateAtConstruction() {
    assertEquals(dealerIndex, manager.getDealerIndex());
    assertEquals(0, manager.getBid());
    assertEquals(-1, manager.getHighBidPlayerIndex());
    assertFalse(manager.getTrump().isPresent());
    assertFalse(manager.isComplete());
    assertFalse(manager.isDeclaringTrump());
    assertTrue(manager.isBidding());
    IntStream.range(0, 4).forEach(i -> assertFalse(manager.playerPassed(i)));
  }

  @Test
  void shouldCloseBiddingAtMinimumIfFirstThreePlayersPass() throws Exception {
    assertEquals(dealerIndex, manager.getDealerIndex());
    for (int player = 0; player < 4; player++) {
      if (player != dealerIndex) {
        manager.pass(player);
      }
    }

    assertEquals(dealerIndex, manager.getHighBidPlayerIndex());
    assertFalse(manager.isBidding());
    assertTrue(manager.isDeclaringTrump());
    assertFalse(manager.isComplete());

    assertEquals(BidManager.MINIMUM_BID, manager.getBid());
    assertFalse(manager.getTrump().isPresent());

    Suite trump = TestUtils.randomEnum(Suite.class);
    manager.declareTrump(trump);

    assertFalse(manager.isBidding());
    assertFalse(manager.isDeclaringTrump());
    assertTrue(manager.isComplete());
  }

  @Test
  void shouldAllowDealerToPassIfAnotherPlayerBid() throws Exception {
    int expectedBid = TestUtils.randomInteger(11) + BidManager.MINIMUM_BID;
    int biddingPlayer;
    do {
      biddingPlayer = TestUtils.randomInteger(4);
    } while (biddingPlayer == dealerIndex);

    manager.bid(biddingPlayer, expectedBid);
    manager.pass(dealerIndex);
    assertTrue(manager.playerPassed(dealerIndex));
  }

  @Test
  void shouldAllowBidToIncreaseAfterFirstRoundOfBidding() throws Exception {
    int bidValue = BidManager.MINIMUM_BID;
    dealerIndex = 3;
    manager = new BidManager(dealerIndex);
    for (int playerIndex = 0; playerIndex < 4; playerIndex++) {
      bidValue++;
      manager.bid(playerIndex, bidValue);
      assertEquals(playerIndex, manager.getHighBidPlayerIndex());
      assertEquals(bidValue, manager.getBid());
    }

    assertEquals(dealerIndex, manager.getHighBidPlayerIndex());
    assertEquals(BidManager.MINIMUM_BID + 4, manager.getBid());

    for (int playerIndex = 0; playerIndex < 4; playerIndex++) {
      if (playerIndex % 2 != 0) {
        bidValue++;
        manager.bid(playerIndex, bidValue);
        assertEquals(playerIndex, manager.getHighBidPlayerIndex());
        assertEquals(bidValue, manager.getBid());
      } else {
        manager.pass(playerIndex);
        assertTrue(manager.playerPassed(playerIndex));
      }
    }

    assertEquals(dealerIndex, manager.getHighBidPlayerIndex());
    assertEquals(BidManager.MINIMUM_BID + 6, manager.getBid());

    manager.bid(1, ++bidValue);
    manager.pass(dealerIndex);

    assertFalse(manager.isBidding());
    assertTrue(manager.isDeclaringTrump());
    assertEquals(1, manager.getHighBidPlayerIndex());
    assertEquals(BidManager.MINIMUM_BID + 7, manager.getBid());
  }

  @Test
  void shouldThrowExceptionIfAttemptToMakeInvalidMinimumBid() {
    int invalidBidValue = 49;
    assertTrue(manager.getBid() < BidManager.MINIMUM_BID);
    validateMinBidExceptionIncrementByOne(invalidBidValue, BidManager.MINIMUM_BID);
  }

  @Test
  void shouldThrowExceptionIfAttemptToMakeIdenticalBid() throws Exception {
    int currentBid = TestUtils.randomInteger(8) + BidManager.MINIMUM_BID + 1;
    assertTrue(currentBid > BidManager.MINIMUM_BID
        && currentBid < BidManager.MINIMUM_INCREMENT_BY_FIVE_BID);

    int playerOneIndex = TestUtils.randomInteger(4);
    manager.bid(playerOneIndex, currentBid);
    assertEquals(currentBid, manager.getBid());
    assertEquals(playerOneIndex, manager.getHighBidPlayerIndex());

    validateMinBidExceptionIncrementByOne(currentBid, currentBid + 1);
  }

  @Test
  void shouldThrowExceptionIfAttemptToMakeBidTooLow() throws Exception {
    int currentBid = TestUtils.randomInteger(8) + BidManager.MINIMUM_BID + 1;
    assertTrue(currentBid > BidManager.MINIMUM_BID
        && currentBid < BidManager.MINIMUM_INCREMENT_BY_FIVE_BID);

    int playerOneIndex = TestUtils.randomInteger(4);
    manager.bid(playerOneIndex, currentBid);
    assertEquals(currentBid, manager.getBid());
    assertEquals(playerOneIndex, manager.getHighBidPlayerIndex());

    validateMinBidExceptionIncrementByOne(currentBid - 1, currentBid + 1);
  }

  @Test
  void shouldThrowExceptionIfAttemptToMakeBidTooLowAtExactlyIncrementByFive() throws Exception {
    int currentBid = BidManager.MINIMUM_INCREMENT_BY_FIVE_BID;
    int playerOneIndex = TestUtils.randomInteger(4);
    manager.bid(playerOneIndex, currentBid);
    assertEquals(currentBid, manager.getBid());
    assertEquals(playerOneIndex, manager.getHighBidPlayerIndex());

    validateMinBidExceptionIncrementByFive(currentBid - 3, currentBid + 5);
  }

  @Test
  void shouldThrowExceptionIfAttemptToMakeIdenticalBidInIncrementByFive() throws Exception {
    int currentBid = BidManager.MINIMUM_INCREMENT_BY_FIVE_BID;
    int playerOneIndex = TestUtils.randomInteger(4);
    manager.bid(playerOneIndex, currentBid);
    assertEquals(currentBid, manager.getBid());
    assertEquals(playerOneIndex, manager.getHighBidPlayerIndex());

    validateMinBidExceptionIncrementByFive(currentBid, currentBid + 5);
  }

  @Test
  void shouldThrowExceptionIfAttemptToBidLessThanFiveOverAfterSixty() throws Exception {
    int currentBid = BidManager.MINIMUM_INCREMENT_BY_FIVE_BID + 5;
    int playerOneIndex = TestUtils.randomInteger(4);
    manager.bid(playerOneIndex, currentBid);
    assertEquals(currentBid, manager.getBid());
    assertEquals(playerOneIndex, manager.getHighBidPlayerIndex());

    validateMinBidExceptionIncrementByFive(currentBid + 1, currentBid + 5);
  }

  @Test
  void shouldThrowExceptionIfAttemptToMakeBidAfterBiddingClosed() throws Exception {
    fastCompleteBidding(null);
    try {
      manager.bid(dealerIndex, manager.getBid() + 5);
      fail("Should have thrown exception for trying to bid after bidding completed.");
    } catch (InvalidBiddingException ex) {
      assertEquals(InvalidBiddingException.biddingCompleted().getMessage(), ex.getMessage());
    }
  }

  @Test
  void shouldThrowExceptionIfAttemptingToBidAfterPassing() throws Exception {
    int playerIndex;
    do {
      playerIndex = TestUtils.randomInteger(4);
    } while (playerIndex == dealerIndex);
    manager.pass(playerIndex);
    assertTrue(manager.playerPassed(playerIndex));

    try {
      manager.bid(playerIndex, BidManager.MINIMUM_INCREMENT_BY_FIVE_BID);
      fail("Should have thrown exception trying to bid after passing.");
    } catch (InvalidBiddingException ex) {
      assertEquals(InvalidBiddingException.bidAfterPassing(playerIndex).getMessage(),
          ex.getMessage());
    }
  }

  @Test
  void shouldThrowExceptionForBidWithInvalidFiveIncrementWhenPlusOneStillValid() {
    int bidValue = BidManager.MINIMUM_INCREMENT_BY_FIVE_BID + TestUtils.randomInteger(3) + 1;
    try {
      manager.bid(dealerIndex, bidValue);
      fail("Should have thrown exception trying to bid after passing.");
    } catch (InvalidBiddingException ex) {
      assertEquals(InvalidBiddingException.invalidMinimumBidIncrementByFive(
          bidValue, BidManager.MINIMUM_BID).getMessage(),
          ex.getMessage());
    }
  }

  @Test
  void shouldIgnoreRepeatedPassingForSamePlayer() throws Exception {
    manager.bid(dealerIndex, BidManager.MINIMUM_BID + TestUtils.randomInteger(10));
    int playerIndex;
    do {
      playerIndex = TestUtils.randomInteger(4);
    } while (playerIndex == dealerIndex);

    assertFalse(manager.playerPassed(playerIndex));
    for (int i = 0; i < 5; i++) {
      manager.pass(playerIndex);
      assertTrue(manager.playerPassed(playerIndex));
    }
    assertTrue(manager.playerPassed(playerIndex));
  }

  @Test
  void shouldThrowExceptionOnHighBidderAttemptToPass() throws Exception {
    int playerIndex = TestUtils.randomInteger(4);
    manager.bid(playerIndex, BidManager.MINIMUM_INCREMENT_BY_FIVE_BID);
    assertEquals(playerIndex, manager.getHighBidPlayerIndex());
    assertEquals(BidManager.MINIMUM_INCREMENT_BY_FIVE_BID, manager.getBid());
    try {
      manager.pass(playerIndex);
      fail("Should have thrown exception when high bidder tried to pass.");
    } catch (InvalidBiddingException ex) {
      assertEquals(InvalidBiddingException.highestBidPassing(playerIndex).getMessage(),
          ex.getMessage());
    }
  }

  @Test
  void shouldThrowExceptionOnDealerAttemptToPassWithoutAnotherBidder() throws Exception {
    try {
      manager.pass(dealerIndex);
      fail("Should have thrown exception for trying to pass without another bid.");
    } catch (InvalidBiddingException ex) {
      assertEquals(InvalidBiddingException.dealerPassing(dealerIndex).getMessage(),
          ex.getMessage());
    }
  }

  @Test
  void shouldThrowExceptionIfAttemptToDeclareTrumpBeforeBidIsWon() {
    assertTrue(manager.isBidding());
    try {
      manager.declareTrump(TestUtils.randomEnum(Suite.class));
      fail("Should have thrown exception for declaring trump during bidding.");
    } catch (InvalidBiddingException ex) {
      assertEquals(InvalidBiddingException.trumpDeclarationWhileBidding().getMessage(),
          ex.getMessage());
    }
  }

  @Test
  void shouldThrowExceptionIfAttemptToDeclareTrumpAfterBiddingIsComplete() throws Exception {
    fastCompleteBidding(TestUtils.randomEnum(Suite.class));
    assertFalse(manager.isBidding());
    assertFalse(manager.isDeclaringTrump());
    assertTrue(manager.isComplete());
    try {
      manager.declareTrump(TestUtils.randomEnum(Suite.class));
      fail("Should have thrown exception for declaring trump after bidding and declaring.");
    } catch (InvalidBiddingException ex) {
      assertEquals(InvalidBiddingException.trumpDeclarationAfterBiddingComplete().getMessage(),
          ex.getMessage());
    }
  }

  private void validateMinBidExceptionIncrementByFive(int bidValue, int minBid) {
    try {
      manager.bid(TestUtils.randomInteger(4), bidValue);
      fail("Should have thrown exception due to invalid bid with increment by five.");
    } catch (InvalidBiddingException ex) {
      assertEquals(
          InvalidBiddingException.invalidMinimumBidIncrementByFive(bidValue, minBid).getMessage(),
          ex.getMessage());
    }
  }

  private void validateMinBidExceptionIncrementByOne(int bidValue, int minBid) {
    try {
      manager.bid(TestUtils.randomInteger(4), bidValue);
      fail("Should have thrown exception due to invalid bid with increment by one.");
    } catch (InvalidBiddingException ex) {
      assertEquals(InvalidBiddingException.invalidMinimumBid(bidValue, minBid).getMessage(),
          ex.getMessage());
    }
  }

  private void fastCompleteBidding(Suite trump) throws Exception {
    for (int index = 0; index < 4; index++) {
      if (index != dealerIndex) {
        manager.pass(index);
      }
    }

    if (trump != null) {
      manager.declareTrump(trump);
      assertEquals(trump, manager.getTrump().get());
    } else {
      assertFalse(manager.getTrump().isPresent());
    }
  }
}