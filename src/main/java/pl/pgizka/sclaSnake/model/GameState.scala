package pl.pgizka.sclaSnake.model

import pl.pgizka.sclaSnake.rng.RNG
import pl.pgizka.sclaSnake.rng.RNG.Simple



case class GameState(board: Board, lastMove: Option[Direction]) {

  def update(gameEvent: GameEvent): GameState = gameEvent match {
    case RefreshEvent() => move()
    case MoveEvent(direction) => copy(lastMove = Some(direction))
  }

  def move(): GameState = {
    val newBoard = board.move(lastMove)
    copy(board = newBoard, lastMove = None)
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
