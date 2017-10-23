package org.eelbbor.carddeck.standard;

import org.eelbbor.carddeck.Type;

/**
 * Generic definition of suites based on a conventional card deck.
 *
 * @author Robb Lee (robbmlee@gmail.com).
 */
public enum Suite {
    Club,
    Diamond,
    Heart,
    Spade;

    private static final Type[] types = new Type[Suite.values().length];

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
}
