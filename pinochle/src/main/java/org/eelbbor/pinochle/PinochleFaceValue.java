package org.eelbbor.pinochle;

import org.eelbbor.carddeck.standard.FaceValue;

/**
 * Pinochle card face values based on the traditional set of French playing cards defined by the
 * {@link FaceValue} implementation.
 *
 * @author Robb Lee (robbmlee@gmail.com).
 */
public enum PinochleFaceValue {
  Jack(FaceValue.Jack),
  Queen(FaceValue.Queen),
  King(FaceValue.King),
  Ten(FaceValue.Ten),
  Ace(FaceValue.Ace);

  private FaceValue faceValue;

  PinochleFaceValue(FaceValue faceValue) {
    this.faceValue = faceValue;
  }

  /**
   * Returns the offset for the unicode character as associated with the values defined in the
   * {@link FaceValue} definitions.
   *
   * @return int value of the character codePoint offset.
   */
  public int getUnicodeOffset() {
    return faceValue.getUnicodeOffset();
  }
}
