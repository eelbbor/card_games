package org.eelbbor.pinochle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eelbbor.carddeck.standard.FaceValue;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


class PinochleFaceValueTest {
  @Test
  void shouldApplyCorrectSortAccordingToCardStrength() {
    Set<PinochleFaceValue> values = new HashSet<>();
    while (values.size() < PinochleFaceValue.values().length) {
      values.add(TestUtils.randomEnum(PinochleFaceValue.class));
    }
    Iterator<PinochleFaceValue> unsortedIterator = values.iterator();
    int index = 0;
    boolean isUnsorted = false;
    while (unsortedIterator.hasNext() && !isUnsorted) {
      isUnsorted = unsortedIterator.next() != PinochleFaceValue.values()[index];
      index++;
    }
    assertTrue(isUnsorted);

    TreeSet<PinochleFaceValue> sortedValues = new TreeSet<>(values);
    Iterator<PinochleFaceValue> sortedIterator = sortedValues.iterator();
    assertEquals(PinochleFaceValue.Jack, sortedIterator.next());
    assertEquals(PinochleFaceValue.Queen, sortedIterator.next());
    assertEquals(PinochleFaceValue.King, sortedIterator.next());
    assertEquals(PinochleFaceValue.Ten, sortedIterator.next());
    assertEquals(PinochleFaceValue.Ace, sortedIterator.next());
    assertFalse(sortedIterator.hasNext());
  }

  @Test
  void shouldSetOffsetsForCodePointCorrectly() {
    assertEquals(5, PinochleFaceValue.values().length);
    assertEquals(FaceValue.Jack.getUnicodeOffset(), PinochleFaceValue.Jack.getUnicodeOffset());
    assertEquals(FaceValue.Queen.getUnicodeOffset(), PinochleFaceValue.Queen.getUnicodeOffset());
    assertEquals(FaceValue.King.getUnicodeOffset(), PinochleFaceValue.King.getUnicodeOffset());
    assertEquals(FaceValue.Ten.getUnicodeOffset(), PinochleFaceValue.Ten.getUnicodeOffset());
    assertEquals(FaceValue.Ace.getUnicodeOffset(), PinochleFaceValue.Ace.getUnicodeOffset());
  }

  @Test
  void valueOfShouldResolveBasedOnName() {
    Arrays.stream(PinochleFaceValue.values()).forEach(faceValue -> {
      assertEquals(faceValue, PinochleFaceValue.valueOf(faceValue.name()));
    });
  }
}
