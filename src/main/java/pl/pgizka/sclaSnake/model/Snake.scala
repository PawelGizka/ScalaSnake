package pl.pgizka.sclaSnake.model

import pl.pgizka.sclaSnake.Config


case class Snake(blockPositions: Map[Int, Block], headIndex: Int, tailIndex: Int, currentDirection: Direction) {

  def move(newDirection: Option[Direction], rewards: Seq[Reward]): (Snake, Seq[Reward]) = {
    val headBlock = blockPositions(headIndex)
    val validMove = getValidMove(newDirection.getOrElse(currentDirection), currentDirection)
    val newHeadBlock = headBlock.move(validMove)

    val rewardOption = getReward(newHeadBlock, rewards)

    val newRewards = if (rewardOption.isDefined) {
      val reward = rewardOption.get
      rewards.filterNot(_.equals(reward))
    } else {
      rewards
    }

    val newTailIndex = if (rewardOption.isDefined) tailIndex else tailIndex + 1

    val newPositions = if (rewardOption.isDefined) {
      blockPositions.+((headIndex + 1) -> newHeadBlock)
    } else {
      blockPositions.-(tailIndex).+((headIndex + 1) -> newHeadBlock)
    }

    (Snake(newPositions, headIndex + 1, newTailIndex, validMove), newRewards)
  }

  def getValidMove(newDirection: Direction, previousDirection: Direction): Direction =
    (newDirection, previousDirection) match {
    case (Up, Down) => previousDirection
    case (Left, Right) => previousDirection
    case (Right, Left) => previousDirection
    case (Down, Up) => previousDirection
    case (_, _) => newDirection
  }

  def getReward(newHeadBlock: Block, rewards: Seq[Reward]): Option[Reward] = {
    rewards.foldLeft[Option[Reward]](None)((result, reward) => {
      if (reward.position.equals(newHeadBlock)) {
        Some(reward)
      } else {
        result
      }
    })
  }

  def getBlockIds(implicit config: Config): Seq[Int] =
    (tailIndex to headIndex).map(blockPositions(_).id)
}

object Snake {

  val initialSnake: Snake = {
    val map = Map(0 -> Block(0, 0), 1 -> Block(1, 0), 2 -> Block(2, 0))
    new Snake(map, 2, 0, Right)
  }
}
