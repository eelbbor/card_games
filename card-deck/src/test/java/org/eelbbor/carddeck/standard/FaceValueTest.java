package org.eelbbor.carddeck.standard;

import org.eelbbor.carddeck.TestUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FaceValueTest {
    @Test
    void shouldDefaultSortToAceHigh() {
        Set<FaceValue> values = new HashSet<>();
        while(values.size() < FaceValue.values().length) {
            values.add(TestUtils.randomEnum(FaceValue.class));
        }
        Iterator<FaceValue> unsortedIterator = values.iterator();
        int index = 0;
        boolean isUnsorted = false;
        while(unsortedIterator.hasNext() && !isUnsorted) {
            isUnsorted = unsortedIterator.next() != FaceValue.values()[index];
            index++;
        }
        assertTrue(isUnsorted);

        TreeSet<FaceValue> sortedValues = new TreeSet<>(values);
        Iterator<FaceValue> sortedIterator = sortedValues.iterator();
        assertEquals(FaceValue.Two, sortedIterator.next());
        assertEquals(FaceValue.Three, sortedIterator.next());
        assertEquals(FaceValue.Four, sortedIterator.next());
        assertEquals(FaceValue.Five, sortedIterator.next());
        assertEquals(FaceValue.Six, sortedIterator.next());
        assertEquals(FaceValue.Seven, sortedIterator.next());
        assertEquals(FaceValue.Eight, sortedIterator.next());
        assertEquals(FaceValue.Nine, sortedIterator.next());
        assertEquals(FaceValue.Ten, sortedIterator.next());
        assertEquals(FaceValue.Jack, sortedIterator.next());
        assertEquals(FaceValue.Queen, sortedIterator.next());
        assertEquals(FaceValue.King, sortedIterator.next());
        assertEquals(FaceValue.Ace, sortedIterator.next());
        assertFalse(sortedIterator.hasNext());
    }

    @Test
    void shouldSetOffsetsForCodePointCorrectly() {
        assertEquals(13, FaceValue.values().length);
        assertEquals(1, FaceValue.Ace.getUnicodeOffset());
        assertEquals(2, FaceValue.Two.getUnicodeOffset());
        assertEquals(3, FaceValue.Three.getUnicodeOffset());
        assertEquals(4, FaceValue.Four.getUnicodeOffset());
        assertEquals(5, FaceValue.Five.getUnicodeOffset());
        assertEquals(6, FaceValue.Six.getUnicodeOffset());
        assertEquals(7, FaceValue.Seven.getUnicodeOffset());
        assertEquals(8, FaceValue.Eight.getUnicodeOffset());
        assertEquals(9, FaceValue.Nine.getUnicodeOffset());
        assertEquals(10, FaceValue.Ten.getUnicodeOffset());
        assertEquals(11, FaceValue.Jack.getUnicodeOffset());
        assertEquals(13, FaceValue.Queen.getUnicodeOffset());
        assertEquals(14, FaceValue.King.getUnicodeOffset());
    }

    @Test
    void valueOfShouldResolveBasedOnName() {
        Arrays.stream(FaceValue.values()).forEach(faceValue -> {
            assertEquals(faceValue, FaceValue.valueOf(faceValue.name()));
        });
    }
}