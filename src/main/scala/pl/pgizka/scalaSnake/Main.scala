package scalaSnake

import pl.pgizka.scalaSnake.model.SecondPlayerDirectionKey
import rx.lang.scala.{Observable, Subscriber, Subscription}
import rx.lang.scala.subjects.ReplaySubject
import scalaSnake.Graphics.GameScreen
import scalaSnake.model._

import scala.swing.SwingApplication
import scala.swing.event.{Key, KeyPressed}


object Main extends SwingApplication {

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

    val eventSource = ReplaySubject.apply[GameEvent]()

    val refreshEventSource =
      Observable.interval(GameSpeed.refreshDuration).map[GameEvent](_ => RefreshEvent())

    val combinedSource = refreshEventSource.merge(eventSource)

    val finalStage = combinedSource.scan(initialGameState)((state, event) => state.update(event))

    var subscription: Option[Subscription] = None

    val subscriber = Subscriber.apply[GameState]((state: GameState) => {
      screen.draw(state)

      if (state.gameOver) {
        subscription.foreach(_.unsubscribe())
        screen.close()
        gameOverDialog.setScores(state)
        gameOverDialog.visible = true
      }
    })

    subscription = Some(finalStage.subscribe(subscriber))

    screen.component.reactions += {
      case KeyPressed(_, DirectionKey(direction), _, _) =>
        eventSource.onNext(MoveEvent(direction))
      case KeyPressed(_, SecondPlayerDirectionKey(direction), _, _) =>
        eventSource.onNext(SecondPlayerMoveEvent(direction))
      case KeyPressed(_, Key.P, _, _) =>
        eventSource.onNext(PauseEvent())

    }
  }

}
