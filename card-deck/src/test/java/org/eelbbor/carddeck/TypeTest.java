package org.eelbbor.carddeck;

import static org.eelbbor.carddeck.TestUtils.randomInteger;
import static org.eelbbor.carddeck.TestUtils.randomString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeTest {
  private int expectedOrdinal;
  private String expectedName;
  private Type randomType;

  @BeforeEach
  void setUp() {
    expectedOrdinal = randomInteger();
    expectedName = randomString();
    randomType = new Type(expectedOrdinal, expectedName);
  }

  @Test
  void shouldCreateType() {
    assertEquals(expectedOrdinal, randomType.getOrdinal());
    assertEquals(expectedName, randomType.getName());

    assertEquals(randomType, randomType);
    assertEquals(randomType.hashCode(), randomType.hashCode());
    assertEquals(0, randomType.compareTo(randomType));
  }

  @Test
  void shouldThrowExceptionForNullName() {
    assertEquals("Name must be defined.",
        assertThrows(IllegalArgumentException.class, () -> new Type(randomInteger(), null))
            .getMessage());
  }

  @Test
  void shouldThrowExceptionForEmptyName() {
    assertEquals("Name must be defined.",
        assertThrows(IllegalArgumentException.class, () -> new Type(randomInteger(), ""))
            .getMessage());
  }

  @Test
  void shouldThrowExceptionForBlankName() {
    assertEquals("Name must be defined.",
        assertThrows(IllegalArgumentException.class, () -> new Type(randomInteger(), "      "))
            .getMessage());
  }

  @Test
  void shouldThrowExceptionForNullCompareTo() {
    assertEquals("Cannot compare to null.",
        assertThrows(IllegalArgumentException.class, () -> randomType.compareTo(null))
            .getMessage());
  }

  @Test
  void shouldBeEqualForSameOrdinalAndName() {
    int ordinal = randomInteger();
    String name = randomString();
    Type typeOne = new Type(ordinal, name);
    Type typeTwo = new Type(ordinal, name);
    assertFalse(typeOne == typeTwo);
    assertEquals(typeOne, typeTwo);
  }

  @Test
  void shouldNotBeEqualForNull() {
    assertFalse(randomType.equals(null));
  }

  @Test
  void shouldNotBeEqualForDifferingObjectType() {
    assertFalse(randomType.equals(randomString()));
  }

  @Test
  void shouldNotBeEqualForDifferentOrdinal() {
    int ordinal;
    do {
      ordinal = randomInteger();
    } while (ordinal == expectedOrdinal);
    Type differentType = new Type(ordinal, expectedName);
    assertFalse(randomType.equals(differentType));
    assertNotEquals(0, randomType.compareTo(differentType));
    assertEquals(expectedOrdinal > ordinal ? 1 : -1, randomType.compareTo(differentType));
  }

  @Test
  void shouldNotBeEqualForDifferentName() {
    String faceValue;
    do {
      faceValue = randomString();
    } while (faceValue.equals(expectedName));
    Type differentType = new Type(expectedOrdinal, faceValue);
    assertFalse(randomType.equals(differentType));
    assertNotEquals(0, randomType.compareTo(differentType));
    assertEquals(expectedName.compareTo(faceValue), randomType.compareTo(differentType));
  }
}