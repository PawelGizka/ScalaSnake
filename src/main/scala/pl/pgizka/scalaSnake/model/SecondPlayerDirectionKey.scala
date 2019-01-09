package pl.pgizka.scalaSnake.model

import scalaSnake.model
import scalaSnake.model.Direction

import scala.swing.event.Key

object SecondPlayerDirectionKey {

  val toKey: PartialFunction[Key.Value, Direction] = {
    case Key.W => model.Up
    case Key.S => model.Down
    case Key.A => model.Left
    case Key.D => model.Right
  }

  def unapply(key: Key.Value): Option[Direction] = toKey.lift(key)
}
