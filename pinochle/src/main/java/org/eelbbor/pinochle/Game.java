package org.eelbbor.pinochle;

import org.eelbbor.carddeck.Deck;
import org.eelbbor.carddeck.standard.Suite;
import org.eelbbor.pinochle.exceptions.InvalidBiddingException;
import org.eelbbor.pinochle.exceptions.InvalidCardException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Controls the flow of a pinochle game. The players are indicated by an index number ranging
 * from 0 to 3 where players 0 and 2 are team one and players 1 and 3 are team two.
 *
 * @author Robb Lee (robbmlee@gmail.com).
 */
public class Game {
  private static final List<List<Integer>> TEAM_INDICES = List.of(List.of(0, 2), List.of(1, 3));

  public static final int MINIMUM_MELD = 20;
  public static final int MINIMUM_TRICKS = 20;
  public static final int TEAM_ONE_INDEX = 0;
  public static final int TEAM_TWO_INDEX = 1;

  private Deck<Card> deck;
  private int currentPlayer;
  private Hand[] hands;
  private int[] score;

  private int currentLead;
  private HandState currentState;
  private Trick currentTrick;

  /**
   * Default constructor initializes a game and sets up the first hand by dealing to each of the
   * four players.
   */
  public Game() {
    deck = new Deck<>(Arrays.stream(Suite.values()).flatMap(suite ->
        Arrays.stream(PinochleFaceValue.values()).flatMap(val -> {
          Card card = new Card(suite, val);
          return Stream.of(card, card, card, card);
        })).collect(Collectors.toList()));
    currentPlayer = 0;
    hands = new Hand[] {new Hand(), new Hand(), new Hand(), new Hand()};
    score = new int[] {0, 0};
    startHand();
  }

  public int getDealer() {
    return currentState.bidManager.getDealerIndex();
  }

  public int getCurrentPlayer() {
    return currentPlayer;
  }

  public int getCurrentLead() {
    return currentLead;
  }

  public int getTeamScore(int teamIndex) {
    return score[teamIndex];
  }

  public int getTeamMeld(int teamIndex) {
    return currentState.meld[teamIndex];
  }

  public boolean isPlayingHand() {
    return currentState.bidManager.isComplete();
  }

  public boolean isBidding() {
    return currentState.bidManager.isBidding();
  }

  public boolean isDeclaringTrump() {
    return currentState.bidManager.isDeclaringTrump();
  }

  public int getCurrentBid() {
    return currentState.bidManager.getBid();
  }

  public Optional<Suite> getTrumpSuite() {
    return currentState.bidManager.getTrump();
  }

  public List<Card> getPlayersRemainingCards(int player) {
    return hands[player].remainingCards();
  }

  /**
   * Passes for the current player during the bidding process. Advances the play to the next
   * player and returns the new current player index. If the player is the last to pass the
   * game is advanced to setting trump, see {@link Game#isDeclaringTrump()} for checking status.
   *
   * @return new current player index.
   * @throws InvalidBiddingException if the bid is too low or if bidding has already concluded.
   */
  public int pass() throws InvalidBiddingException {
    BidManager bidManager = currentState.bidManager;
    bidManager.pass(getCurrentPlayer());

    if (bidManager.isDeclaringTrump()) {
      // Bidding is completed, set values in preparation for declaring trump.
      currentPlayer = bidManager.getHighBidPlayerIndex();
      currentLead = currentPlayer;
    } else {
      // Advance to next player for bid.
      while (bidManager.playerPassed(getCurrentPlayer())) {
        currentPlayer = advancePlayer(getCurrentPlayer());
      }
    }

    /*if (!isBidding()) {
      throw InvalidBiddingException.biddingCompleted();
    }

    currentState.passed[getCurrentPlayer()] = true;

    // Advance to next player for bid.
    while (currentState.passed[getCurrentPlayer()]) {
      currentPlayer = advancePlayer(getCurrentPlayer());
    }

    // If last player to pass, then advance the bidding to accept declaring trump.
    int stillBidding = IntStream.range(0, 4).map(i -> currentState.passed[i] ? 0 : 1).sum();
    if (stillBidding == 1) {
      currentState.winningPlayerIndex = getCurrentPlayer();
      currentLead = getCurrentPlayer();
      currentState.bid = getCurrentBid() < MINIMUM_BID ? MINIMUM_BID : getCurrentBid();
    }*/

    return getCurrentPlayer();
  }

