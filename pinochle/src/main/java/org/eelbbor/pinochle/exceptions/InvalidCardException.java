package org.eelbbor.pinochle.exceptions;

/**
 * Checked exception for tracking issues when trying to playing a card. Implements several
 * factory methods for creating a consistent messaging structure.
 *
 * @author Robb Lee (robbmlee@gmail.com).
 */
public class InvalidCardException extends Exception {
  private CardPlayingErrorCode errorCode;

  InvalidCardException(CardPlayingErrorCode code, String message) {
    super(message);
    this.errorCode = code;
  }

  /**
   * Conveys the error code cause of the exception to allow for reasonable adaptation to an issue.
   *
   * @return error code indicating the root issue.
   */
  public CardPlayingErrorCode getErrorCode() {
    return errorCode;
  }
}
