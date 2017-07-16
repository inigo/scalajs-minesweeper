package net.surguy.minesweeper

import scala.util.Random

/**
  * Randomly place mines on the board.
  */
class MinePlacer(dimensions: BoardDimensions) {
  def randomMines(count: Int): List[Position] = {
    if (count > dimensions.totalSpaces) throw new IllegalArgumentException("Trying to create more mines than there are spaces")

    def newMine(current: List[Position]): Position = { val p = randomPosition(); if (current.contains(p)) newMine(current) else p }
    (0 until count).foldLeft(List[Position]())((acc, _) => newMine(acc) :: acc )
  }

  def randomPosition() = Position(Random.nextInt(dimensions.x), Random.nextInt(dimensions.y))
}
