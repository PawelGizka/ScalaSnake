package scalaSnake.model

sealed trait GameEvent

case class RefreshEvent() extends GameEvent
case class MoveEvent(direction: Direction) extends GameEvent
case class SecondPlayerMoveEvent(direction: Direction) extends GameEvent
case class PauseEvent() extends GameEvent
case class GameOverEvent() extends GameEvent

