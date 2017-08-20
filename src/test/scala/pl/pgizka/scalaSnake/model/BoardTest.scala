package pl.pgizka.scalaSnake.model

import org.scalatest.{MustMatchers, WordSpec}
import org.scalatest.EitherValues._
import org.scalatest.OptionValues._

import scalaSnake.model._
import scalaSnake.rng.RNG

class BoardTest extends WordSpec with MustMatchers {

  implicit val config = Config(20, 20, 20, GameDifficulty.easy)

  val snakeGoingRight: Snake = Snake.initialSnake(Right)
  val rng: RNG.Simple = RNG.Simple(100)

  "A Board" should {
    "place new reward when snake collected one" in {
      val headBlock = snakeGoingRight.blockPositions(snakeGoingRight.headIndex)
      val reward = Reward(headBlock.move(Right), 100)
      val board = Board(snakeGoingRight, Seq(reward), rng)

      val (newBoard, rewardOption) = board.move(Some(Right)).right.value
      rewardOption.value mustBe reward

      newBoard.rewards.size mustEqual 1
      newBoard.snake must not equal snakeGoingRight
      newBoard.rng must not equal rng
    }
  }

  "A Board" can {
    "be created from snake and Rng" in {
      val board = Board.initialBoard(snakeGoingRight, rng)

      board.snake mustBe snakeGoingRight
      board.rewards.size mustEqual 3
      board.rng must not equal rng
    }
  }

}
