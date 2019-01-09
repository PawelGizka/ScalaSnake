package scalaSnake.Graphics

import java.awt.{Dimension, Graphics2D}

import scalaSnake.model.{Config, GameState}

import scala.swing.{BoxPanel, Component, Label, MainFrame, Orientation, Swing}


class GameScreen(var gameState: GameState)(implicit config: Config) extends MainFrame {
  require(gameState != null)

  centerOnScreen()
  resizable = false

  val scoreLabel = new Label("Score: ")
  val secondPlayerScoreLabel = new Label("Second Player Score: ")
  val gameLevelLabel = new Label(s"Game Level: ${config.gameLevel}")

  def draw(gameState: GameState): Unit = {
    this.gameState = gameState
    scoreLabel.text= s"Score: ${gameState.score}"
    secondPlayerScoreLabel.text= s"Second Player Score: ${gameState.secondPlayerScore}"
    repaint()
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

  contents = new BoxPanel(Orientation.Vertical) {
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += scoreLabel
      if (config.multiplayer) contents += secondPlayerScoreLabel
      contents += Swing.HStrut(70)
      contents += gameLevelLabel

    }
    contents += Swing.VStrut(10)
    contents += Swing.Glue
    contents += component

  }
}
