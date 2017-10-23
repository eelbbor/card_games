package org.eelbbor.carddeck;

import java.util.Objects;

/**
 * Generic definition to capture metatdata for a card type. An example would be a traditional
 * suite such as Clubs, but enables dynamic definitions such as a color. Also tracks an ordinal
 * to allow for sorting by the type. See the {@link Type#compareTo(Type)} method for more
 * information regarding the default sorting.
 *
 * @author Robb Lee (robbmlee@gmail.com).
 */
public class Type implements Comparable<Type> {
    private int ordinal;
    private String name;

    public Type(int ordinal, String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name must be defined.");
        }
        this.ordinal = ordinal;
        this.name = name;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public String getName() {
        return name;
    }

    /**
     * Enables ordering and comparison based on ordinal and then name.
     *
     * @param other {@link Type} to compare against.
     * @return int indicating where the type belongs in a sort order.
     */
    @Override
    public int compareTo(Type other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot compare to null.");
        }

        int delta = Integer.compare(ordinal, other.ordinal);
        return delta == 0 ? name.compareTo(other.name) : delta;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Type)) {
            return false;
        }
        Type type = (Type) other;
        return ordinal == type.ordinal && Objects.equals(name, type.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ordinal, name);
    }
}
