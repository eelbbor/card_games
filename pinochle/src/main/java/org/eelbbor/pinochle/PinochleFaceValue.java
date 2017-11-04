package org.eelbbor.pinochle;

import org.eelbbor.carddeck.standard.StandardFaceValue;

/**
 * Pinochle card face values based on the traditional set of French playing cards defined by the
 * {@link StandardFaceValue} implementation. The ordering of the values effects the sorting to
 * reflect the values in a pinochle hand.
 *
 * @author Robb Lee (robbmlee@gmail.com).
 */
public enum PinochleFaceValue {
  Jack(StandardFaceValue.Jack),
  Queen(StandardFaceValue.Queen),
  King(StandardFaceValue.King),
  Ten(StandardFaceValue.Ten),
  Ace(StandardFaceValue.Ace);

  private StandardFaceValue standardFaceValue;

  PinochleFaceValue(StandardFaceValue standardFaceValue) {
    this.standardFaceValue = standardFaceValue;
  }

  /**
   * Returns the {@link StandardFaceValue} this value is based on.
   *
   * @return {@link StandardFaceValue} value.
   */
  public StandardFaceValue getStandardFaceValue() {
    return standardFaceValue;
  }
}
