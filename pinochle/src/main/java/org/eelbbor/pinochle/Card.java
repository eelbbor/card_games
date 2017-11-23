package org.eelbbor.pinochle;

import org.eelbbor.carddeck.standard.StandardCard;
import org.eelbbor.carddeck.standard.StandardFaceValue;
import org.eelbbor.carddeck.standard.Suite;

/**
 * Definition of a card for playing pinochle. The card is based on traditional French playing
 * cards used in the game.
 *
 * @author Robb Lee (robbmlee@gmail.com).
 */
public class Card extends StandardCard {
  private PinochleFaceValue faceValue;

  /**
   * Constructor defining {@link Card} based on a {@link Suite} and {@link PinochleFaceValue}
   * definition.
   *
   * @param suite {@link Suite} the card belongs to.
   * @param pinochleFaceValue {@link PinochleFaceValue} the face value on the card.
   */
  public Card(Suite suite, PinochleFaceValue pinochleFaceValue) {
    super(suite, pinochleFaceValue.getStandardFaceValue());
    faceValue = pinochleFaceValue;
  }

  /**
   * Override to alter sorting to pinochle conventions based on the {@link PinochleFaceValue}
   * ordinal values as opposed to the {@link StandardFaceValue} definition
   * used in the {@link StandardCard}.
   *
   * @return int value indicating relative position
   */
  @Override
  public int getOrdinal() {
    return faceValue.ordinal();
  }

  /**
   * Returns the {@link PinochleFaceValue} defining the card.
   *
   * @return {@link PinochleFaceValue} card is defined by.
   */
  public PinochleFaceValue getFaceValue() {
    return faceValue;
  }

  @Override
  public String toString() {
    return getFaceValue().name() + " of " + getSuite().name();
  }
}
