package org.eelbbor.carddeck.standard;

import org.eelbbor.carddeck.Type;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SuiteTest {
    @Test
    void defaultOrderShouldMirrorBridgeDefault() {
        assertEquals(4, Suite.values().length);
        assertEquals(1, Suite.Diamond.compareTo(Suite.Club));
        assertEquals(1, Suite.Heart.compareTo(Suite.Diamond));
        assertEquals(1, Suite.Spade.compareTo(Suite.Heart));
    }

    @Test
    void shouldOnlyInitializeTypeOnce() {
        for (Suite suite : Suite.values()) {
            Type type = suite.getType();
            assertEquals(suite.ordinal(), type.getOrdinal());
            assertEquals(suite.name(), type.getName());
            assertTrue(type == suite.getType());
        }
    }

    @Test
    void valueOfShouldResolveBasedOnName() {
        Arrays.stream(Suite.values()).forEach(suit -> {
            assertEquals(suit, Suite.valueOf(suit.name()));
        });
    }

    @Test
    void valueOfBlackSymbolShouldBeCorrect() {
        assertEquals(0x2663, Suite.Club.getBlackSymbolCodePoint());
        assertEquals(0x2666, Suite.Diamond.getBlackSymbolCodePoint());
        assertEquals(0x2665, Suite.Heart.getBlackSymbolCodePoint());
        assertEquals(0x2660, Suite.Spade.getBlackSymbolCodePoint());
    }

    @Test
    void valueOfWhiteSymbolShouldBeCorrect() {
        assertEquals(0x2667, Suite.Club.getWhiteSymbolCodePoint());
        assertEquals(0x2662, Suite.Diamond.getWhiteSymbolCodePoint());
        assertEquals(0x2661, Suite.Heart.getWhiteSymbolCodePoint());
        assertEquals(0x2664, Suite.Spade.getWhiteSymbolCodePoint());
    }

    @Test
    void valueOfBaseSymbolShouldBeZeroBitLocation() {
        assertEquals(0x1F0D0, Suite.Club.getBaseUnicodeValue());
        assertEquals(0x1F0C0, Suite.Diamond.getBaseUnicodeValue());
        assertEquals(0x1F0B0, Suite.Heart.getBaseUnicodeValue());
        assertEquals(0x1F0A0, Suite.Spade.getBaseUnicodeValue());
    }
}