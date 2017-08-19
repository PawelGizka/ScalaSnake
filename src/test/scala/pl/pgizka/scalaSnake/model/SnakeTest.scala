package pl.pgizka.scalaSnake.model

import org.scalatest.EitherValues._
import org.scalatest.OptionValues._
import org.scalatest.{GivenWhenThen, MustMatchers, WordSpec}

import scalaSnake.model._

class SnakeTest extends WordSpec with MustMatchers with GivenWhenThen {

  implicit val config = Config(20, 20, 20, GameDifficulty.easy)

  "A snake" should {
    "only go in possible direction" in {
      Given("snake going Up | Down | Left | Right")

      val snakeUp = Snake.initialSnake(Up)
      val snakeDown = Snake.initialSnake(Down)
      val snakeLeft = Snake.initialSnake(Left)
      val snakeRight = Snake.initialSnake(Right)

      When("new direction is Down | Up | Right | Left")
      Then("snake should still go Up | Down | Left | Right")
      snakeUp.move(Some(Down), Seq.empty).right.value._1.currentDirection mustBe Up
      snakeDown.move(Some(Up), Seq.empty).right.value._1.currentDirection mustBe Down
      snakeLeft.move(Some(Right), Seq.empty).right.value._1.currentDirection mustBe Left
      snakeRight.move(Some(Left), Seq.empty).right.value._1.currentDirection mustBe Right

      When("new direction is other than Down | Up | Right | Left")
      Then("snake should got to that direction")
      snakeUp.move(Some(Left), Seq.empty).right.value._1.currentDirection mustBe Left
      snakeDown.move(Some(Right), Seq.empty).right.value._1.currentDirection mustBe Right
      snakeLeft.move(Some(Down), Seq.empty).right.value._1.currentDirection mustBe Down
      snakeRight.move(Some(Up), Seq.empty).right.value._1.currentDirection mustBe Up
    }

    "move itself" in {
      val snakeUp = Snake.initialSnake(Up)

      val snakeMoved = snakeUp.move(Some(Up), Seq.empty).right.value._1

      snakeMoved.currentDirection mustBe Up
      snakeMoved.headIndex must equal(3)
      snakeMoved.tailIndex must equal(1)
      snakeMoved.blockPositions.size must equal(3)
    }

    "detect collision" in {
      val blocks = Map(
        0 -> Block(0, 0),
        1 -> Block(0, 1),
        2 -> Block(0, 2),
        3 -> Block(1, 2),
        4 -> Block(1, 1))

      val longSnake = Snake(blocks, 4, 0, Up)

      longSnake.move(Some(Left), Seq.empty).left.value mustBe GameOverEvent()
    }

    "collect rewards and elongate itself" in {
      val snakeUp = Snake.initialSnake(Up)
      val headBlock = snakeUp.blockPositions(snakeUp.headIndex)
      val rewardBlock = headBlock.move(Up)
      val reward = Reward(rewardBlock, 100)

      val (snakeMoved, collectedReward) = snakeUp.move(Some(Up), Seq(reward)).right.value

      collectedReward.value mustBe reward

      snakeMoved.headIndex must equal(3)
      snakeMoved.tailIndex must equal(0)
      snakeMoved.blockPositions.size must equal(4)
      snakeMoved.blockPositions(snakeMoved.headIndex) must equal(rewardBlock)
      snakeMoved.blockPositions(snakeMoved.tailIndex) must equal(snakeUp.blockPositions(snakeUp.tailIndex))
    }
  }

}
