package sclaSnake.model

import sclaSnake.rng.RNG

case class Reward(position: Block, value: Int = 0)

object Reward {
  def fromIdAndRng(blockId: Int, rng: RNG)(implicit config: Config): (Reward, RNG) = {
    val (rewardValue, newRng) = RNG.nonNegativeLessThan(100)(rng)
    val reward = new Reward(Block.fromId(blockId), rewardValue)
    (reward, newRng)
  }
}
