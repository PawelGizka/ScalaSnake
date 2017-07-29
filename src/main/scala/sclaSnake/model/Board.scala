package sclaSnake.model

import sclaSnake.Config
import sclaSnake.rng.RNG


case class Board(snake: Snake, rewards: Seq[Reward], rng: RNG) {

  def this(snake: Snake, rng: RNG) = {
    this(snake, List(), rng)
  }

  def move(newDirection: Option[Direction])(implicit config: Config): Either[GameOverEvent, (Board, Option[Reward])] = {
    snake.move(newDirection, rewards).map{ case (newSnake, rewardOption) =>
      if (rewardOption.isDefined) {
        val reward = rewardOption.get
        val board = placeReward(newSnake, rewards.filterNot(_.equals(reward)))
        (board, rewardOption)
      } else {
        (Board(newSnake, rewards, rng), None)
      }
    }
  }

  def placeReward(snake: Snake, rewards: Seq[Reward])(implicit config: Config): Board = {
    val occupiedBlocksIds = (snake.getBlockIds ++ rewards.map(_.position.id)).sorted
    val freeBlocks = config.boardSize - occupiedBlocksIds.size

    val (random, newRng) = RNG.nonNegativeLessThan(freeBlocks)(rng)

    val freeBlockId = occupiedBlocksIds.foldLeft(random)((random, id) => if (id <= random) random + 1 else random)

    val newReward = Reward(Block.fromId(freeBlockId), 0)

    Board(snake, rewards.+:(newReward), newRng)
  }
}

object Board {
  def initialBoard(snake: Snake, rng: RNG)(implicit config: Config): Board = {
    //TODO refactor it
    val empty = Board(snake, Seq(), rng)
    val one = empty.placeReward(snake, empty.rewards)
    val two = one.placeReward(snake, one.rewards)
    val three = two.placeReward(snake, two.rewards)
    three
  }
}
