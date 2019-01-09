package scalaSnake.model

import pl.pgizka.scalaSnake.model.BoardMoveResult
import scalaSnake.rng.RNG

case class Board(snake: Snake, snake2: Option[Snake], rewards: Seq[Reward], rng: RNG) {

  def move(newDirection: Option[Direction], secondPlayerNewDirection: Option[Direction])
          (implicit config: Config): Either[GameOverEvent, BoardMoveResult] = {
    val (newSnake, collectedReward) = snake.move(newDirection, rewards)
    if (newSnake.isCollision) {
      scala.util.Left(GameOverEvent())
    } else {
//
      val newRewards = removeReward(collectedReward, rewards)

      val result = snake2.map(_.move(secondPlayerNewDirection, newRewards))

      if (result.isDefined) {
        val (secondNewSnake, secondCollectedReward) = result.get

        val firstSnakeCrash = newSnake.isCollisionIn(secondNewSnake)
        val secondSnakeCrash = secondNewSnake.isCollisionIn(newSnake)

        if (secondNewSnake.isCollision || firstSnakeCrash || secondSnakeCrash) {
          scala.util.Left(GameOverEvent())
        } else {
          val newBoard = placeNecessaryRewards(newSnake, Some(secondNewSnake), removeReward(secondCollectedReward, newRewards))
          scala.util.Right(BoardMoveResult(newBoard, collectedReward, secondCollectedReward))
        }
      } else {
        val newBoard = placeNecessaryRewards(newSnake, snake2, newRewards)
        scala.util.Right(BoardMoveResult(newBoard, collectedReward, None))
      }

    }
  }


  def removeReward(rewardOption: Option[Reward], rewards: Seq[Reward]): Seq[Reward] = {
    rewardOption.map(reward => rewards.filterNot(_.equals(reward))).getOrElse(rewards)
  }


  def placeNecessaryRewards(newSnake: Snake, newSecondSnake: Option[Snake], rewards: Seq[Reward])
                           (implicit config: Config): Board = {
    val rewardsToPlace = 3 - rewards.size

    val occupiedBlocks =
      snake.getBlockIds ++
      newSecondSnake.map(_.getBlockIds).getOrElse(Seq.empty) ++
      rewards.map(_.position.id)

    val (newRewards, newRng) = 1.to(rewardsToPlace).foldLeft((Seq.empty[Reward], rng)) {
      case ((currentRewards, currentRng), c) =>
        val (newReward, newRng) = Reward.fromOccupiedBlocks(occupiedBlocks ++ currentRewards.map(_.position.id), currentRng)
        (currentRewards :+ newReward, newRng)
    }

    Board(newSnake, newSecondSnake, newRewards ++ rewards, newRng)
  }
}

object Board {



  def initialBoard(snake: Snake, secondSnake: Option[Snake], rng: RNG)(implicit config: Config): Board = {
    Board(snake, secondSnake, Seq(), rng).placeNecessaryRewards(snake, secondSnake, Seq())
  }
}
