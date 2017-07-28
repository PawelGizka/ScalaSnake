package pl.pgizka.sclaSnake


import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.{Flow, Sink, Source}

import pl.pgizka.sclaSnake.Graphics.GameScreen
import pl.pgizka.sclaSnake.model._

import scala.swing.event.KeyPressed
import scala.swing.SwingApplication
import scala.swing.event.Key

object Main extends SwingApplication {

  implicit val system = ActorSystem("ScalaSnake")
  implicit val materializer = ActorMaterializer()
  implicit val config: Config = Config.defaultConfig

  val gameOverDialog = new GameOverDialog(() => setupGame())

  override def main(args: Array[String]): Unit = {
    super.main(args)
  }

  override def startup(args: Array[String]): Unit = {
    setupGame()
  }

  def setupGame(): Unit = {
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
        screen.close()
        gameOverDialog.visible = true
        killSwitch.shutdown()
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
      case KeyPressed(_, Key.P, _, _) =>
        eventQueue.offer(PauseEvent())

    }
  }

}
