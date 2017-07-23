package pl.pgizka.sclaSnake.Graphics

import java.awt.{Dimension, Graphics2D}

import pl.pgizka.sclaSnake.Config
import pl.pgizka.sclaSnake.model.GameState

import scala.swing.{Component, Panel}


class GameScreen(var gameState: GameState)(implicit config: Config) extends Component {
  focusable = true
  requestFocus()
  listenTo(keys)

  preferredSize = new Dimension(config.pixelScreenWidth, config.pixelScreenHeight)

  def draw(gameState: GameState): Unit = {
    repaint()
    this.gameState = gameState
  }

  override protected def paintComponent(g: Graphics2D): Unit = {
    BoardDrawable.draw(gameState, g)
  }


}
