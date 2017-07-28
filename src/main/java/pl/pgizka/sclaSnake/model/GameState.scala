package pl.pgizka.sclaSnake.model


import pl.pgizka.sclaSnake.rng.RNG.Simple

case class GameState(board: Board, lastMove: Option[Direction], gameOver: Boolean = false) {

  def update(gameEvent: GameEvent): GameState = gameEvent match {
    case _ if gameOver => this
    case RefreshEvent() => move()
    case MoveEvent(direction) => copy(lastMove = Some(direction))
  }

  def move(): GameState = {
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
