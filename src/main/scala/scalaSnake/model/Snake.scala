package scalaSnake.model


case class Snake(blockPositions: Map[Int, Block], headIndex: Int, tailIndex: Int, currentDirection: Direction) {

  def move(newDirection: Option[Direction], rewards: Seq[Reward])
          (implicit config: Config): Either[GameOverEvent, (Snake, Option[Reward])] = {
    val headBlock = blockPositions(headIndex)
    val validMove = getValidMove(newDirection.getOrElse(currentDirection), currentDirection)
    val newHeadBlock = headBlock.move(validMove)

    if (isCollision(newHeadBlock)) {
      scala.util.Left(GameOverEvent())
    } else {
      makeMove(newHeadBlock, rewards, validMove)
    }
  }

  def getValidMove(newDirection: Direction, previousDirection: Direction): Direction =
    (newDirection, previousDirection) match {
      case (Up, Down) => Down
      case (Left, Right) => Right
      case (Right, Left) => Left
      case (Down, Up) => Up
      case (_, _) => newDirection
    }

  def isCollision(headBlock: Block): Boolean =
    tailIndex.until(headIndex).foldLeft(false)((collision, key) => collision || blockPositions(key).equals(headBlock))

  def makeMove(newHeadBlock: Block, rewards: Seq[Reward], validMove: Direction): scala.util.Right[GameOverEvent, (Snake, Option[Reward])] = {
    val rewardOption = getReward(newHeadBlock, rewards)

    val newTailIndex = if (rewardOption.isDefined) tailIndex else tailIndex + 1

    val newPositions = if (rewardOption.isDefined) {
      blockPositions.+((headIndex + 1) -> newHeadBlock)
    } else {
      blockPositions.-(tailIndex).+((headIndex + 1) -> newHeadBlock)
    }

    scala.util.Right(Snake(newPositions, headIndex + 1, newTailIndex, validMove), rewardOption)
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
