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
}