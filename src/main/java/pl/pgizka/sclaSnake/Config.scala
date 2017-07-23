package pl.pgizka.sclaSnake


case class Config(screenWidth: Int, screenHeight: Int, blockSize: Int, blockBorder: Int) {
  val pixelScreenWidth: Int = screenWidth * blockSize
  val pixelScreenHeight: Int = screenHeight * blockSize

  val boardSize: Int = screenWidth * screenHeight
}

object Config {

  implicit val defaultConfig = Config(screenWidth = 20, screenHeight = 20, blockSize = 20, blockBorder = 2)

  val screenWidth = 20
  val screenHeight = 20

  val blockSize = 10
  val blockBorder = 2


}
