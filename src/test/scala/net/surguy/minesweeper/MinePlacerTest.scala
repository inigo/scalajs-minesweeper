package net.surguy.minesweeper

import utest._
import utest.framework.{Test, Tree}

object MinePlacerTest extends TestSuite {
  private val minePlacer = new MinePlacer(BoardDimensions(4, 4))

  override def tests: Tree[Test] = TestSuite {
    "Creating mines gives a position within the board dimensions"-{
      val pos = minePlacer.randomPosition()
      assert( pos.x >=0 && pos.x <= 3)
      assert( pos.y >=0 && pos.y <= 3)
    }
    "Creating multiple mines creates several different values"-{
      assert( (1 to 10).map(i => minePlacer.randomPosition()).toSet.size > 1)
    }
    "Creating too many mines throws an exception"-{
      intercept[IllegalArgumentException] { minePlacer.randomMines(26) }
    }
    "Creating mines gives a unique set of positions"-{
      val mines = minePlacer.randomMines(10)
      assert(mines.distinct.size == mines.size)
    }
    "Creating mines gives the correct number of mines"-{
      assert(minePlacer.randomMines(10).size == 10)
    }
    "Spaces on board matches expected size"-{
      assert( BoardDimensions(3, 3).totalSpaces == 16)
      assert( BoardDimensions(4, 2).totalSpaces == 15)
    }
  }
}
