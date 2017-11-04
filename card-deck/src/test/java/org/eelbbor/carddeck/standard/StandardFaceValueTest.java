package org.eelbbor.carddeck.standard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eelbbor.carddeck.TestUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

class StandardFaceValueTest {
  @Test
  void shouldDefaultSortToAceHigh() {
    Set<StandardFaceValue> values = new HashSet<>();
    while (values.size() < StandardFaceValue.values().length) {
      values.add(TestUtils.randomEnum(StandardFaceValue.class));
    }
    Iterator<StandardFaceValue> unsortedIterator = values.iterator();
    int index = 0;
    boolean isUnsorted = false;
    while (unsortedIterator.hasNext() && !isUnsorted) {
      isUnsorted = unsortedIterator.next() != StandardFaceValue.values()[index];
      index++;
    }
    assertTrue(isUnsorted);

    TreeSet<StandardFaceValue> sortedValues = new TreeSet<>(values);
    Iterator<StandardFaceValue> sortedIterator = sortedValues.iterator();
    assertEquals(StandardFaceValue.Two, sortedIterator.next());
    assertEquals(StandardFaceValue.Three, sortedIterator.next());
    assertEquals(StandardFaceValue.Four, sortedIterator.next());
    assertEquals(StandardFaceValue.Five, sortedIterator.next());
    assertEquals(StandardFaceValue.Six, sortedIterator.next());
    assertEquals(StandardFaceValue.Seven, sortedIterator.next());
    assertEquals(StandardFaceValue.Eight, sortedIterator.next());
    assertEquals(StandardFaceValue.Nine, sortedIterator.next());
    assertEquals(StandardFaceValue.Ten, sortedIterator.next());
    assertEquals(StandardFaceValue.Jack, sortedIterator.next());
    assertEquals(StandardFaceValue.Queen, sortedIterator.next());
    assertEquals(StandardFaceValue.King, sortedIterator.next());
    assertEquals(StandardFaceValue.Ace, sortedIterator.next());
    assertFalse(sortedIterator.hasNext());
  }

  @Test
  void shouldSetOffsetsForCodePointCorrectly() {
    assertEquals(13, StandardFaceValue.values().length);
    assertEquals(1, StandardFaceValue.Ace.getUnicodeOffset());
    assertEquals(2, StandardFaceValue.Two.getUnicodeOffset());
    assertEquals(3, StandardFaceValue.Three.getUnicodeOffset());
    assertEquals(4, StandardFaceValue.Four.getUnicodeOffset());
    assertEquals(5, StandardFaceValue.Five.getUnicodeOffset());
    assertEquals(6, StandardFaceValue.Six.getUnicodeOffset());
    assertEquals(7, StandardFaceValue.Seven.getUnicodeOffset());
    assertEquals(8, StandardFaceValue.Eight.getUnicodeOffset());
    assertEquals(9, StandardFaceValue.Nine.getUnicodeOffset());
    assertEquals(10, StandardFaceValue.Ten.getUnicodeOffset());
    assertEquals(11, StandardFaceValue.Jack.getUnicodeOffset());
    assertEquals(13, StandardFaceValue.Queen.getUnicodeOffset());
    assertEquals(14, StandardFaceValue.King.getUnicodeOffset());
  }

  @Test
  void valueOfShouldResolveBasedOnName() {
    Arrays.stream(StandardFaceValue.values()).forEach(faceValue -> {
      assertEquals(faceValue, StandardFaceValue.valueOf(faceValue.name()));
    });
  }
}