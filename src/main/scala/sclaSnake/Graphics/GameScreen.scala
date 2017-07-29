package sclaSnake.Graphics

import java.awt.{Dimension, Graphics2D}

import sclaSnake.Config
import sclaSnake.model.GameState

import scala.swing.{Component, MainFrame}


class GameScreen(var gameState: GameState)(implicit config: Config) extends MainFrame {
  require(gameState != null)

  centerOnScreen()
  resizable = false

  def draw(gameState: GameState): Unit = {
    repaint()
    this.gameState = gameState
  }

  val component = new Component {
    focusable = true
    requestFocus()
    listenTo(keys)

    preferredSize = new Dimension(config.pixelScreenWidth, config.pixelScreenHeight)

    override protected def paintComponent(g: Graphics2D): Unit = {
      BoardDrawable.draw(gameState, g)
    }
  }

  contents = component
}
