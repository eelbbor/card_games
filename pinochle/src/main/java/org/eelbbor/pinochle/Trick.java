package org.eelbbor.pinochle;

import org.eelbbor.carddeck.standard.Suite;
import org.eelbbor.pinochle.exceptions.CardPlayingErrorCode;
import org.eelbbor.pinochle.exceptions.InvalidCardException;

import java.util.List;
import java.util.Optional;

/**
 * Represents a single trick within a hand. Validates and tracks cards as they are played to
 * ensure the trick is valid and defends against a renig.
 *
 * @author Robb Lee (robbmlee@gmail.com).
 */
public class Trick {
  private Suite trump;
  private int highPlayerIndex;
  private Card highCard;
  private Card highTrump;
  private Card[] cards;

  /**
   * Generic constructor for creating a new trick.
   *
   * @param trump suite declared trump during play for this hand.
   */
  public Trick(Suite trump) {
    this.trump = trump;
    this.highPlayerIndex = -1;
    this.cards = new Card[4];
  }

  /**
   * Returns an immutable {@link List} of the cards played on the trick indexed by the player as
   * {@link Optional} in order to account for players yet to play.
   *
   * @return list of the cards played.
   */
  public List<Optional<Card>> getCardsPlayed() {
    return List.of(Optional.ofNullable(cards[0]),
        Optional.ofNullable(cards[1]),
        Optional.ofNullable(cards[2]),
        Optional.ofNullable(cards[3]));
  }

  public Suite getTrump() {
    return trump;
  }

  public int getHighPlayerIndex() {
    return highPlayerIndex;
  }

  public Optional<Card> getHighCard() {
    return Optional.ofNullable(highCard);
  }

  public Optional<Card> getHighTrump() {
    return Optional.ofNullable(highTrump);
  }

  /**
   * Adds the card to be played from the players cards to the trick if it is a valid play. If the
   * card is invalid the method will throw an exception and not add the card to the trick.
   *
   * @param card card to be played.
   * @param playersCards list of cards still in the players hand to validate against.
   * @throws InvalidCardException if the card to be played is invalid.
   */
  public void playCard(int playerIndex, Card card, List<Card> playersCards)
      throws InvalidCardException {
    if (cards[playerIndex] != null) {
      throw new RuntimeException(
          "Unexpected exception trying to play more than one card for player " + playerIndex
              + " on a single trick.");
    }

    if (!playersCards.contains(card)) {
      throw CardPlayingErrorCode.NO_SUCH_CARD_ERROR.createInvalidCardException(null, card);
    }

    // Check for first card played on the trick or exceeds necessary power.
    Suite ledSuite = highCard == null ? null : highCard.getSuite();
    if (ledSuite == null || card.getSuite() == ledSuite) {
      if (highTrump == null) {
        // Validate the card is sufficiently high.
        if (violatesHighestCard(playersCards, card, highCard)) {
          throw CardPlayingErrorCode.CARD_TOO_LOW_FOLLOWING_SUITE_ERROR
              .createInvalidCardException(highCard, card);
        } else if (highCard == null || highCard.getOrdinal() < card.getOrdinal()) {
          highPlayerIndex = playerIndex;
        }
      }
      highCard = highCard == null || highCard.getOrdinal() < card.getOrdinal() ? card : highCard;
    } else if (playersCards.stream().anyMatch(cd -> cd.getSuite() == ledSuite)) {
      throw CardPlayingErrorCode.FOLLOWING_SUITE_ERROR.createInvalidCardException(highCard, card);
    } else if (card.getSuite() == trump) {
      if (highTrump != null && violatesHighestCard(playersCards, card, highTrump)) {
        // Validate the trump card is sufficiently high.
        throw CardPlayingErrorCode.TRUMP_CARD_TOO_LOW_ERROR
            .createInvalidCardException(highTrump, card);
      } else if (highTrump == null || highTrump.getOrdinal() < card.getOrdinal()) {
        highPlayerIndex = playerIndex;
      }
      highTrump = highTrump == null || highTrump.getOrdinal() < card.getOrdinal()
          ? card : highTrump;
    } else if (playersCards.stream().anyMatch(cd -> cd.getSuite() == trump)) {
      throw CardPlayingErrorCode.TRUMP_ERROR.createInvalidCardException(
          highTrump == null ? new Card(trump, PinochleFaceValue.Jack) : highTrump, card);
    }

    cards[playerIndex] = card;
  }

  private boolean violatesHighestCard(List<Card> playersCards, Card played, Card high) {
    if (high == null) {
      return false;
    }

    return played.getOrdinal() <= high.getOrdinal()
        && playersCards.stream().anyMatch(
            testCard -> testCard.getSuite() == high.getSuite()
                && testCard.getOrdinal() > high.getOrdinal());

  }
}
