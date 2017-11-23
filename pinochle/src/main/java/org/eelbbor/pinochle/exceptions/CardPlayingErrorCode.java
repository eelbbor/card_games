package org.eelbbor.pinochle.exceptions;

import org.eelbbor.pinochle.Card;

/**
 * Enum capturing validation codes for playing a card.
 *
 * @author Robb Lee (robbmlee@gmail.com).
 */
public enum CardPlayingErrorCode {
  /**
   * Conveys an attempt at a playing a card, which is not in the players hand.
   */
  NO_SUCH_CARD_ERROR(((minCard, played) ->
      "Trying to play a '" + played + "', which is not in the player's cards.")),

  /**
   * Attempting to play a card that does not follow the suite led when the player can follow suite.
   */
  FOLLOWING_SUITE_ERROR((minCard, played) ->
      "Must follow suite led, '" + minCard.getSuite().name() + "', but attempted to play a '"
          + played.getSuite().name() + "'."),

  /**
   * Attempting to play a card which follows suite, but is lower than required.
   */
  CARD_TOO_LOW_FOLLOWING_SUITE_ERROR(((minCard, played) -> "Must beat highest card, '"
      + minCard.toString() + "', but played '" + played.toString() + "'.")),

  /**
   * Attempting to play a card in an off-suite when the player cannot follow the suite led, but
   * still has trump remaining.
   */
  TRUMP_ERROR(((minCard, played) -> "Must play trump, '" + minCard.getSuite().name()
      + "', but attempted to play a '" + played.getSuite().name() + "'.")),

  /**
   * Attempting to play a trump card that is lower than required.
   */
  TRUMP_CARD_TOO_LOW_ERROR(((minCard, played) -> "Must beat highest trump card, '"
      + minCard.toString() + "', but played '" + played.toString() + "'."));

  private CardPlayingErrorMessage message;

  CardPlayingErrorCode(CardPlayingErrorMessage message) {
    this.message = message;
  }

  public InvalidCardException createInvalidCardException(Card minCard, Card playedCard) {
    return new InvalidCardException(this, message.createErrorMessage(minCard, playedCard));
  }

  @FunctionalInterface
  private interface CardPlayingErrorMessage {
    String createErrorMessage(Card minCard, Card played);
  }
}
