package org.eelbbor.carddeck.standard;

import org.eelbbor.carddeck.Card;
import org.eelbbor.carddeck.Type;

/**
 * Standard playing card using a traditional suite as defined by the {@link Suite} enum.
 *
 * @author Robb Lee (robbmlee@gmail.com)
 */
public class StandardCard extends Card {
    private Suite suite;

    /**
     * Constructor defining {@link Card} based on {@link Suite} definition.
     *
     * @param suite
     * @param ordinal
     * @param faceValue
     */
    public StandardCard(Suite suite, int ordinal, String faceValue) {
        super(new Type(suite.ordinal(), suite.name()), ordinal, faceValue);
        this.suite = suite;
    }

    public Suite getSuite() {
        return suite;
    }
}
