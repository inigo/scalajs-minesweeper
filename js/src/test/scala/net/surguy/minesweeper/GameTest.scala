package net.surguy.minesweeper

import net.surguy.minesweeper.shared._
import utest._
import utest.framework.{Test, Tree}

object GameTest extends TestSuite {

  override def tests: Tree[Test] = TestSuite {
    val canvasEl = Game.createCanvasNode(SmallGame)
    val board = Game.startGame(canvasEl, SmallGame)
    "Clicking on one square either loses or continues play" - {
      val gameState = board.clickPixel(1, 1)
      assert( gameState == Lost || gameState == Playing)
    }
    "Clicking on all squares either wins or loses" - {
      for (x <- 0 to canvasEl.width by Game.tileWidth; y <- 0 to canvasEl.height by Game.tileHeight) board.clickPixel(x, y)
      val gameState = board.getState.gameState
      assert( gameState == Lost || gameState == Won)
    }
    "Initial state is playing" - {
      val gameState = board.getState.gameState
      assert( gameState == Playing)
    }
    "Right-clicking on a square flags it" - {
      board.rightClickPixel(1, 1)
      assert( board.getState.at(Position(0,0)) == Flagged)
    }
    "Right-clicking on a square again unflags it" - {
      board.rightClickPixel(1, 1)
      board.rightClickPixel(1, 1)
      assert( board.getState.at(Position(0,0)) == Unknown)
    }
    "Right-clicking on every square flags all squares" - {
      for (x <- 0 to canvasEl.width by Game.tileWidth; y <- 0 to canvasEl.height by Game.tileHeight) board.rightClickPixel(x, y)
      assert( board.getState.flaggedTiles.length == SmallGame.dimensions.totalSpaces)
    }
  }

}
