package bboreel.games.card.domain

/**
  * @author Robb Lee (robbmlee@gmail.com).
  */
object Utils {
  def createSetOfCardsForAllSuites(ordinalRange: Range): List[Card] = {
    Suite.values.toList.flatMap((suite: Suite.Value) =>
      ordinalRange.toList.map((ordinal: Int) => Card(suite, FaceValue(ordinal, ordinal.toString))))
  }
}
