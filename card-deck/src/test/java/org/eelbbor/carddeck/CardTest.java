package org.eelbbor.carddeck;

import static org.eelbbor.carddeck.TestUtils.randomInteger;
import static org.eelbbor.carddeck.TestUtils.randomString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CardTest {
  private Card randomCard;
  private Type expectedType;
  private int expectedOrdinal;
  private String expectedFaceValue;

  @BeforeEach
  void setUp() {
    expectedType = new Type(randomInteger(), randomString());
    expectedOrdinal = randomInteger();
    expectedFaceValue = randomString();
    randomCard = new Card(expectedType, expectedOrdinal, expectedFaceValue);
  }

  @Test
  void shouldSetGenericValues() {
    assertEquals(expectedType, randomCard.getType());
    assertEquals(expectedOrdinal, randomCard.getOrdinal());
    assertEquals(expectedFaceValue, randomCard.getValue());

    assertEquals(0, randomCard.compareTo(randomCard));
    assertEquals(randomCard, randomCard);
    assertEquals(randomCard.hashCode(), randomCard.hashCode());
  }

  @Test
  void shouldThrowExceptionForNullType() {
    assertEquals("Type must be defined.", assertThrows(IllegalArgumentException.class,
        () -> new Card(null, randomInteger(), randomString())).getMessage());
  }

  @Test
  void shouldThrowExceptionForNullFaceValue() {
    assertEquals("Value must be defined.", assertThrows(IllegalArgumentException.class,
        () -> new Card(expectedType, randomInteger(), null)).getMessage());
  }

  @Test
  void shouldThrowExceptionForEmptyFaceValue() {
    assertEquals("Value must be defined.", assertThrows(IllegalArgumentException.class,
        () -> new Card(expectedType, randomInteger(), "")).getMessage());
  }

  @Test
  void shouldThrowExceptionForBlankFaceValue() {
    assertEquals("Value must be defined.", assertThrows(IllegalArgumentException.class,
        () -> new Card(expectedType, randomInteger(), "    ")).getMessage());
  }

  @Test
  void shouldThrowExceptionForNullCompareTo() {
    assertEquals("Cannot compare to null.",
        assertThrows(IllegalArgumentException.class, () -> randomCard.compareTo(null))
            .getMessage());
  }

  @Test
  void shouldNotBeEqualForNull() {
    assertFalse(randomCard.equals(null));
  }

  @Test
  void shouldNotBeEqualForDifferingObjectType() {
    assertFalse(randomCard.equals(randomString()));
  }

  @Test
  void shouldNotBeEqualForDifferentSuite() {
    Type otherType = new Type(randomInteger(), randomString());
    assertNotEquals(expectedType, otherType);

    Card differentCard = new Card(otherType, expectedOrdinal, expectedFaceValue);
    assertFalse(randomCard.equals(differentCard));
    assertNotEquals(0, randomCard.compareTo(differentCard));
    assertEquals(expectedType.compareTo(otherType), randomCard.compareTo(differentCard));
  }

  @Test
  void shouldNotBeEqualForDifferentOrdinal() {
    int ordinal;
    do {
      ordinal = randomInteger();
    } while (ordinal == expectedOrdinal);
    Card differentCard = new Card(expectedType, ordinal, expectedFaceValue);
    assertFalse(randomCard.equals(differentCard));
    assertNotEquals(0, randomCard.compareTo(differentCard));
    assertEquals(expectedOrdinal > ordinal ? 1 : -1, randomCard.compareTo(differentCard));
  }

  @Test
  void shouldNotBeEqualForDifferentFaceValue() {
    String faceValue;
    do {
      faceValue = randomString();
    } while (faceValue.equals(expectedFaceValue));
    Card differentCard = new Card(expectedType, expectedOrdinal, faceValue);
    assertFalse(randomCard.equals(differentCard));
    assertNotEquals(0, randomCard.compareTo(differentCard));
    assertEquals(expectedFaceValue.compareTo(faceValue), randomCard.compareTo(differentCard));
  }
}