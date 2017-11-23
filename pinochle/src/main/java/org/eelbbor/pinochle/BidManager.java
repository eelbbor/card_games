package org.eelbbor.pinochle;

import org.eelbbor.carddeck.standard.Suite;
import org.eelbbor.pinochle.exceptions.InvalidBiddingException;

import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Tracks the state of bidding for starting a hand.
 *
 * @author Robb Lee (robbmlee@gmail.com).
 */
public class BidManager {
  public static final int MINIMUM_BID = 50;
  public static final int MINIMUM_INCREMENT_BY_FIVE_BID = 60;

  private static final int PASSING_BID = -1;

  private boolean bidsComplete;
  private Suite trump;
  private int dealerIndex;
  private int highBidIndex;
  private int[] bids;

  /**
   * Constructor for managing the bid keeping track of the dealer.
   *
   * @param dealerIndex index for the player dealing.
   */
  public BidManager(int dealerIndex) {
    this.dealerIndex = dealerIndex;
    bidsComplete = false;
    trump = null;
    highBidIndex = -1;
    bids = new int[] {0, 0, 0, 0};
  }

  public int getBid() {
    return highBidIndex < 0 ? 0 : bids[highBidIndex];
  }

  public int getDealerIndex() {
    return dealerIndex;
  }

  public int getHighBidPlayerIndex() {
    return highBidIndex;
  }

  public Optional<Suite> getTrump() {
    return Optional.ofNullable(trump);
  }

  /**
   * Indicates if a player has won the bid and trump has been declared.
   *
   * @return true if bidding is done and trump declared else false.
   */
  public boolean isComplete() {
    return trump != null;
  }

  /**
   * Indicates if players are still bidding.
   *
   * @return true if still bidding else false.
   */
  public boolean isBidding() {
    return !bidsComplete;
  }

  /**
   * Indicates if bidding is complete but trump has not been declared.
   *
   * @return true if a player has won the bid but not declared trump else false.
   */
  public boolean isDeclaringTrump() {
    return !isBidding() && !isComplete();
  }

  /**
   * Indicates if the player at the provided index has passed.
   *
   * @param playerIndex index of player to check.
   * @return true if player already passed else false.
   */
  public boolean playerPassed(int playerIndex) {
    return bids[playerIndex] == PASSING_BID;
  }

  /**
   * Casts bid for the player index. Throws {@link InvalidBiddingException} if the bidding is
   * closed or the bid does not meet the minimum bid or if the player has already passed.
   *
   * @param playerIndex index of the bidding player.
   * @param bidValue    value of bid value.
   * @throws InvalidBiddingException if the bid is too low or if bidding has already concluded.
   */
  public void bid(int playerIndex, int bidValue) throws InvalidBiddingException {
    if (!isBidding()) {
      throw InvalidBiddingException.biddingCompleted();
    } else if (playerPassed(playerIndex)) {
      throw InvalidBiddingException.bidAfterPassing(playerIndex);
    }

    int minBid = getMinBid();
    if (bidValue < minBid) {
      if (minBid > MINIMUM_INCREMENT_BY_FIVE_BID) {
        throw InvalidBiddingException.invalidMinimumBidIncrementByFive(bidValue, minBid);
      } else {
        throw InvalidBiddingException.invalidMinimumBid(bidValue, minBid);
      }
    } else if (bidValue > MINIMUM_INCREMENT_BY_FIVE_BID && bidValue % 5 != 0) {
      throw InvalidBiddingException.invalidMinimumBidIncrementByFive(bidValue, minBid);
    }

    // Set the new min bid and update bid values.
    bids[playerIndex] = bidValue;
    highBidIndex = playerIndex;
  }

  /**
   * Passes for the current player during the bidding process. If the indicated player has
   * already passed there is no effect. If the bid is too low or an attempt is made at passing
   * for last player still bidding an exception is thrown.
   *
   * @param playerIndex index of the passing player.
   * @throws InvalidBiddingException for any issues with the bid value or invalid bidding condition.
   */
  public void pass(int playerIndex) throws InvalidBiddingException {
    if (playerPassed(playerIndex)) {
      return;
    }

    // Defend against trying to pass with the highest bidder.
    if (playerIndex == highBidIndex) {
      throw InvalidBiddingException.highestBidPassing(playerIndex);
    }

    // Defend against trying to pass for the dealer without any other bids.
    if (playerIndex == dealerIndex && highBidIndex < 0) {
      throw InvalidBiddingException.dealerPassing(playerIndex);
    }

    bids[playerIndex] = PASSING_BID;
    bidsComplete = IntStream.range(0, 4).map(i -> bids[i] == PASSING_BID ? 0 : 1).sum() == 1;

    // If all players have passed then stick the dealer with the bid if no bid was made.
    if (bidsComplete && highBidIndex < 0) {
      highBidIndex = dealerIndex;
      bids[dealerIndex] = MINIMUM_BID;
    }
  }

  /**
   * Sets the trump suite for the upcoming hand. Throws {@link InvalidBiddingException} if the
   * bidding is not complete or if bidding is closed.
   *
   * @param trump suite to be trump for the hand.
   * @throws InvalidBiddingException if declaring trump is invalid.
   */
  public void declareTrump(Suite trump) throws InvalidBiddingException {
    if (isComplete()) {
      throw InvalidBiddingException.trumpDeclarationAfterBiddingComplete();
    } else if (!isDeclaringTrump()) {
      throw InvalidBiddingException.trumpDeclarationWhileBidding();
    }

    this.trump = trump;
  }

  private int getMinBid() {
    int currentBid = this.getBid();
    int minBid = MINIMUM_BID;
    if (currentBid >= MINIMUM_BID) {
      minBid = currentBid + (currentBid >= MINIMUM_INCREMENT_BY_FIVE_BID ? 5 : 1);
    }
    return minBid;
  }
}
