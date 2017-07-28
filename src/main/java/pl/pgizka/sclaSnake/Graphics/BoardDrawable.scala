package pl.pgizka.sclaSnake.Graphics

import java.awt.{Color, Font, Graphics2D}

import pl.pgizka.sclaSnake.Config
import pl.pgizka.sclaSnake.model.GameState

object BoardDrawable {

  def draw(gameState: GameState, g: Graphics2D)(implicit config: Config): Unit = {
    drawBoard(g)

    SnakeDrawable.draw(gameState.board.snake, g)
    RewardDrawable.draw(gameState.board.rewards, g)

    if (gameState.paused) {
      g.setColor(Color.RED)
      g.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 30))
      g.drawString("PAUSED", config.pixelScreenWidth * 4 / 10, config.pixelScreenHeight / 2)
    }

  }

  def drawBoard(g: Graphics2D)(implicit config: Config): Unit = {

    def drawRow(y: Int): Unit =
      (0 to (config.pixelScreenWidth, config.blockSize)).foreach(x => {
        g.setColor(Color.BLACK)
        g.drawRect(x, y, config.blockSize, config.blockSize)
      })

    def drawColumns(): Unit =
      (0 to (config.pixelScreenHeight, config.blockSize)).foreach(y => drawRow(y))

    drawColumns()
  }



}
