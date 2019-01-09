package scalaSnake

import java.awt.{Color, Dimension}

import scalaSnake.model.{Config, GameState}

import scala.swing.{BoxPanel, Button, Dialog, Label, MainFrame, Orientation, Swing}


class GameOverDialog(playAgainCallback: () => Unit) extends MainFrame {
  title = "Scala Snake"
  centerOnScreen()

  val scoreLabel = new Label("")

  preferredSize = new Dimension(350, 150)

  contents = new BoxPanel(Orientation.Vertical) {
    contents += scoreLabel
    contents += Swing.VStrut(10)
    contents += Swing.Glue
    contents += new Label("Do you want to play again?")
    contents += Swing.VStrut(10)
    contents += Swing.Glue

    contents += new BoxPanel(Orientation.Horizontal) {
      contents += Swing.VStrut(5)
      contents += Button("Yes") { playAgain() }
      contents += Swing.VStrut(5)
      contents += Button("No") { closeMe() }
    }

    border = Swing.EmptyBorder(10, 10, 10, 10)
  }

  def playAgain() {
    playAgainCallback()
    close()
  }

  def setScores(gameState: GameState)(implicit config: Config): Unit = {
    if (config.multiplayer) {
      scoreLabel.text = s"Game over. Final results: player1 ${gameState.score} scores, player2: ${gameState.secondPlayerScore} scores"
    } else {
      scoreLabel.text = s"Game over. Your final result is ${gameState.score} scores."
    }
  }

  def closeMe() {
    val res = Dialog.showConfirmation(contents.head,
      "Do you really want to quit?",
      optionType=Dialog.Options.YesNo,
      title=title)
    if (res == Dialog.Result.Ok)
      sys.exit(0)
  }
}
