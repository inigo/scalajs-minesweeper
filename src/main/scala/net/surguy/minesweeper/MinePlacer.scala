package net.surguy.minesweeper

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

/**
  * Randomly place mines on the board.
  */
class MinePlacer(dimensions: BoardDimensions) {
  def randomMines(count: Int): List[Position] = {
    if (count > dimensions.totalSpaces) throw new IllegalArgumentException("Trying to create more mines than there are spaces")

    val minesSoFar = new ArrayBuffer[Position]()
    for (_ <- 0 until count) {
      var newMine: Position = null
      do {
        newMine = randomPosition()
      } while (minesSoFar.contains(newMine))
      minesSoFar += newMine
    }
    minesSoFar.toList
  }

  def randomPosition() = Position(Random.nextInt(dimensions.x), Random.nextInt(dimensions.y))
}
