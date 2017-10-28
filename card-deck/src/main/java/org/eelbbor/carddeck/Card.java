package org.eelbbor.carddeck;


import java.util.Objects;

/**
 * Generic card definition based on a card {@link Type}, ordinal and value. This class enables
 * ordering of cards based on the {@link Type}, ordinal and value in respective order.
 *
 * NOTE: Ordering is focused on a behavior central to sorting a collection of cards by type and
 * then value, but not necessarily for determining if the value should defeat another card in a
 * game.
 *
 * @author Robb Lee (robbmlee@gmail.com).
 */
public class Card implements Comparable<Card> {
    private Type type;
    private int ordinal;
    private String value;

    /**
     * General constructor.
     *
     * @param type defintion of the card {@link Type} metadata.
     * @param ordinal index value indicating relative position of the card.
     * @param value string value indicating value on the card.
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

    public Type getType() {
        return type;
    }

    public int getOrdinal() {
        return ordinal;
    }

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

        int delta = this.type.compareTo(other.type);
        if (delta != 0) {
            return delta;
        }

        delta = Integer.compare(ordinal, other.ordinal);
        if (delta != 0) {
            return delta;
        }

        return this.value.compareTo(other.value);
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
        return ordinal == card.ordinal &&
            Objects.equals(type, card.type) &&
            Objects.equals(value, card.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ordinal, type, value);
    }
}
