package sclaSnake.model

import java.util.concurrent.TimeUnit

import scala.concurrent.duration.FiniteDuration


case class GameSpeed(makeMoveAfterRefreshes: Int, per: FiniteDuration, refreshCounter: Int) {
  def this(makeMoveAfterRefreshes: Int, per: FiniteDuration) = this(makeMoveAfterRefreshes, per, 0)

  def canMakeMoveAfterRefresh: (Boolean, GameSpeed) = {
    if (refreshCounter >= makeMoveAfterRefreshes) {
      (true, copy(refreshCounter = 1))
    } else {
      (false, copy(refreshCounter = refreshCounter + 1))
    }
  }

  def increaseSpeedByOneReward: GameSpeed = {
    val newSpeed = makeMoveAfterRefreshes - 7
    copy(makeMoveAfterRefreshes = if (newSpeed < 10) 10 else newSpeed)
  }

}

object GameSpeed {
  val refreshDuration = FiniteDuration(10, TimeUnit.MILLISECONDS)

  val slowSpeed = new GameSpeed(100, refreshDuration)
  val mediumSpeed = new GameSpeed(50, refreshDuration)
  val insaneSpeed = new GameSpeed(10, refreshDuration)
}
