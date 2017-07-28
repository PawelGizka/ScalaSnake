package pl.pgizka.sclaSnake.model


import pl.pgizka.sclaSnake.Config
import pl.pgizka.sclaSnake.rng.RNG.Simple

case class GameState(board: Board, lastMove: Option[Direction],
                     gameSpeed: GameSpeed, gameOver: Boolean = false, paused: Boolean = false) {

  def update(gameEvent: GameEvent)(implicit config: Config): GameState = gameEvent match {
    case PauseEvent() => copy(paused = !paused)
    case _ if gameOver || paused => this
    case RefreshEvent() => move()
    case MoveEvent(direction) => copy(lastMove = Some(direction))
  }

  def move()(implicit config: Config): GameState = {
    val (canMakeMove, newSpeed) = gameSpeed.canMakeMoveAfterRefresh

    if (canMakeMove) {
      def whenGameIsOver(gameOverEvent: GameOverEvent): GameState = copy(gameOver = true)

      def whenGameIsContinued(tuple: Tuple2[Board, Option[Reward]]): GameState = {
        val (newBoard, rewardOption) = tuple

        val newGameSpeed = if (rewardOption.isDefined) {
          newSpeed.increaseSpeedByOneReward
        } else {
          newSpeed
        }

        copy(board = newBoard, lastMove = None, gameSpeed = newGameSpeed)
      }

      board.move(lastMove).fold(whenGameIsOver, whenGameIsContinued)

    } else {
      copy(gameSpeed = newSpeed)
    }

  }

}

object GameState {

  def initialGameState(seed: Long)(implicit config: Config): GameState = {
    val snake = Snake.initialSnake
    val rng = Simple(seed)
    GameState(Board.initialBoard(snake, rng), None, GameSpeed.slowSpeed)
  }

}
