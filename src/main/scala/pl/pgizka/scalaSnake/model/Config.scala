package scalaSnake.model

case class Config(screenWidth: Int, screenHeight: Int, blockSize: Int, gameLevel: GameDifficulty, multiplayer: Boolean) {
  val pixelScreenWidth: Int = screenWidth * blockSize
  val pixelScreenHeight: Int = screenHeight * blockSize

  val boardSize: Int = screenWidth * screenHeight
}

object Config {

  val defaultConfig: Config = Config(screenWidth = 20, screenHeight = 20, blockSize = 20,
    gameLevel = GameDifficulty.easy, multiplayer = false)

}
