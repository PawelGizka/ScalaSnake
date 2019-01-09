package pl.pgizka.scalaSnake.model

import org.scalatest.{MustMatchers, WordSpec}

import scalaSnake.model.{Block, Config, GameDifficulty, Reward}
import scalaSnake.rng.RNG

class RewardTest extends WordSpec with MustMatchers {

  implicit val config = Config(20, 20, 20, GameDifficulty.easy, multiplayer = false)

  "A Reward" can {
    "be created from id and Rng" in {
      val block = Block(8, 13)
      val rng = RNG.Simple(100)

      val (reward, newRng) = Reward.fromIdAndRng(block.id, rng)

      reward.position mustBe block

      reward.value must be >= 0
      reward.value must be <= 100
    }
  }

}
