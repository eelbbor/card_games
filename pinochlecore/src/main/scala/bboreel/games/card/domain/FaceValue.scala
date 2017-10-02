package bboreel.games.card.domain

/**
  * @author Robb Lee (robbmlee@gmail.com).
  */
case class FaceValue(ordinal: Int, faceValue: String) extends Ordered[FaceValue] {
  val this.ordinal = ordinal
  val this.faceValue = faceValue

  override def compare(that: FaceValue): Int = {
    this.ordinal compare that.ordinal
  }
}
