package pl.pgizka.sclaSnake.model


import pl.pgizka.sclaSnake.Config
import pl.pgizka.sclaSnake.rng.RNG.Simple

case class GameState(board: Board, lastMove: Option[Direction], gameOver: Boolean = false, paused: Boolean = false) {

  def update(gameEvent: GameEvent)(implicit config: Config): GameState = gameEvent match {
    case PauseEvent() => copy(paused = !paused)
    case _ if gameOver || paused => this
    case RefreshEvent() => move()
    case MoveEvent(direction) => copy(lastMove = Some(direction))
  }

  def move()(implicit config: Config): GameState = {
    board.move(lastMove).fold(
      (gameOverEvent) => copy(gameOver = true),
      (newBoard) => copy(board = newBoard, lastMove = None)
    )
  }

}

object GameState {

  def initialGameState(seed: Long): GameState = {
    val snake = Snake.initialSnake
    val rng = Simple(seed)
    val board = new Board(snake, rng)
    GameState(board, None)
  }

}
