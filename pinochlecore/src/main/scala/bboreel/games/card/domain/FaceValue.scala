package bboreel.games.card.domain

/**
  * @author Robb Lee (robbmlee@gmail.com).
  */
case class FaceValue(ordinal: Int, value: String) extends Ordered[FaceValue] {
  val this.ordinal = ordinal
  val this.value = value

  override def compare(that: FaceValue): Int = {
    val ordinalCompare = this.ordinal compare that.ordinal
    if (ordinalCompare == 0) this.value compare that.value else ordinalCompare
  }
}
