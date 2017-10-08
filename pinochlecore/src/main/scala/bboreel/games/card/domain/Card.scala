package bboreel.games.card.domain

/**
  * @author Robb Lee (robbmlee@gmail.com).
  */
case class Card(suite: Suite.Value, value: FaceValue) extends Ordered[Card] {
//  private val this.suite = suite
//  private val this.value = value

  override def compare(that: Card): Int = {
    (this.suite, this.value) compare(that.suite, that.value)
  }
}

