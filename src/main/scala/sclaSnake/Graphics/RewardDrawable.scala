package sclaSnake.Graphics

import java.awt.Color

import sclaSnake.model.{Config, Reward}

import scala.swing.Graphics2D


object RewardDrawable {

  def draw(rewards: Seq[Reward], g: Graphics2D)(implicit config: Config): Unit = {
    rewards.foreach(reward => {
      val position = reward.position

      g.setColor(Color.YELLOW)
      g.fillOval(position.xInPixels, position.yInPixels, config.blockSize, config.blockSize)
    })
  }

}
