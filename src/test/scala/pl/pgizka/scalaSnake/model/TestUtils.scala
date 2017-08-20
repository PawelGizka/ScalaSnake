package pl.pgizka.scalaSnake.model

import scalaSnake.model.{Block, Config, Down, Right, Snake, Up}

object TestUtils {
  /**
    * Snake which will have collision when will go to left
    */
  def collisionSnake()(implicit config: Config): Snake = {
    val tailBlock = Block(0, 0)
    val block1 = tailBlock.move(Down)
    val block2 = block1.move(Down)
    val block3 = block2.move(Right)
    val headBlcok = block3.move(Up)

    val blocks = Map(
      0 -> tailBlock,
      1 -> block1,
      2 -> block2,
      3 -> block3,
      4 -> headBlcok)

    Snake(blocks, 4, 0, Up)
  }
}