  /**
   * Casts bid for the current player. Throws {@link InvalidBiddingException} if bid does not
   * exceed the required bid. Advances the play to the next player and returns the new current
   * player index.
   *
   * @param bidValue value of bid value.
   * @return new current player index.
   * @throws InvalidBiddingException if the bid is too low or if bidding has already concluded.
   */
  public int bid(int bidValue) throws InvalidBiddingException {
    BidManager bidManager = currentState.bidManager;
    bidManager.bid(getCurrentPlayer(), bidValue);
    // Advance to next player for bid.
    do {
      currentPlayer = advancePlayer(getCurrentPlayer());
    } while (bidManager.playerPassed(getCurrentPlayer()));
    /* if (!isBidding()) {
      throw InvalidBiddingException.biddingCompleted();
    }

    int minBid = getMinBid();
    if (bidValue < MINIMUM_BID) {
      throw InvalidBiddingException.invalidMinimumBid(bidValue, minBid);
    } else if (bidValue < minBid) {
      if (minBid > MINIMUM_INCREMENT_BY_FIVE_BID) {
        throw InvalidBiddingException.invalidMinimumBidIncrementByFive(bidValue, minBid);
      } else {
        throw InvalidBiddingException.invalidMinimumBid(bidValue, minBid);
      }
    } else if (bidValue > MINIMUM_INCREMENT_BY_FIVE_BID && bidValue % 5 != 0) {
      throw InvalidBiddingException.invalidMinimumBidIncrementByFive(bidValue, minBid);
    }

    // Set the bid and finalize bidding.
    currentState.bid = bidValue;

    // Advance to next player for bid.
    do {
      currentPlayer = advancePlayer(getCurrentPlayer());
    } while (currentState.passed[getCurrentPlayer()]);*/
    return getCurrentPlayer();
  }

  /**
   * Sets the trump suite for the upcoming hand. Throws {@link InvalidBiddingException} if the
   * bidding is not complete or if bidding is closed. Note: If the team that took the build has
   * insufficient meld the hand is close immediately and the team takes a set.
   *
   * @param trump suite to be trump for the hand.
   * @return new current player index, which is the player that won the bid.
   * @throws InvalidBiddingException if there are issues with the bid.
   */
  public int declareTrump(Suite trump) throws InvalidBiddingException {
    BidManager bidManager = currentState.bidManager;
    bidManager.declareTrump(trump);
    currentState.meld[TEAM_ONE_INDEX] = computeTeamMeld(TEAM_ONE_INDEX);
    currentState.meld[TEAM_TWO_INDEX] = computeTeamMeld(TEAM_TWO_INDEX);

    // Validate sufficient meld for team that won the bid else end the hand with a set.
    int teamIndex = TEAM_INDICES.get(TEAM_ONE_INDEX).contains(currentPlayer)
        ? TEAM_ONE_INDEX : TEAM_TWO_INDEX;
    if (currentState.meld[teamIndex] < MINIMUM_MELD) {
      // Set the bidding team.
      // Other team keeps meld.
      // Start a new hand.
      throw new UnsupportedOperationException("Still need to address meld issue.");
    }

    currentTrick = new Trick(trump);

    /*if (isBidding()) {
      throw InvalidBiddingException.trumpDeclarationWhileBidding();
    } else if (!isDeclaringTrump()) {
      throw InvalidBiddingException.trumpDeclarationAfterBiddingComplete();
    }

    // Set the trump suite and record the meld for the hands.
    currentState.trump = trump;
    currentState.meld[TEAM_ONE_INDEX] = computeTeamMeld(TEAM_ONE_INDEX);
    currentState.meld[TEAM_TWO_INDEX] = computeTeamMeld(TEAM_TWO_INDEX);
    currentTrick = new Trick(trump);*/

    return getCurrentPlayer();
  }

