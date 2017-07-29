package sclaSnake.model

import sclaSnake.Config

case class Block(x: Int, y: Int) {
  def xInPixels(implicit config: Config): Int = x * config.blockSize
  def yInPixels(implicit config: Config): Int = y * config.blockSize

  def id(implicit config: Config): Int = y * config.screenWidth + x

  def move(direction: Direction)(implicit config: Config): Block =
    getWithinWindow(getNewPosition(direction))

  private def getNewPosition(direction: Direction): Block = direction match {
    case Up => Block(x, y - 1)
    case Down => Block(x, y + 1)
    case Left => Block(x - 1, y)
    case Right => Block(x + 1, y)
  }

  private def getWithinWindow(block: Block)(implicit config: Config): Block = {
    if (block.x < 0) {
      Block(config.screenWidth - 1, y)
    } else if (block.x > config.screenWidth - 1) {
      Block(0, y)
    } else if (block.y < 0) {
      Block(block.x, config.screenHeight - 1)
    } else if (block.y > config.screenHeight - 1) {
      Block(block.x, 0)
    } else {
      block
    }
  }
}

object Block {
  def fromId(id: Int)(implicit config: Config): Block = {
    val y = id / config.screenWidth
    val x = id % config.screenWidth
    Block(x, y)
  }
}
