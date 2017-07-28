package pl.pgizka.sclaSnake.Graphics

import java.awt.{Color, Graphics2D}

import pl.pgizka.sclaSnake.Config
import pl.pgizka.sclaSnake.model.Snake


object SnakeDrawable {

  def draw(snake: Snake, g: Graphics2D)(implicit config: Config): Unit = {

    (snake.tailIndex to snake.headIndex).foreach(key => {
      val block = snake.blockPositions(key)

      if (key == snake.headIndex) {
        g.setColor(Color.blue)
      } else {
        g.setColor(Color.green)
      }

      g.fillOval(block.xInPixels, block.yInPixels, config.blockSize, config.blockSize)
    })

  }

}
