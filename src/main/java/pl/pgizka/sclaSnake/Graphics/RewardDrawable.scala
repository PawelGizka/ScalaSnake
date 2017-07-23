package pl.pgizka.sclaSnake.Graphics

import java.awt.Color

import pl.pgizka.sclaSnake.Config
import pl.pgizka.sclaSnake.model.Reward

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
