package pl.pgizka.sclaSnake


case class Config(screenWidth: Int, screenHeight: Int, blockSize: Int) {
  val pixelScreenWidth: Int = screenWidth * blockSize
  val pixelScreenHeight: Int = screenHeight * blockSize

  val boardSize: Int = screenWidth * screenHeight
}

object Config {

  val defaultConfig = Config(screenWidth = 20, screenHeight = 20, blockSize = 20)

}
