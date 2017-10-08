package bboreel.games.card.domain.pinochle

import bboreel.games.card.domain.{Card, FaceValue, Suite}

/**
  * @author Robb Lee (robbmlee@gmail.com).
  */
class PinochleCard(suite: Suite.Value, value: PinochleFaceValue.Value) extends
  Card (suite = suite, value = FaceValue(value.id, value.toString))
