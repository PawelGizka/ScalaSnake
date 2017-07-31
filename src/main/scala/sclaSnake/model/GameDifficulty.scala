package sclaSnake.model


case class GameDifficulty(name: String, initialGameSpeed: GameSpeed, maxGameSpeed: GameSpeed,
                          speedMultiplier: Double, scoreMultiplier: Int) {
  override def toString: String = name
}

object GameDifficulty {
  val easy = GameDifficulty("Easy", GameSpeed.slowSpeed, new GameSpeed(20), 0.94, 1)
  val medium = GameDifficulty("Medium", GameSpeed.mediumSpeed, new GameSpeed(10), 0.82, 2)
  val hard = GameDifficulty("Hard", GameSpeed.fastSpeed, new GameSpeed(5), 0.75, 3)
}
