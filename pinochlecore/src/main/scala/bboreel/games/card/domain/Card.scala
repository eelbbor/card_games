package bboreel.games.card.domain

/**
  * Generic card definition based on a traditional suite definition [[Suite]] and corresponding
  * face value [[FaceValue]]. This class enables ordering of cards based on the [[Suite]] and
  * [[FaceValue]] of the card.
  *
  * @param suite one of the suite definitions from the [[Suite]] enum.
  * @param value dynamically defined [[FaceValue]] object.
  * @author Robb Lee (robbmlee@gmail.com).
  */
case class Card(suite: Suite.Value, value: FaceValue) extends Ordered[Card] {
  /**
    * Enables ordering and card comparison based on suite then by value.
    *
    * NOTE: This ordering is focused on a behavior central to sorting a collection of cards by
    * suite and then value, but not necessarily for determining if the value should defeat
    * another card in a game.
    *
    * @param that [[Card]] to compare against.
    * @return [[Int]] implying where the card belongs in a sort order.
    */
  override def compare(that: Card): Int = {
    (this.suite, this.value) compare(that.suite, that.value)
  }
}

