package pl.pgizka.scalaSnake.model

import org.scalatest.{MustMatchers, WordSpec}
import org.scalatest.EitherValues._
import org.scalatest.OptionValues._

import scalaSnake.model._
import scalaSnake.rng.RNG

class BoardTest extends WordSpec with MustMatchers {

  implicit val config = Config(20, 20, 20, GameDifficulty.easy, multiplayer = false)

  val snakeGoingRight: Snake = Snake.initialSnake(Right, Block.blockCenter)
  val rng: RNG.Simple = RNG.Simple(100)

  "A Board" should {
    "place new reward when snake collected one" in {
      val headBlock = snakeGoingRight.blockPositions(snakeGoingRight.headIndex)
      val reward = Reward(headBlock.move(Right), 100)
      val board = Board(snakeGoingRight, None, Seq(reward), rng)

      val boardMoveResult = board.move(Some(Right), None).right.value
      boardMoveResult.reward.value mustBe reward

      boardMoveResult.board.rewards.size mustEqual 3
      boardMoveResult.board.snake must not equal snakeGoingRight
      boardMoveResult.board.rng must not equal rng
    }
  }

  "A Board" can {
    "be created from snake and Rng" in {
      val board = Board.initialBoard(snakeGoingRight, None, rng)

      board.snake mustBe snakeGoingRight
      board.rewards.size mustEqual 3
      board.rng must not equal rng
    }
  }

}
