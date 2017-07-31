package sclaSnake

import sclaSnake.model.Direction

import scala.swing.event.Key

object DirectionKey {

  val toKey: PartialFunction[Key.Value, Direction] = {
    case Key.Up => model.Up
    case Key.Left => model.Left
    case Key.Right => model.Right
    case Key.Down => model.Down
  }

  def unapply(key: Key.Value): Option[Direction] = toKey.lift(key)
}
