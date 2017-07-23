package net.surguy.minesweeper.shared

import org.specs2.mutable.Specification


object MinePlacerTest extends Specification {
  private val minePlacer = new MinePlacer(BoardDimensions(4, 4))

  "Creating mines gives a position within the board dimensions" in {
    val pos = minePlacer.randomPosition()
    pos.x must beGreaterThanOrEqualTo(0) and beLessThanOrEqualTo(3)
    pos.y must beGreaterThanOrEqualTo(0) and beLessThanOrEqualTo(3)
  }
  "Creating multiple mines creates several different values" in {
    (1 to 10).map(i => minePlacer.randomPosition()).toSet must haveSize(greaterThan(1))
  }
  "Creating too many mines throws an exception" in {
    minePlacer.randomMines(26) must throwAn[IllegalArgumentException]
  }
  "Creating mines gives a unique set of positions" in {
    val mines = minePlacer.randomMines(10)
    mines.distinct.size mustEqual mines.size
  }
  "Creating mines gives the correct number of mines" in {
    minePlacer.randomMines(10) must haveSize(10)
  }
  "Spaces on board matches expected size" in {
    BoardDimensions(3, 3).totalSpaces mustEqual 16
    BoardDimensions(4, 2).totalSpaces mustEqual 15
  }
}
