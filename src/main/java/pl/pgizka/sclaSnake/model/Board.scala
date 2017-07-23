package pl.pgizka.sclaSnake.model

import pl.pgizka.sclaSnake.Config
import pl.pgizka.sclaSnake.rng.RNG


case class Board(snake: Snake, rewards: Seq[Reward], rng: RNG) {

  def this(snake: Snake, rng: RNG) = {
    this(snake, List(), rng)
  }

  def move(newDirection: Option[Direction])(implicit config: Config): Board = {
    val (newSnake, newRewards) = snake.move(newDirection, rewards)

    if (newRewards.size < 3) {
      val occupiedBlocksIds = (snake.getBlockIds ++ rewards.map(_.position.id)).sorted
      val freeBlocks = config.boardSize - occupiedBlocksIds.size

      val (random, newRng) = RNG.nonNegativeLessThan(freeBlocks)(rng)

      val freeBlockId = occupiedBlocksIds.foldLeft(random)((random, id) => if (id <= random) random + 1 else random)

      val newReward = Reward(Block.fromId(freeBlockId), 0)

      Board(newSnake, newRewards.+:(newReward), newRng)
    } else {
      Board(newSnake, newRewards, rng)
    }
  }

}
