package org.eelbbor.pinochle;

import static org.eelbbor.pinochle.PinochleFaceValue.Ace;
import static org.eelbbor.pinochle.PinochleFaceValue.Jack;
import static org.eelbbor.pinochle.PinochleFaceValue.King;
import static org.eelbbor.pinochle.PinochleFaceValue.Queen;

import org.eelbbor.carddeck.standard.Suite;

import java.util.Arrays;
import java.util.Map;

/**
 * Pinochle hand track cards in the hand and adds logic for calculating meld for a hand.
 *
 * @author Robb Lee (robbmlee@gmail.com).
 */
public class Hand extends org.eelbbor.carddeck.Hand<Card> {
  public static final Map<PinochleFaceValue, int[]> MELD_AROUND =
      Map.of(Jack, new int[] {0, 4, 40, 60, 80}, Queen, new int[] {0, 6, 60, 90, 120}, King,
          new int[] {0, 8, 80, 120, 160}, Ace, new int[] {0, 10, 100, 150, 200});

  public static final int[] PINOCHLE_MELD = new int[] {0, 4, 30, 60, 90};
  public static final int[] RUN_MELD = new int[] {0, 15, 150, 225, 300};

  public static final int MARRIAGE_MULTIPLIER = 2;

  private int[][] cardArray = new int[Suite.values().length][PinochleFaceValue.values().length];

  public Hand() {
    this.cardArray = new int[Suite.values().length][PinochleFaceValue.values().length];
  }

  @Override
  public boolean playCard(Card card) {
    boolean played = super.playCard(card);
    if (played) {
      cardArray[card.getSuite().ordinal()][card.getOrdinal()]--;
    }
    return played;
  }

  /**
   * Adds cards to the hand. If more than 4 of a given card type is added, the method throws an
   * illegal argument exception as it is not possible to have more than four of any card.
   *
   * @param card variable length number of cards to add to the hand.
   */
  @Override
  public void dealCard(Card... card) {
    super.dealCard(card);
    Arrays.stream(card).forEach(c -> {
      if (++cardArray[c.getSuite().ordinal()][c.getOrdinal()] > 4) {
        throw new IllegalArgumentException("Tried to deal more than 4 '"
            + c.getFaceValue().name() + "s' of '" + c.getSuite().name() + "s'.");
      }
    });

    if (numCards() > 20) {
      throw new IllegalArgumentException("Tried to add more than 20 cards to a hand.");
    }
  }

  /**
   * Computes the total meld of a hand based on the following set of values. The four values
   * indicate the total based on the number of occurrences.
   * <p/>
   * Arounds
   * Aces (An Ace in each suit):      10    100    150    200
   * Kings (A King in each suit):     8     80     120    160
   * Queens (A Queen in each suit):   6     60     90     120
   * Jacks (A Jack in each suit):     4     40     60     80
   * Note: A set of tens is not worth anything in meld.
   * <p/>
   * Pinochles
   * Pinochle(Jack of diamonds & Queen of spades):    4    30    60    90
   * <p/>
   * Marriages and Run
   * Marriage (Kings and Queen of the same suit, not trump):   2     4      6      8
   * Royal Marriage (King and Queen of trump):                 4     8      12     16
   * Run (Ace, Ten, King, Queen, Jack of trump):               15    150    225    300
   * Note: A run in a suit other than trumps is not worth anything more than the marriage score
   * for the king and queen. Additionally a Royal Marriage used in a run does not add to the meld
   * i.e.
   * Ace, Ten, King, Queen, Jack of trump = 15 NOT 19
   * Ace, Ten, King, King, Queen, Queen, Jack of trump = 19 due to the extra marriage.
   *
   * @param trump {@link Suite} declared as trump.
   * @return total meld for a hand with the given trump suite.
   */
  public int countMeld(Suite trump) {
    int total = 0;

    // Jacks around.
    total += MELD_AROUND.get(Jack)[getMinCardCountByValue(Jack)];

    // Queens around.
    total += MELD_AROUND.get(Queen)[getMinCardCountByValue(Queen)];

    // Kings around.
    total += MELD_AROUND.get(King)[getMinCardCountByValue(King)];

    // Aces around.
    total += MELD_AROUND.get(Ace)[getMinCardCountByValue(Ace)];

    // Pinochles.
    int jacks = getCardCount(Suite.Diamond, Jack);
    int queens = getCardCount(Suite.Spade, Queen);
    total += PINOCHLE_MELD[Math.min(jacks, queens)];

    // Marriages.
    int marriageTotal = Arrays.stream(Suite.values())
        .mapToInt(suite -> Math.min(getCardCount(suite, Queen), getCardCount(suite, King))).sum();

    int[] trumpCards = cardArray[trump.ordinal()];
    int royalMarriageCount = Math.min(trumpCards[King.ordinal()], trumpCards[Queen.ordinal()]);
    total += (marriageTotal + royalMarriageCount) * MARRIAGE_MULTIPLIER;

    // Runs.
    int runCount = Arrays.stream(trumpCards).min().getAsInt();
    total += RUN_MELD[runCount];
    // Defend against marriage as part of trump run.
    total -= runCount * MARRIAGE_MULTIPLIER * MARRIAGE_MULTIPLIER;

    return total;
  }

  /**
   * Returns the number of instances of the {@link Card} currently in the hand if any.
   *
   * @param card {@link Card} definition to query for.
   * @return number of instances in the hand or zero.
   */
  int getCardCount(Card card) {
    return getCardCount(card.getSuite(), card.getFaceValue());
  }

  private int getCardCount(Suite suite, PinochleFaceValue faceValue) {
    return cardArray[suite.ordinal()][faceValue.ordinal()];
  }

  private int getMinCardCountByValue(PinochleFaceValue faceValue) {
    int result = 4;
    for (int[] cardCountArray : cardArray) {
      result = Math.min(result, cardCountArray[faceValue.ordinal()]);
    }
    return result;
  }
}
