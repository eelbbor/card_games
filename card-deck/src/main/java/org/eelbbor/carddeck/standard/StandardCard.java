package org.eelbbor.carddeck.standard;

import org.eelbbor.carddeck.Card;

/**
 * Standard playing card using a traditional suite as defined by the {@link Suite} enum and values
 * defined by the {@link FaceValue}.
 *
 * @author Robb Lee (robbmlee@gmail.com)
 */
public class StandardCard extends Card {
  public static final int CARD_BACK_CODE_POINT = 0x1F0A0;
  public static final int RED_JOKER_CODE_POINT = 0x1F0BF;
  public static final int BLACK_JOKER_CODE_POINT = 0x1F0CF;
  public static final int WHITE_JOKER_CODE_POINT = 0x1F0DF;

  private Suite suite;
  private FaceValue faceValue;

  /**
   * Constructor defining {@link StandardCard} based on a {@link Suite} and {@link FaceValue}
   * definition.
   *
   * @param suite     {@link Suite} the card belongs to.
   * @param faceValue {@link FaceValue} the face value on the card.
   */
  public StandardCard(Suite suite, FaceValue faceValue) {
    super(suite.getType(), faceValue.ordinal(), faceValue.name());
    this.suite = suite;
    this.faceValue = faceValue;
  }

  public Suite getSuite() {
    return suite;
  }

  public FaceValue getFaceValue() {
    return faceValue;
  }

  public int getUnicodeCodePoint() {
    return suite.getBaseUnicodeValue() + faceValue.getUnicodeOffset();
  }
}
