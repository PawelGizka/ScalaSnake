package pl.pgizka.scalaSnake.model

import org.scalatest.EitherValues._
import org.scalatest.OptionValues._
import org.scalatest.{GivenWhenThen, MustMatchers, WordSpec}

import scalaSnake.model._

class SnakeTest extends WordSpec with MustMatchers with GivenWhenThen {

  implicit val config = Config(20, 20, 20, GameDifficulty.easy, multiplayer = false)
  val blockCenter = Block.blockCenter

  "A snake" should {
    "only go in possible direction" in {
      Given("snake going Up | Down | Left | Right")

      val snakeUp = Snake.initialSnake(Up, blockCenter)
      val snakeDown = Snake.initialSnake(Down, blockCenter)
      val snakeLeft = Snake.initialSnake(Left, blockCenter)
      val snakeRight = Snake.initialSnake(Right, blockCenter)

      When("new direction is Down | Up | Right | Left")
      Then("snake should still go Up | Down | Left | Right")
      snakeUp.move(Some(Down), Seq.empty)._1.currentDirection mustBe Up
      snakeDown.move(Some(Up), Seq.empty)._1.currentDirection mustBe Down
      snakeLeft.move(Some(Right), Seq.empty)._1.currentDirection mustBe Left
      snakeRight.move(Some(Left), Seq.empty)._1.currentDirection mustBe Right

      When("new direction is other than Down | Up | Right | Left")
      Then("snake should got to that direction")
      snakeUp.move(Some(Left), Seq.empty)._1.currentDirection mustBe Left
      snakeDown.move(Some(Right), Seq.empty)._1.currentDirection mustBe Right
      snakeLeft.move(Some(Down), Seq.empty)._1.currentDirection mustBe Down
      snakeRight.move(Some(Up), Seq.empty)._1.currentDirection mustBe Up
    }

    "move itself" in {
      val snakeUp = Snake.initialSnake(Up, blockCenter)

      val snakeMoved = snakeUp.move(Some(Up), Seq.empty)._1

      snakeMoved.currentDirection mustBe Up
      snakeMoved.headIndex must equal(3)
      snakeMoved.tailIndex must equal(1)
      snakeMoved.blockPositions.size must equal(3)
    }

    "detect collision" in {
      TestUtils.collisionSnake.move(Some(Left), Seq.empty)._1.isCollision mustBe true
    }

    "collect rewards and elongate itself" in {
      val snakeUp = Snake.initialSnake(Up, blockCenter)
      val headBlock = snakeUp.blockPositions(snakeUp.headIndex)
      val rewardBlock = headBlock.move(Up)
      val reward = Reward(rewardBlock, 100)

      val (snakeMoved, collectedReward) = snakeUp.move(Some(Up), Seq(reward))

      collectedReward.value mustBe reward

      snakeMoved.headIndex must equal(3)
      snakeMoved.tailIndex must equal(0)
      snakeMoved.blockPositions.size must equal(4)
      snakeMoved.blockPositions(snakeMoved.headIndex) must equal(rewardBlock)
      snakeMoved.blockPositions(snakeMoved.tailIndex) must equal(snakeUp.blockPositions(snakeUp.tailIndex))
    }
  }

}
