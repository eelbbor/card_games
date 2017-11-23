package org.eelbbor.pinochle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.eelbbor.carddeck.standard.Suite;
import org.eelbbor.pinochle.exceptions.InvalidBiddingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class GameTest {
  private Game game;

  @BeforeEach
  void setUp() {
    game = new Game();
  }

  @Test
  void validateConstants() {
    assertEquals(50, BidManager.MINIMUM_BID);
    assertEquals(60, BidManager.MINIMUM_INCREMENT_BY_FIVE_BID);
  }

  @Test
  void shouldDealOnInitialization() {
    assertEquals(0, game.getDealer());
    assertEquals(0, game.getCurrentLead());
    assertEquals(1, game.getCurrentPlayer());
    assertEquals(0, game.getTeamScore(Game.TEAM_ONE_INDEX));
    assertEquals(0, game.getTeamScore(Game.TEAM_TWO_INDEX));
    IntStream.range(0, 4).forEach(i -> assertEquals(20, game.getPlayersRemainingCards(i).size()));
    assertTrue(game.isBidding());
    assertFalse(game.isDeclaringTrump());
    assertFalse(game.isPlayingHand());
    assertEquals(0, game.getCurrentBid());
  }

  @Test
  void shouldThrowExceptionIfAttemptToPlayWhileBidding() throws Exception {
    List<Card> cards = game.getPlayersRemainingCards(game.getCurrentPlayer());
    try {
      game.playCard(cards.get(TestUtils.randomInteger(cards.size())));
      fail("Should have thrown exception for trying to play while bidding.");
    } catch (InvalidBiddingException e) {
      assertEquals(InvalidBiddingException.biddingInProgress().getMessage(), e.getMessage());
    }
  }

  @Test
  void shouldSetDealingTeamAndStartNewHandWithInsufficientMeld() {
    // TODO (robb): fail("");
  }

  @Test
  void shouldAdvanceDealerOnSubsequentHandStart() throws Exception {
    // TODO (robb): fail("");
  }

  @Test
  void shouldChangeTheLeadOnLosingATrick() throws Exception {
    /*fastCompleteBidding(null);
    // Find largest suite for trump.
    assertEquals(game.getDealer(), game.getCurrentPlayer());
    List<Card> dealerCards = game.getPlayersRemainingCards(game.getCurrentPlayer());
    Collections.sort(dealerCards, Comparator.comparingInt(Card::getOrdinal));
    Card lowest = dealerCards.get(0);
    assertEquals(1, game.playCard(lowest));

    // Follow suite and play the next highest card until back to the dealer.
    IntStream.rangeClosed(1, 3).forEach(player -> {
      List<Card> playersRemainingCards = game.getPlayersRemainingCards(player);
      List<Card> suiteCards = playersRemainingCards.stream()
          .filter(card -> card.getSuite() == lowest.getSuite()).sorted()
          .collect(Collectors.toList());
      if (suiteCards.size() > 0) {
        // Find the next highest in the suite.

      } else {
        // Find trump if out of led suite.
      }
    });*/

    // TODO (robb): fail("Implement this test.");
  }

  @Test
  void shouldCompleteTheGameOnTheFinalCard() {
    // TODO (robb): fail("");
  }

  private int computeExpectedTeamMeld(Suite trump, Hand playerOne, Hand playerTwo) {
    int meld = playerOne.countMeld(trump) + playerTwo.countMeld(trump);
    return meld < Game.MINIMUM_MELD ? 0 : meld;
  }

  private void fastCompleteBidding(Suite trump) throws Exception {
    while (game.isBidding()) {
      game.pass();
    }

    if (trump != null) {
      game.declareTrump(trump);
      assertEquals(trump, game.getTrumpSuite().get());
      Hand playerOneTeamOne = buildHandFromRemainingCards(0);
      Hand playerTwoTeamOne = buildHandFromRemainingCards(2);
      int teamOneMeld = computeExpectedTeamMeld(trump, playerOneTeamOne, playerTwoTeamOne);
      assertEquals(teamOneMeld < Game.MINIMUM_MELD ? 0 : teamOneMeld,
          game.getTeamMeld(Game.TEAM_ONE_INDEX));

      Hand playerOneTeamTwo = buildHandFromRemainingCards(1);
      Hand playerTwoTeamTwo = buildHandFromRemainingCards(3);
      int teamTwoMeld = computeExpectedTeamMeld(trump, playerOneTeamTwo, playerTwoTeamTwo);
      assertEquals(teamTwoMeld < Game.MINIMUM_MELD ? 0 : teamTwoMeld,
          game.getTeamMeld(Game.TEAM_TWO_INDEX));
    } else {
      assertFalse(game.getTrumpSuite().isPresent());
      assertEquals(0, game.getTeamMeld(Game.TEAM_ONE_INDEX));
      assertEquals(0, game.getTeamMeld(Game.TEAM_TWO_INDEX));
    }
  }

  private Map<Suite, List<Card>> getPlayerSuiteMap(int playerIndex) {
    return game.getPlayersRemainingCards(playerIndex).stream()
        .collect(Collectors.groupingBy(Card::getSuite));
  }

  private Hand buildHandFromRemainingCards(int player) {
    Hand result = new Hand();
    game.getPlayersRemainingCards(player).forEach(card -> result.dealCard(card));
    return result;
  }
}