  /**
   * Plays the designated card for the current player. Throws {@link InvalidCardException} if the
   * card is not a legal play of if the game is still in the bidding process. Advances the play to
   * the next player and returns the new current player index.
   *
   * @param card card to play on the current trick.
   * @return new current player index.
   * @throws InvalidCardException if there is an issue playing the card.
   * @throws InvalidBiddingException if the bidding is not complete.
   */
  public int playCard(Card card) throws InvalidCardException, InvalidBiddingException {
    if (isBidding()) {
      throw InvalidBiddingException.biddingInProgress();
    }

    // Play the card on the current trick.
    Hand currentPlayersHand = hands[getCurrentPlayer()];
    currentTrick.playCard(getCurrentPlayer(), card, currentPlayersHand.remainingCards());
    currentPlayersHand.playCard(card);
    currentPlayer = advancePlayer(getCurrentPlayer());

    // Check for last card of the trick.
    if (getCurrentPlayer() == getCurrentLead()) {
      // Collect trick and set currentPlayer and currentLead to player that took the trick.
      int lastTrickIndex = -1;
      // currentLead = ?;
      // currentPlayer = ?;
      score[TEAM_ONE_INDEX] += computeTeamHandTotal(TEAM_ONE_INDEX, lastTrickIndex);
      score[TEAM_ONE_INDEX] += computeTeamHandTotal(TEAM_TWO_INDEX, lastTrickIndex);
      // Complete hand and start next or finish game if last card.
      // Initialize a new trick.
      currentTrick = new Trick(currentState.bidManager.getTrump().get());
      throw new UnsupportedOperationException("Need to handle issue.");
    }
    return getCurrentPlayer();
  }

  /**
   * Returns a {@link List} of the cards played on the trick indexed by the player as
   * {@link Optional} in order to account for players yet to play.
   *
   * @return list of the cards played.
   */
  public List<Optional<Card>> currentTrick() {
    return currentTrick.getCardsPlayed();
  }

  private void startHand() {
    // Shuffle deck.
    deck.shuffle();

    // Deal to each player.
    int dealer = currentState == null ? 0 : advancePlayer(getDealer());
    currentState = new HandState(dealer);
    currentPlayer = advancePlayer(dealer);
    currentLead = dealer;
    currentTrick = null;

    while (deck.remainingCount() > 0) {
      IntStream.range(0, 4).forEach(i -> hands[getCurrentPlayer()].dealCard(deck.deal().get()));
      currentPlayer = advancePlayer(getCurrentPlayer());
    }
  }

  private int computeTeamMeld(int teamIndex) {
    int meld = TEAM_INDICES.get(teamIndex).stream().flatMapToInt(
        player -> IntStream.of(hands[player].countMeld(currentState.bidManager.getTrump().get())))
        .sum();
    return meld < MINIMUM_MELD ? 0 : meld;
  }

  private int computeTeamHandTotal(int teamIndex, int lastTrickIndex) {
    boolean wonLastTrick = lastTrickIndex == teamIndex;
    int tricks = currentState.tricks[teamIndex] + (wonLastTrick ? 2 : 0);
    int total = tricks < MINIMUM_TRICKS ? 0 : currentState.meld[teamIndex] + tricks;

    // Check for being set.
    BidManager bidManager = currentState.bidManager;
    boolean biddingTeam = TEAM_INDICES.get(teamIndex)
        .contains(bidManager.getHighBidPlayerIndex());
    int bid = bidManager.getBid();
    return biddingTeam && total < bid ? -bid : total;
  }

  private int advancePlayer(int currentIndex) {
    return currentIndex == 3 ? 0 : currentIndex + 1;
  }

  /**
   * Tracks the state of a hand during the game.
   */
  private static class HandState {
    private BidManager bidManager;
    private int[] meld;
    private int[] tricks;

    HandState(int dealerIndex) {
      bidManager = new BidManager(dealerIndex);
      meld = new int[] {0, 0};
      tricks = new int[] {0, 0};
    }
  }
}
