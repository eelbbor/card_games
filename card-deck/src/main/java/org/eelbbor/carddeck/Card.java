package org.eelbbor.carddeck;


import java.util.Objects;

/**
 * Generic card definition based on a card {@link Type}, ordinal and value. This class enables
 * ordering of cards based on the {@link Type}, ordinal and value in respective order.
 *
 * <p/>
 * NOTE: Ordering is focused on a behavior central to sorting a collection of cards by type and then
 * value, but not necessarily for determining if the value should defeat another card in a game.
 *
 * @author Robb Lee (robbmlee@gmail.com).
 */
public class Card implements Comparable<Card> {
  private Type type;
  private int ordinal;
  private String value;

  /**
   * General constructor. Type and value cannot be null nor empty or an exception will be thrown.
   *
   * @param type    definition of the card {@link Type} metadata.
   * @param ordinal index value indicating relative position of the card.
   * @param value   string value indicating value on the card.
   */
  public Card(Type type, int ordinal, String value) {
    if (type == null) {
      throw new IllegalArgumentException("Type must be defined.");
    }

    if (value == null || value.trim().isEmpty()) {
      throw new IllegalArgumentException("Value must be defined.");
    }

    this.type = type;
    this.ordinal = ordinal;
    this.value = value;
  }

  /**
   * Returns the card type, which serves as a higher level classification for the card.
   *
   * @return {@link Type} of the card.
   */
  public Type getType() {
    return type;
  }

  /**
   * Returns the relative power of the card within a given {@link Type}.
   *
   * @return int value of card rank.
   */
  public int getOrdinal() {
    return ordinal;
  }

  /**
   * Returns a string value indication of the card value, i.e. King or One
   *
   * @return string representation of the card value.
   */
  public String getValue() {
    return value;
  }

  /**
   * Enables ordering and card comparison based on {@link Type}, ordinal and lastly value.
   *
   * @param other {@link Card} to compare against.
   * @return int indicating where the card belongs in a sort order.
   */
  @Override
  public int compareTo(Card other) {
    if (other == null) {
      throw new IllegalArgumentException("Cannot compare to null.");
    }

    int delta = this.getType().compareTo(other.getType());
    if (delta != 0) {
      return delta;
    }

    delta = Integer.compare(getOrdinal(), other.getOrdinal());
    if (delta != 0) {
      return delta;
    }

    return this.getValue().compareTo(other.getValue());
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof Card)) {
      return false;
    }
    Card card = (Card) other;
    return getOrdinal() == card.getOrdinal()
        && Objects.equals(getType(), card.getType())
        && Objects.equals(getValue(), card.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getOrdinal(), getType(), getValue());
  }
}
