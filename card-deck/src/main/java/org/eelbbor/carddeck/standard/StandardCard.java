package org.eelbbor.carddeck.standard;

import org.eelbbor.carddeck.Card;

/**
 * Standard playing card using a traditional suite as defined by the {@link Suite} enum and values
 * defined by the {@link StandardFaceValue}.
 *
 * @author Robb Lee (robbmlee@gmail.com)
 */
public class StandardCard extends Card {
  public static final int CARD_BACK_CODE_POINT = 0x1F0A0;
  public static final int RED_JOKER_CODE_POINT = 0x1F0BF;
  public static final int BLACK_JOKER_CODE_POINT = 0x1F0CF;
  public static final int WHITE_JOKER_CODE_POINT = 0x1F0DF;

  private Suite suite;
  private StandardFaceValue standardFaceValue;

  /**
   * Constructor defining {@link StandardCard} based on a {@link Suite} and
   * {@link StandardFaceValue} definition.
   *
   * @param suite     {@link Suite} the card belongs to.
   * @param standardFaceValue {@link StandardFaceValue} the face value on the card.
   */
  public StandardCard(Suite suite, StandardFaceValue standardFaceValue) {
    super(suite.getType(), standardFaceValue.ordinal(), standardFaceValue.name());
    this.suite = suite;
    this.standardFaceValue = standardFaceValue;
  }

  public Suite getSuite() {
    return suite;
  }

  public StandardFaceValue getStandardFaceValue() {
    return standardFaceValue;
  }

  public int getUnicodeCodePoint() {
    return suite.getBaseUnicodeValue() + standardFaceValue.getUnicodeOffset();
  }
}
