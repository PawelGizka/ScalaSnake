package scalaSnake.Graphics

import java.awt.{Color, Font, Graphics2D}

import scalaSnake.model.{Config, GameState}

object BoardDrawable {

  def draw(gameState: GameState, g: Graphics2D)(implicit config: Config): Unit = {
    drawBoard(g)

    SnakeDrawable.draw(gameState.board.snake, g, Color.green)
    gameState.board.snake2.foreach(SnakeDrawable.draw(_, g, Color.red))

    RewardDrawable.draw(gameState.board.rewards, g)

    if (gameState.paused) {
      g.setColor(Color.RED)
      g.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 30))
      g.drawString("PAUSED", config.pixelScreenWidth * 4 / 10, config.pixelScreenHeight / 2)
    }

  }

  def drawBoard(g: Graphics2D)(implicit config: Config): Unit = {

    def drawRow(y: Int): Unit =
      (0 until (config.pixelScreenWidth, config.blockSize)).foreach(x => {
        g.setColor(Color.BLACK)
        g.drawRect(x, y, config.blockSize, config.blockSize)
      })

    def drawColumns(): Unit =
      (0 until (config.pixelScreenHeight, config.blockSize)).foreach(y => drawRow(y))

    drawColumns()
  }



}
