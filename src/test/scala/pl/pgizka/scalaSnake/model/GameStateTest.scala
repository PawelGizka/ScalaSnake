package pl.pgizka.scalaSnake.model

import org.scalatest.{MustMatchers, WordSpec}
import org.scalatest.OptionValues._

import scalaSnake.model._

class GameStateTest extends WordSpec with MustMatchers {

  implicit val config = Config(20, 20, 20, GameDifficulty.easy)

  val gameState: GameState = GameState.initialGameState(100)
  val makeMoveAfterRefreshes = gameState.gameSpeed.makeMoveAfterRefreshes
  val elapsedGameSpeed: GameSpeed = gameState.gameSpeed.copy(refreshCounter = makeMoveAfterRefreshes)

  "A GameState" should {
    "remember its last move" in {
      val newGameState = gameState.update(MoveEvent(Up))

      newGameState.lastMove.value mustBe Up
      newGameState.copy(lastMove = gameState.lastMove) mustEqual gameState
    }

    "be paused after pause event" in {
      val pausedGameState = gameState.update(PauseEvent())
      pausedGameState.paused mustEqual true
    }

    "be not paused when was previously paused after pause event" in {
      val pausedGameState = gameState.update(PauseEvent())
      pausedGameState.paused mustEqual true

      val notPausedGameState = pausedGameState.update(PauseEvent())
      notPausedGameState.paused mustEqual false

      notPausedGameState mustEqual gameState
    }

    "do nothing when game is over" in {
      val gameOverState = gameState.copy(gameOver = true)
      gameOverState.update(RefreshEvent()) mustEqual gameOverState
    }

    "do nothing when game is paused" in {
      val gamePausedState = gameState.copy(paused = true)
      gamePausedState.update(RefreshEvent()) mustEqual gamePausedState
    }

    "do nothing when game speed time did not elapsed" in {
      val notElapsedState = gameState.copy(gameSpeed = new GameSpeed(3))
      notElapsedState.update(RefreshEvent()).board mustEqual notElapsedState.board
    }

    "make move when game speed time elapsed" in {
      val elapsedState = gameState.copy(gameSpeed = elapsedGameSpeed)
      elapsedState.update(RefreshEvent()).board must not equal elapsedState.board
    }

    "do not increase scores and game speed when reward was not collected" in {
      val boardWithoutRewards = gameState.board.copy(rewards = Seq.empty)
      val gameStateWithoutRewards = gameState.copy(board = boardWithoutRewards, gameSpeed = elapsedGameSpeed)

      val updatedState = gameStateWithoutRewards.update(RefreshEvent())

      updatedState.board must not equal gameStateWithoutRewards.board

      updatedState.score mustEqual gameStateWithoutRewards.score
      updatedState.gameSpeed.makeMoveAfterRefreshes mustEqual gameStateWithoutRewards.gameSpeed.makeMoveAfterRefreshes
    }

    "increase scores and game speed when reward was collected" in {
      val snake = gameState.board.snake
      val reward = Reward(snake.blockPositions(snake.headIndex).move(Right), 100)
      val boardWithReward = gameState.board.copy(rewards = Seq(reward))

      val gameStateWithReward = gameState.copy(board = boardWithReward, gameSpeed = elapsedGameSpeed)

      val updatedState = gameStateWithReward.update(RefreshEvent())

      updatedState.board must not equal gameStateWithReward.board

      updatedState.score must be > gameStateWithReward.score
      updatedState.gameSpeed.makeMoveAfterRefreshes must be < gameStateWithReward.gameSpeed.makeMoveAfterRefreshes
    }
    
    "be over when snake had collision" in {
      val snake = TestUtils.collisionSnake
      val board = gameState.board.copy(snake = snake)
      val collisionState = gameState.copy(board = board, gameSpeed = elapsedGameSpeed, lastMove = Some(Left))

      val afterCollision = collisionState.update(RefreshEvent())

      afterCollision.gameOver mustBe true
    }

  }

}
