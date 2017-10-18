package bboreel.games.card

import java.util.UUID

import bboreel.games.card.domain.{Card, FaceValue, Suite}

import scala.util.Random

/**
  * @author Robb Lee (robbmlee@gmail.com).
  */
object TestUtils {
  final val MaxCardValue = 20
  final val random: Random = new Random()

  def randomString(): String = {
    UUID.randomUUID().toString.replaceAll("-", "")
  }

  def randomPositiveInteger(max: Int = Int.MaxValue, min: Int = 0): Int = {
    var result = min - 1
    while (result < min) {
      result = random.nextInt(max)
    }
    result
  }

  def randomSuite(): Suite.Value = {
    val values = Suite.values.toArray
    values(randomPositiveInteger(values.size))
  }

  def createVectorOfCardsForAllSuites(ordinalRange: Range): Vector[Card] = {
    Suite.values.toVector.flatMap((suite: Suite.Value) => ordinalRange.toVector.map(
      (ordinal: Int) => Card(suite, FaceValue(ordinal, ordinal.toString))))
  }
}
