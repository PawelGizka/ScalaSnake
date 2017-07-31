package scalaSnake.model

import java.util.concurrent.TimeUnit

import scala.concurrent.duration.FiniteDuration


case class GameSpeed(makeMoveAfterRefreshes: Int, per: FiniteDuration, refreshCounter: Int) {
  def this(makeMoveAfterRefreshes: Int) = this(makeMoveAfterRefreshes, GameSpeed.refreshDuration, 0)

  def canMakeMoveAfterRefresh: (Boolean, GameSpeed) = {
    if (refreshCounter >= makeMoveAfterRefreshes) {
      (true, copy(refreshCounter = 1))
    } else {
      (false, copy(refreshCounter = refreshCounter + 1))
    }
  }

  def increaseSpeedByGameLevel(gameDifficulty: GameDifficulty): GameSpeed = {
    val newSpeed = (makeMoveAfterRefreshes * gameDifficulty.speedMultiplier).toInt
    val maxRefreshes = gameDifficulty.maxGameSpeed.makeMoveAfterRefreshes
    val maxSpeed = if (newSpeed < maxRefreshes) maxRefreshes else newSpeed
    copy(makeMoveAfterRefreshes = maxSpeed)
  }

}

object GameSpeed {
  val refreshDuration = FiniteDuration(10, TimeUnit.MILLISECONDS)

  val slowSpeed = new GameSpeed(100)
  val mediumSpeed = new GameSpeed(50)
  val fastSpeed = new GameSpeed(20)
  val insaneSpeed = new GameSpeed(5)
}
