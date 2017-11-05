package org.eelbbor.pinochle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eelbbor.carddeck.standard.Suite;
import org.junit.jupiter.api.Test;

class CardTest {
  @Test
  void shouldShortTenToCorrectLocation() {
    Suite suite = TestUtils.randomEnum(Suite.class);
    Card ten = new Card(suite, PinochleFaceValue.Ten);
    for (PinochleFaceValue faceValue : PinochleFaceValue.values()) {
      Card temp = new Card(suite, faceValue);
      if (faceValue == PinochleFaceValue.Ten) {
        assertEquals(0, temp.compareTo(ten));
      } else if (faceValue == PinochleFaceValue.Ace) {
        assertTrue(temp.compareTo(ten) > 0);
      } else {
        assertTrue(temp.compareTo(ten) < 0);
      }
      assertEquals(faceValue == PinochleFaceValue.Ten, temp.equals(ten));
    }
  }

  @Test
  void shouldOverrideOrdinalWithPinochleFaceValue() {
    Suite suite = TestUtils.randomEnum(Suite.class);
    for (PinochleFaceValue faceValue : PinochleFaceValue.values()) {
      Card card = new Card(suite, faceValue);
      assertEquals(faceValue, card.getFaceValue());
      assertEquals(card.getOrdinal(), faceValue.ordinal());
      assertNotEquals(card.getOrdinal(), faceValue.getStandardFaceValue().ordinal());
    }
  }
}