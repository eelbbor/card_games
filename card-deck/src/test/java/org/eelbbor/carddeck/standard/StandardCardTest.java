package org.eelbbor.carddeck.standard;

import org.junit.jupiter.api.Test;

import static org.eelbbor.carddeck.TestUtils.randomEnum;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StandardCardTest {
    @Test
    void shouldCreateTypeBasedOnSuiteAndFaceValue() {
        Suite suite = randomEnum(Suite.class);
        FaceValue faceValue = randomEnum(FaceValue.class);
        StandardCard card = new StandardCard(suite, faceValue);

        assertEquals(suite, card.getSuite());
        assertEquals(suite.ordinal(), card.getType().getOrdinal());
        assertEquals(suite.name(), card.getType().getName());

        assertEquals(faceValue, card.getFaceValue());
        assertEquals(faceValue.ordinal(), card.getOrdinal());
        assertEquals(faceValue.name(), card.getValue());
    }

    @Test
    void shouldReturnUnicodeCharacterCodePointForPlayingCards() {
        for (Suite suite : Suite.values()) {
            for (FaceValue faceValue : FaceValue.values()) {
                StandardCard card = new StandardCard(suite, faceValue);
                assertEquals(Character.UnicodeBlock.PLAYING_CARDS,
                    Character.UnicodeBlock.of(card.getUnicodeCodePoint()));

            }
        }
    }

    @Test
    void shouldSetCorrectUnicodeCharactersForClubs() {
        assertEquals(13, FaceValue.values().length);
        Suite suite = Suite.Club;
        assertEquals(0x1F0D1, new StandardCard(suite, FaceValue.Ace).getUnicodeCodePoint());
        assertEquals(0x1F0D2, new StandardCard(suite, FaceValue.Two).getUnicodeCodePoint());
        assertEquals(0x1F0D3, new StandardCard(suite, FaceValue.Three).getUnicodeCodePoint());
        assertEquals(0x1F0D4, new StandardCard(suite, FaceValue.Four).getUnicodeCodePoint());
        assertEquals(0x1F0D5, new StandardCard(suite, FaceValue.Five).getUnicodeCodePoint());
        assertEquals(0x1F0D6, new StandardCard(suite, FaceValue.Six).getUnicodeCodePoint());
        assertEquals(0x1F0D7, new StandardCard(suite, FaceValue.Seven).getUnicodeCodePoint());
        assertEquals(0x1F0D8, new StandardCard(suite, FaceValue.Eight).getUnicodeCodePoint());
        assertEquals(0x1F0D9, new StandardCard(suite, FaceValue.Nine).getUnicodeCodePoint());
        assertEquals(0x1F0DA, new StandardCard(suite, FaceValue.Ten).getUnicodeCodePoint());
        assertEquals(0x1F0DB, new StandardCard(suite, FaceValue.Jack).getUnicodeCodePoint());
        assertEquals(0x1F0DD, new StandardCard(suite, FaceValue.Queen).getUnicodeCodePoint());
        assertEquals(0x1F0DE, new StandardCard(suite, FaceValue.King).getUnicodeCodePoint());
    }

    @Test
    void shouldSetCorrectUnicodeCharactersForDiamonds() {
        assertEquals(13, FaceValue.values().length);
        Suite suite = Suite.Diamond;
        assertEquals(0x1F0C1, new StandardCard(suite, FaceValue.Ace).getUnicodeCodePoint());
        assertEquals(0x1F0C2, new StandardCard(suite, FaceValue.Two).getUnicodeCodePoint());
        assertEquals(0x1F0C3, new StandardCard(suite, FaceValue.Three).getUnicodeCodePoint());
        assertEquals(0x1F0C4, new StandardCard(suite, FaceValue.Four).getUnicodeCodePoint());
        assertEquals(0x1F0C5, new StandardCard(suite, FaceValue.Five).getUnicodeCodePoint());
        assertEquals(0x1F0C6, new StandardCard(suite, FaceValue.Six).getUnicodeCodePoint());
        assertEquals(0x1F0C7, new StandardCard(suite, FaceValue.Seven).getUnicodeCodePoint());
        assertEquals(0x1F0C8, new StandardCard(suite, FaceValue.Eight).getUnicodeCodePoint());
        assertEquals(0x1F0C9, new StandardCard(suite, FaceValue.Nine).getUnicodeCodePoint());
        assertEquals(0x1F0CA, new StandardCard(suite, FaceValue.Ten).getUnicodeCodePoint());
        assertEquals(0x1F0CB, new StandardCard(suite, FaceValue.Jack).getUnicodeCodePoint());
        assertEquals(0x1F0CD, new StandardCard(suite, FaceValue.Queen).getUnicodeCodePoint());
        assertEquals(0x1F0CE, new StandardCard(suite, FaceValue.King).getUnicodeCodePoint());
    }

    @Test
    void shouldSetCorrectUnicodeCharactersForHearts() {
        assertEquals(13, FaceValue.values().length);
        Suite suite = Suite.Heart;
        assertEquals(0x1F0B1, new StandardCard(suite, FaceValue.Ace).getUnicodeCodePoint());
        assertEquals(0x1F0B2, new StandardCard(suite, FaceValue.Two).getUnicodeCodePoint());
        assertEquals(0x1F0B3, new StandardCard(suite, FaceValue.Three).getUnicodeCodePoint());
        assertEquals(0x1F0B4, new StandardCard(suite, FaceValue.Four).getUnicodeCodePoint());
        assertEquals(0x1F0B5, new StandardCard(suite, FaceValue.Five).getUnicodeCodePoint());
        assertEquals(0x1F0B6, new StandardCard(suite, FaceValue.Six).getUnicodeCodePoint());
        assertEquals(0x1F0B7, new StandardCard(suite, FaceValue.Seven).getUnicodeCodePoint());
        assertEquals(0x1F0B8, new StandardCard(suite, FaceValue.Eight).getUnicodeCodePoint());
        assertEquals(0x1F0B9, new StandardCard(suite, FaceValue.Nine).getUnicodeCodePoint());
        assertEquals(0x1F0BA, new StandardCard(suite, FaceValue.Ten).getUnicodeCodePoint());
        assertEquals(0x1F0BB, new StandardCard(suite, FaceValue.Jack).getUnicodeCodePoint());
        assertEquals(0x1F0BD, new StandardCard(suite, FaceValue.Queen).getUnicodeCodePoint());
        assertEquals(0x1F0BE, new StandardCard(suite, FaceValue.King).getUnicodeCodePoint());
    }

    @Test
    void shouldSetCorrectUnicodeCharactersForSpades() {
        assertEquals(13, FaceValue.values().length);
        Suite suite = Suite.Spade;
        assertEquals(0x1F0A1, new StandardCard(suite, FaceValue.Ace).getUnicodeCodePoint());
        assertEquals(0x1F0A2, new StandardCard(suite, FaceValue.Two).getUnicodeCodePoint());
        assertEquals(0x1F0A3, new StandardCard(suite, FaceValue.Three).getUnicodeCodePoint());
        assertEquals(0x1F0A4, new StandardCard(suite, FaceValue.Four).getUnicodeCodePoint());
        assertEquals(0x1F0A5, new StandardCard(suite, FaceValue.Five).getUnicodeCodePoint());
        assertEquals(0x1F0A6, new StandardCard(suite, FaceValue.Six).getUnicodeCodePoint());
        assertEquals(0x1F0A7, new StandardCard(suite, FaceValue.Seven).getUnicodeCodePoint());
        assertEquals(0x1F0A8, new StandardCard(suite, FaceValue.Eight).getUnicodeCodePoint());
        assertEquals(0x1F0A9, new StandardCard(suite, FaceValue.Nine).getUnicodeCodePoint());
        assertEquals(0x1F0AA, new StandardCard(suite, FaceValue.Ten).getUnicodeCodePoint());
        assertEquals(0x1F0AB, new StandardCard(suite, FaceValue.Jack).getUnicodeCodePoint());
        assertEquals(0x1F0AD, new StandardCard(suite, FaceValue.Queen).getUnicodeCodePoint());
        assertEquals(0x1F0AE, new StandardCard(suite, FaceValue.King).getUnicodeCodePoint());
    }
}