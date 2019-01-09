package scalaSnake.model


case class Snake(blockPositions: Map[Int, Block], headIndex: Int, tailIndex: Int, currentDirection: Direction) {

  def move(newDirection: Option[Direction], rewards: Seq[Reward])
          (implicit config: Config): (Snake, Option[Reward]) = {
    val headBlock = blockPositions(headIndex)
    val validMove = getValidMove(newDirection.getOrElse(currentDirection))
    val newHeadBlock = headBlock.move(validMove)


    makeMove(newHeadBlock, rewards, validMove)
  }

  def getValidMove(newDirection: Direction): Direction =
    (newDirection, currentDirection) match {
      case (Up, Down) => Down
      case (Left, Right) => Right
      case (Right, Left) => Left
      case (Down, Up) => Up
      case (_, _) => newDirection
    }

  val isCollision: Boolean = {
    val headBlock = blockPositions(headIndex)
    tailIndex.until(headIndex).foldLeft(false)((collision, key) => collision || blockPositions(key).equals(headBlock))
  }

  def isCollisionIn(other: Snake)(implicit config: Config): Boolean = {
    val headBlock = blockPositions(headIndex)
    other.tailIndex.until(other.headIndex).foldLeft(false)((collision, key) => collision || other.blockPositions(key).equals(headBlock))
  }

  def makeMove(newHeadBlock: Block, rewards: Seq[Reward], validMove: Direction): (Snake, Option[Reward]) = {
    val rewardOption = getReward(newHeadBlock, rewards)

    val newTailIndex = if (rewardOption.isDefined) tailIndex else tailIndex + 1

    val newPositions = if (rewardOption.isDefined) {
      blockPositions.+((headIndex + 1) -> newHeadBlock)
    } else {
      blockPositions.-(tailIndex).+((headIndex + 1) -> newHeadBlock)
    }

    (Snake(newPositions, headIndex + 1, newTailIndex, validMove), rewardOption)
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

  def initialSnake(direction: Direction = Right, blockCenter: Block)(implicit config: Config): Snake = {
    val blocks = direction match {
      case Up => Map(
        0 -> blockCenter.move(Down),
        1 -> blockCenter,
        2 -> blockCenter.move(Up))

      case Down => Map(
        0 -> blockCenter.move(Up),
        1 -> blockCenter,
        2 -> blockCenter.move(Down))

      case Left => Map(
        0 -> blockCenter.move(Right),
        1 -> blockCenter,
        2 -> blockCenter.move(Left))

      case Right => Map(
        0 -> blockCenter.move(Left),
        1 -> blockCenter,
        2 -> blockCenter.move(Right))
    }

    new Snake(blocks, 2, 0, direction)
  }
}
