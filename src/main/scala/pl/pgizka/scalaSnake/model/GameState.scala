package scalaSnake.model

import pl.pgizka.scalaSnake.model.{BoardMoveResult, SecondPlayerDirectionKey}
import scalaSnake.rng.RNG.Simple

case class GameState(board: Board,
                     lastMove: Option[Direction],
                     secondPlayerLastMove: Option[Direction],
                     gameSpeed: GameSpeed,
                     score: Int = 0,
                     secondPlayerScore: Int = 0,
                     gameOver: Boolean = false,
                     paused: Boolean = false) {

  def update(gameEvent: GameEvent)(implicit config: Config): GameState = gameEvent match {
    case PauseEvent() => copy(paused = !paused)
    case _ if gameOver || paused => this
    case RefreshEvent() => move()
    case MoveEvent(direction) => copy(lastMove = Some(direction))
    case SecondPlayerMoveEvent(direction) => copy(secondPlayerLastMove = Some(direction))
  }

  def move()(implicit config: Config): GameState = {
    val (canMakeMove, newSpeed) = gameSpeed.canMakeMoveAfterRefresh

    if (canMakeMove) {
      def whenGameIsOver(gameOverEvent: GameOverEvent): GameState = copy(gameOver = true)

      def whenGameIsContinued(moveResult: BoardMoveResult): GameState = {
        val newGameSpeed = if (moveResult.reward.isDefined || moveResult.secondPlayerReward.isDefined) {
          newSpeed.increaseSpeedByGameDifficulty(config.gameLevel)
        } else {
          newSpeed
        }

        copy(
          board = moveResult.board,
          lastMove = None,
          secondPlayerLastMove = None,
          gameSpeed = newGameSpeed,
          score = score + getRewardValue(moveResult.reward),
          secondPlayerScore = secondPlayerScore + getRewardValue(moveResult.secondPlayerReward)
        )
      }

      board.move(lastMove, secondPlayerLastMove).fold(whenGameIsOver, whenGameIsContinued)

    } else {
      copy(gameSpeed = newSpeed)
    }

  }

  def getRewardValue(rewardOption: Option[Reward])(implicit config: Config): Int = {
    if (rewardOption.isDefined) {
      val reward = rewardOption.get
      reward.value * config.gameLevel.scoreMultiplier * (100 / gameSpeed.makeMoveAfterRefreshes)
    } else {
      0
    }
  }

}

object GameState {

  def initialGameState(seed: Long)(implicit config: Config): GameState = {
    val blockCenter = Block.blockCenter
    val snake = Snake.initialSnake(direction = Right, blockCenter.move(Up))
    val secondSnake = if (config.multiplayer) {
      Some(Snake.initialSnake(direction = Right, blockCenter.move(Down)))
    } else {
      None
    }
    val rng = Simple(seed)
    GameState(Board.initialBoard(snake, secondSnake, rng), None, None, config.gameLevel.initialGameSpeed)
  }

}
