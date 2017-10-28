package org.eelbbor.carddeck.standard;

/**
 * Standard face values for a traditional set of French playing cards. The unicode offsets
 * skip the Knights, which would come between the Queen and King in some countries decks. Note:
 * The default sort order for the enum will yield the Ace high due to ordinals.
 *
 * @author Robb Lee (robbmlee@gmail.com)
 */
public enum FaceValue {
    Two(2),
    Three(3),
    Four(4),
    Five(5),
    Six(6),
    Seven(7),
    Eight(8),
    Nine(9),
    Ten(10),
    Jack(11),
    Queen(13),
    King(14),
    Ace(1);

    private int unicodeOffset;

    FaceValue(int unicodeOffset) {
        this.unicodeOffset = unicodeOffset;
    }

    /**
     * Returns the offset for the unicode character as associated with the values defined in the
     * {@link Suite} definitions. See the {@link Suite#getBaseUnicodeValue()} method.
     *
     * @return
     */
    public int getUnicodeOffset() {
        return unicodeOffset;
    }
}
