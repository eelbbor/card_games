package org.eelbbor.pinochle.exceptions;

import org.eelbbor.pinochle.BidManager;

/**
 * Checked exception for tracking issues associated with bidding. Implements several factory
 * methods for creating a consistent messaging structure.
 *
 * @author Robb Lee (robbmlee@gmail.com).
 */
public class InvalidBiddingException extends RuntimeException {
  private InvalidBiddingException(String message) {
    super(message);
  }

  /**
   * Conveys an issue when trying to bid with an invalid minimum value. Note: This exception is
   * meant for values below 60 not requiring an increment of 5. If the value must be greater than
   * 60 use {@link InvalidBiddingException#invalidMinimumBidIncrementByFive(int, int)}.
   *
   * @param bid value sent for a bid.
   * @param minBid minimum valid value the bid must be.
   * @return exception object indicating the error.
   */
  public static InvalidBiddingException invalidMinimumBid(int bid, int minBid) {
    return new InvalidBiddingException(
        "Invalid bid value of '" + bid + "', must be at least '" + minBid + "'.");
  }

  /**
   * Conveys an issue when trying to bid with an invalid minimum value when the score requires
   * an increment of 5 for the bid, also use to convey issue if the value is greater than the
   * minimum bid but not a multiple of 5.
   *
   * @param bid value sent for a bid.
   * @param minBid minimum valid value the bid must be.
   * @return exception object indicating the error.
   */
  public static InvalidBiddingException invalidMinimumBidIncrementByFive(int bid, int minBid) {
    return new InvalidBiddingException("Invalid bid value of '" + bid + "', must exceed '"
        + minBid + "' and be an increment of 5 when greater than "
        + BidManager.MINIMUM_INCREMENT_BY_FIVE_BID + " (i.e. 65 or 70).");
  }

  /**
   * Generates the exception object conveying an attempt at bidding after passing.
   *
   * @return exception object indicating the error.
   */
  public static InvalidBiddingException bidAfterPassing(int playerIndex) {
    return new InvalidBiddingException("Player " + playerIndex
        + " has already passed and can no longer bid.");
  }

  /**
   * Generates the exception object conveying an attempt at passing for the highest bid.
   *
   * @return exception object indicating the error.
   */
  public static InvalidBiddingException highestBidPassing(int playerIndex) {
    return new InvalidBiddingException("Player " + playerIndex
        + " has the highest bid and cannot pass until outbid.");
  }

  /**
   * Generates the exception object conveying an attempt at passing by the dealer without another
   * bid.
   *
   * @return exception object indicating the error.
   */
  public static InvalidBiddingException dealerPassing(int playerIndex) {
    return new InvalidBiddingException("Player " + playerIndex
        + " is the dealer and cannot pass until a higher bid is made.");
  }

  /**
   * Generates the exception object conveying an attempt at a bidding operation after bidding is
   * closed.
   *
   * @return exception object indicating the error.
   */
  public static InvalidBiddingException biddingCompleted() {
    return new InvalidBiddingException("Trying to execute bid after bidding is closed.");
  }

  /**
   * Generates the exception object conveying an attempt at a playing a card when the bidding
   * phase has not been completed.
   *
   * @return exception object indicating the error.
   */
  public static InvalidBiddingException biddingInProgress() {
    return new InvalidBiddingException("Trying to play cards before bidding is complete.");
  }

  /**
   * Generates the exception object conveying an attempt at declaring trump prior to a player
   * winning the bid.
   *
   * @return exception object indicating the error.
   */
  public static InvalidBiddingException trumpDeclarationWhileBidding() {
    return new InvalidBiddingException("Trying to declare trump before the bid has been won.");
  }

  /**
   * Generates the exception object conveying an attempt at declaring trump after play has
   * started for the hand.
   *
   * @return exception object indicating the error.
   */
  public static InvalidBiddingException trumpDeclarationAfterBiddingComplete() {
    return new InvalidBiddingException("Trying to declare trump after it was already declared.");
  }
}
