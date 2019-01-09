package scalaSnake

import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.{Flow, Sink, Source}
import pl.pgizka.scalaSnake.model.SecondPlayerDirectionKey
import scalaSnake.Graphics.GameScreen
import scalaSnake.model._

import scala.swing.SwingApplication
import scala.swing.event.{Key, KeyPressed}

object Main extends SwingApplication {

  implicit val system = ActorSystem("ScalaSnake")
  implicit val materializer = ActorMaterializer()

  val gameOverDialog = new GameOverDialog(() => showOpeningDialog())

  override def startup(args: Array[String]): Unit = {
    showOpeningDialog()
  }

  def showOpeningDialog(): Unit = {
    val openingDialog = new OpeningDialog((config) => setupGame(config))
    openingDialog.visible = true
  }

  def setupGame(implicit config: Config): Unit = {
    val seed = System.currentTimeMillis()
    val initialGameState: GameState = GameState.initialGameState(seed)

    val screen = new GameScreen(initialGameState)
    screen.visible = true

    val killSwitch = KillSwitches.shared("main")

    val eventSource =
      Source.queue[GameEvent](bufferSize = 100, overflowStrategy = OverflowStrategy.backpressure)

    val refreshSource =
      Source.repeat[GameEvent](RefreshEvent())
        .throttle(1, GameSpeed.refreshDuration, 1, ThrottleMode.Shaping)

    val updateFlow = Flow[GameEvent]
      .scan(initialGameState)((state, event) => state.update(event))

    val drawSink = Sink.foreach[GameState](state => {
      screen.draw(state)

      if (state.gameOver) {
        killSwitch.shutdown()
        screen.close()
        gameOverDialog.setScores(state)
        gameOverDialog.visible = true
      }
    })

    val eventQueue = eventSource.merge(refreshSource)
      .via(killSwitch.flow)
      .via(updateFlow)
      .to(drawSink)
      .run()


    screen.component.reactions += {
      case KeyPressed(_, DirectionKey(direction), _, _) =>
        eventQueue.offer(MoveEvent(direction))
      case KeyPressed(_, SecondPlayerDirectionKey(direction), _, _) =>
        eventQueue.offer(SecondPlayerMoveEvent(direction))
      case KeyPressed(_, Key.P, _, _) =>
        eventQueue.offer(PauseEvent())

    }
  }

}
