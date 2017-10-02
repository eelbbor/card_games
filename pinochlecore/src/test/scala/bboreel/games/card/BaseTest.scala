package bboreel.games.card

import org.apache.logging.log4j.{LogManager, Logger}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, FunSuite}

/**
  * @author Robb Lee (robbmlee@gmail.com).
  */
@RunWith(classOf[JUnitRunner])
abstract class BaseTest extends FunSuite with BeforeAndAfterEach with BeforeAndAfterAll {
  val log: Logger = LogManager.getLogger(this.getClass())
}
