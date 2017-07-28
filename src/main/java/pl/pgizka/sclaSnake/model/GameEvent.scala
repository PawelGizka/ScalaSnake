package pl.pgizka.sclaSnake.model


sealed trait GameEvent

case class RefreshEvent() extends GameEvent
case class MoveEvent(direction: Direction) extends GameEvent
case class PauseEvent(paused: Boolean) extends GameEvent
case class GameOverEvent() extends GameEvent

