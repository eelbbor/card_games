package org.eelbbor.carddeck.standard;

import org.eelbbor.carddeck.Type;

import java.util.Arrays;

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

    private static Type[] types = (Type[]) Arrays.stream(Suite.values()).
            map(suite -> new Type(suite.ordinal(), suite.name())).toArray();

    public Type getType() {
        return types[this.ordinal()];
    }
}
