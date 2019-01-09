package pl.pgizka.scalaSnake.model

import org.scalatest.{MustMatchers, WordSpec}

import scalaSnake.model.{Config, GameDifficulty, GameSpeed}

class GameSpeedTest extends WordSpec with MustMatchers {

  implicit val config = Config(20, 20, 20, GameDifficulty.easy, multiplayer = false)

  "A GameSpeed" should {
    val speed = new GameSpeed(3)

    "do not allow to make move when specified time did not elapsed" in {
      val (canMake2, speed2) = speed.canMakeMoveAfterRefresh
      canMake2 mustBe false

      val (canMake3, _) = speed2.canMakeMoveAfterRefresh
      canMake3 mustBe false
    }

    "allow to make move when specified time elapsed" in {
      val (canMake2, speed2) = speed.canMakeMoveAfterRefresh
      canMake2 mustBe false

      val (canMake3, speed3) = speed2.canMakeMoveAfterRefresh
      canMake3 mustBe false

      val (canMake4, speed4) = speed3.canMakeMoveAfterRefresh
      canMake4 mustBe true
    }

    "increase speed by GameDifficulty when it is not max speed for game difficulty" in {
      val fastSpeed = GameSpeed.fastSpeed
      val newGameSpeed = fastSpeed.increaseSpeedByGameDifficulty(GameDifficulty.hard)

      newGameSpeed.makeMoveAfterRefreshes must be < fastSpeed.makeMoveAfterRefreshes
    }

    "do not increase speed when it is max speed for game difficulty" in {
      val fastSpeed = GameDifficulty.hard.maxGameSpeed
      val newGameSpeed = fastSpeed.increaseSpeedByGameDifficulty(GameDifficulty.hard)

      newGameSpeed.makeMoveAfterRefreshes mustEqual fastSpeed.makeMoveAfterRefreshes
    }
  }

}
