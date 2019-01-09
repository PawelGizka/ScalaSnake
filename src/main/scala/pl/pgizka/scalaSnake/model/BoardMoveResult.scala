package pl.pgizka.scalaSnake.model

import scalaSnake.model.{Board, Reward}

case class BoardMoveResult(board: Board, reward: Option[Reward], secondPlayerReward: Option[Reward])
