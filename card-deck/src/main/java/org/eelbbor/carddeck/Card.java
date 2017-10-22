package org.eelbbor.carddeck;


import java.util.Objects;

/**
 * Generic card definition based on a traditional suite defined in {@link Suite} and corresponding
 * face value as described by an ordinal number and string value on the card. This class enables
 * ordering of cards based on the {@link Suite}, ordinal and face value in respective order.
 *
 * NOTE: Ordering is focused on a behavior central to sorting a collection of cards by suite and
 * then value, but not necessarily for determining if the value should defeat another card in a
 * game.
 *
 * @author Robb Lee (robbmlee@gmail.com).
 */
public class Card implements Comparable<Card> {
    private Type type;
    private Suite suite;
    private int ordinal;
    private String faceValue;

    /**
     * General constructor.
     *
     * @param suite one of the suite definitions from the {@link Suite} enum.
     * @param ordinal index value indicating relative position of the card.
     * @param faceValue string value indicating value on the card.
     */
    public Card(Suite suite, int ordinal, String faceValue) {
        if (suite == null) {
            throw new IllegalArgumentException("Suite must be defined.");
        }

        if (faceValue == null || faceValue.trim().isEmpty()) {
            throw new IllegalArgumentException("Face value must be defined.");
        }
        this.suite = suite;
        this.ordinal = ordinal;
        this.faceValue = faceValue;
    }

    public Suite getSuite() {
        return suite;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public String getFaceValue() {
        return faceValue;
    }

    /**
     * Enables ordering and card comparison based on {@link Type}, ordinal and lastly face value.
     *
     * @param other {@link Card} to compare against.
     * @return int indicating where the card belongs in a sort order.
     */
    @Override
    public int compareTo(Card other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot compare to null.");
        }

        int delta = this.suite.compareTo(other.suite);
        if (delta != 0) {
            return delta;
        }

        delta = this.ordinal - other.ordinal;
        if (delta != 0) {
            return delta;
        }

        return this.faceValue.compareTo(other.faceValue);
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
        return ordinal == card.ordinal && suite == card.suite &&
            Objects.equals(faceValue, card.faceValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ordinal, suite, faceValue);
    }
}
