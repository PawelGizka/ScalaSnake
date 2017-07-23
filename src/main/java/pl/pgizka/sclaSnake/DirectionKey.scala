package pl.pgizka.sclaSnake

import pl.pgizka.sclaSnake.model.{Direction, Down, Left, Right, Up}

import scala.swing.event.Key

object DirectionKey {

  val toKey: PartialFunction[Key.Value, Direction] = {
    case Key.Up => Up
    case Key.Left => Left
    case Key.Right => Right
    case Key.Down => Down
  }

  def unapply(key: Key.Value): Option[Direction] = toKey.lift(key)
}
