package pl.pgizka.sclaSnake

import java.awt.Dimension
import java.nio.file.Paths

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, IOResult, OverflowStrategy, ThrottleMode}
import akka.stream.scaladsl.{FileIO, Flow, Keep, RunnableGraph, Sink, Source}
import akka.util.ByteString
import pl.pgizka.sclaSnake.Graphics.GameScreen
import pl.pgizka.sclaSnake.model._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.annotation.unchecked.uncheckedVariance
import scala.concurrent.Future
import scala.io.StdIn
import scala.swing.event.Key.Location
import scala.swing.event.{Key, KeyPressed}
import scala.swing.{Frame, Label, MainFrame, SimpleSwingApplication}
import pl.pgizka.sclaSnake.Config

import scala.annotation.tailrec


object Main extends SimpleSwingApplication {

  val initialGameState: GameState = GameState.initialGameState(1)

  val screen = new GameScreen(initialGameState)

  override def top: Frame = new MainFrame {
    centerOnScreen()
    resizable = false
    contents = screen
  }

  override def main(args: Array[String]): Unit = {
    super.main(args)

    implicit val system = ActorSystem("ScalaSnake")
    implicit val materializer = ActorMaterializer()

    val eventSource =
      Source.queue[GameEvent](bufferSize = 100, overflowStrategy = OverflowStrategy.backpressure)

    val refreshSource =
      Source.repeat[GameEvent](RefreshEvent())
        .throttle(1, 1.second, 1, ThrottleMode.Shaping)

    val updateFlow = Flow[GameEvent]
      .scan(initialGameState)((state, event) => state.update(event))

    val drawSink = Sink.foreach[GameState](screen.draw)

    val eventQueue = eventSource.merge(refreshSource)
      .via(updateFlow)
      .to(drawSink)
      .run()


    screen.reactions += {
      case KeyPressed(_, DirectionKey(direction), _, _) => {
        eventQueue.offer(MoveEvent(direction))
      }
    }


    StdIn.readLine()
    eventQueue.complete()
  }


}
