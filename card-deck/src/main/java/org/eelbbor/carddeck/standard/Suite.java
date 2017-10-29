package org.eelbbor.carddeck.standard;

import org.eelbbor.carddeck.Type;

/**
 * Standard suites for a traditional set of French playing cards.
 *
 * @author Robb Lee (robbmlee@gmail.com).
 */
public enum Suite {
  Club(0x1F0D0, 0x2663, 0x2667),
  Diamond(0x1F0C0, 0x2666, 0x2662),
  Heart(0x1F0B0, 0x2665, 0x2661),
  Spade(0x1F0A0, 0x2660, 0x2664);

  private static final Type[] types = new Type[Suite.values().length];

  private int baseUnicodeValue;
  private int blackSymbolCodePoint;
  private int whiteSymbolCodePoint;

  Suite(int baseUnicodeValue, int blackSymbolCodePoint, int whiteSymbolCodePoint) {
    this.baseUnicodeValue = baseUnicodeValue;
    this.blackSymbolCodePoint = blackSymbolCodePoint;
    this.whiteSymbolCodePoint = whiteSymbolCodePoint;
  }

  /**
   * Provides a base {@link Type} object correlating to the suite.
   *
   * @return {@link Type} object.
   */
  public Type getType() {
    if (types[ordinal()] == null) {
      types[ordinal()] = new Type(ordinal(), name());
    }
    return types[ordinal()];
  }

  /**
   * Returns the unicode code point value for the white symbol.
   *
   * @return int value of the white suite character codePoint.
   */
  public int getWhiteSymbolCodePoint() {
    return whiteSymbolCodePoint;
  }

  /**
   * Returns the unicode code point value for the black symbol.
   *
   * @return int value of the black suite character codePoint.
   */
  public int getBlackSymbolCodePoint() {
    return blackSymbolCodePoint;
  }

  /**
   * Returns the 0th location for a suite. The offset of the value of the card needs to be added
   * to the value to get the correct value.
   *
   * @return int base value a card unicode value for the suite.
   */
  public int getBaseUnicodeValue() {
    return baseUnicodeValue;
  }
}
