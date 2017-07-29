package sclaSnake

import java.awt.Color

import scala.swing.{BoxPanel, Button, Dialog, Label, MainFrame, Orientation, Swing}


class GameOverDialog(playAgainCallback: () => Unit) extends MainFrame {
  title = "Scala Snake"

  val questionLabel = new Label("Game over. Do you want to play again?")

  questionLabel.foreground = Color.BLACK

  contents = new BoxPanel(Orientation.Vertical) {
    contents += questionLabel
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

  def closeMe() {
    val res = Dialog.showConfirmation(contents.head,
      "Do you really want to quit?",
      optionType=Dialog.Options.YesNo,
      title=title)
    if (res == Dialog.Result.Ok)
      sys.exit(0)
  }
}
