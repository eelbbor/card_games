package org.eelbbor.carddeck.standard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SuiteTest {
    @Test
    void defaultOrderShouldMirrorBridgeDefault() {
        assertEquals(4, Suite.values().length);
        assertEquals(1, Suite.Diamond.compareTo(Suite.Club));
        assertEquals(1, Suite.Heart.compareTo(Suite.Diamond));
        assertEquals(1, Suite.Spade.compareTo(Suite.Heart));
    }
}