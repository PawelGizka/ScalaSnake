package scalaSnake.Graphics

import java.awt.{Color, Graphics2D}

import scalaSnake.model.{Config, Snake}


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
