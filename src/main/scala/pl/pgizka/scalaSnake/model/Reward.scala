package scalaSnake.model

import scalaSnake.rng.RNG

case class Reward(position: Block, value: Int = 0)

object Reward {
  def fromIdAndRng(blockId: Int, rng: RNG)(implicit config: Config): (Reward, RNG) = {
    val (rewardValue, newRng) = RNG.nonNegativeLessThan(100)(rng)
    val reward = new Reward(Block.fromId(blockId), rewardValue)
    (reward, newRng)
  }

  def fromOccupiedBlocks(occupiedBlocksIds: Seq[Int], rng: RNG)(implicit config: Config): (Reward, RNG) = {
    val freeBlocks = config.boardSize - occupiedBlocksIds.size

    val (random, rng1) = RNG.nonNegativeLessThan(freeBlocks)(rng)

    val freeBlockId = occupiedBlocksIds.sorted.foldLeft(random)((random, id) => if (id <= random) random + 1 else random)

    Reward.fromIdAndRng(freeBlockId, rng1)
  }
}
