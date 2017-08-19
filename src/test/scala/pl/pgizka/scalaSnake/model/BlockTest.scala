package pl.pgizka.scalaSnake.model

import org.scalatest.{MustMatchers, WordSpec}

import scalaSnake.model._

class BlockTest extends WordSpec with MustMatchers {

  def beMoved = afterWord("be moved")

  implicit val config = Config(20, 20, 20, GameDifficulty.easy)

  "A Block" should beMoved {

    "always by one position when it is not near the edge" when {
        val block = Block(10, 10)

        "up" in {
          block.move(Up) mustBe Block(10, 9)
        }

        "down" in {
          block.move(Down) mustBe Block(10, 11)
        }

        "left" in {
          block.move(Left) mustBe Block(9, 10)
        }

        "right" in {
          block.move(Right) mustBe Block(11, 10)
        }
      }

    "to the other side when it is near the up edge and moved to up" in {
      Block(10, 0).move(Up) mustBe Block(10, 19)
    }

    "to the other side when it is near the bottom edge and moved to bottom" in {
      Block(10, 19).move(Down) mustBe Block(10, 0)
    }

    "to the other side when it is near the left edge and moved to left" in {
      Block(0, 10).move(Left) mustBe Block(19, 10)
    }

    "to the other side when it is near the right edge and moved to right" in {
      Block(19, 10).move(Right) mustBe Block(0, 10)
    }
  }

  "A Block" can {
    "be created from id" in {
      val block = Block(7, 12)
      Block.fromId(block.id) mustBe block
    }

  }

}
