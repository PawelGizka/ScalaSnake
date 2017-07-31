package sclaSnake

import java.awt.Dimension

import sclaSnake.model.{Config, GameDifficulty}

import scala.swing.{BoxPanel, Button, ComboBox, Label, MainFrame, Orientation, Slider, Swing}


class OpeningDialog(playCallback: (Config) => Unit) extends MainFrame {
  title = "Scala Snake"
  centerOnScreen()
  preferredSize = new Dimension(400, 300)

  val boardWidthLabel = new Label("Board Width: ")
  val boardWidthSlider = new Slider()
  boardWidthSlider.min = 5
  boardWidthSlider.max = 40
  boardWidthSlider.reactions.+={
    case _ => boardWidthLabel.text = s"Board Width: ${boardWidthSlider.value}"
  }
  boardWidthSlider.value = 20

  val boardHeightLabel = new Label("Board Width: ")
  val boardHeightSlider = new Slider()
  boardHeightSlider.min = 5
  boardHeightSlider.max = 40
  boardHeightSlider.reactions.+={
    case _ => boardHeightLabel.text = s"Board Height: ${boardHeightSlider.value}"
  }
  boardHeightSlider.value = 20

  val boardBlockSizeLabel = new Label("Board Block Size: ")
  val boardBlockSizeSlider = new Slider()
  boardBlockSizeSlider.min = 10
  boardBlockSizeSlider.max = 40
  boardBlockSizeSlider.reactions.+={
    case _ => boardBlockSizeLabel.text = s"Board Block Size: ${boardBlockSizeSlider.value}"
  }
  boardBlockSizeSlider.value = 20

  val gameDifficultyComboBox = new ComboBox(List(GameDifficulty.easy.name, GameDifficulty.medium.name, GameDifficulty.hard.name))

  contents = new BoxPanel(Orientation.Vertical) {
    contents += new Label("Welcome to functional Scala Snake")
    contents += Swing.VStrut(10)
    contents += Swing.VGlue
    contents += boardWidthLabel
    contents += boardWidthSlider
    contents += Swing.VStrut(10)
    contents += Swing.VGlue
    contents += boardHeightLabel
    contents += boardHeightSlider
    contents += Swing.VStrut(10)
    contents += Swing.VGlue
    contents += boardBlockSizeLabel
    contents += boardBlockSizeSlider
    contents += Swing.VStrut(10)
    contents += Swing.VGlue
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += new Label("Dificulty: ")
      contents += Swing.HStrut(20)
      contents += gameDifficultyComboBox
    }

    contents += Button("Play!") { play() }


    border = Swing.EmptyBorder(10, 10, 10, 10)
  }

  def play() {
    val gameLevel = gameDifficultyComboBox.selection.item match {
      case GameDifficulty.easy.name => GameDifficulty.easy
      case GameDifficulty.medium.name => GameDifficulty.medium
      case GameDifficulty.hard.name => GameDifficulty.hard
    }

    playCallback(Config(boardWidthSlider.value, boardHeightSlider.value, boardBlockSizeSlider.value, gameLevel))

    close()
  }

}

