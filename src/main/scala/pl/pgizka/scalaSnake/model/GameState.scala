package scalaSnake.model

import scalaSnake.rng.RNG.Simple

case class GameState(board: Board, lastMove: Option[Direction],
                     gameSpeed: GameSpeed, score: Int = 0,
                     gameOver: Boolean = false, paused: Boolean = false) {

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
          newSpeed.increaseSpeedByGameLevel(config.gameLevel)
        } else {
          newSpeed
        }

        val newScore = if (rewardOption.isDefined) {
          val reward = rewardOption.get
          if (score == 0) {
            reward.value
          } else {
            score +
              (reward.value * config.gameLevel.scoreMultiplier * (100 / gameSpeed.makeMoveAfterRefreshes))
          }
        } else {
          score
        }

        copy(board = newBoard, lastMove = None, gameSpeed = newGameSpeed, score = newScore)
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
    GameState(Board.initialBoard(snake, rng), None, config.gameLevel.initialGameSpeed)
  }

}
