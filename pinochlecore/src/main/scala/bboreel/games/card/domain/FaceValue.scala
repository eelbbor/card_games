package bboreel.games.card.domain

/**
  * Defines the face value of a card indicating the relative strength and value for a card.
  *
  * @param ordinal [[Int]] defining relative power.
  * @param value [[String]] indicating what would be on the card; i.e. Jack or J
  * @author Robb Lee (robbmlee@gmail.com).
  */
case class FaceValue(ordinal: Int, value: String) extends Ordered[FaceValue] {
  /**
    * Enables ordering and card comparison based on ordinal then by string value.
    *
    * @param that [[FaceValue]] to compare against.
    * @return [[Int]] implying where the card belongs in a sort order.
    */
  override def compare(that: FaceValue): Int = {
    val ordinalCompare = this.ordinal compare that.ordinal
    if (ordinalCompare == 0) this.value compare that.value else ordinalCompare
  }
}
