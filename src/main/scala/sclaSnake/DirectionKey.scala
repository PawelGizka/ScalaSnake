package sclaSnake

import sclaSnake.model.{Direction, Down, Up}

import scala.swing.event.Key

object DirectionKey {

  val toKey: PartialFunction[Key.Value, Direction] = {
    case Key.Up => Up
    case Key.Left => model.Left
    case Key.Right => model.Right
    case Key.Down => Down
  }

  def unapply(key: Key.Value): Option[Direction] = toKey.lift(key)
}
