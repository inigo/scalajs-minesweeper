package net.surguy.minesweeper

import utest._
import utest.framework.{Test, Tree}

object BoardStateTest extends TestSuite {
  private val boardState = BoardState(BoardDimensions(1, 1), mines = List(Position(0,1), Position(1,0)), revealedTiles = List(), flaggedTiles = List())

  override def tests: Tree[Test] = TestSuite {
    "Checking for game end"- {
      "Initial game state is playing" - {
        assert(boardState.checkGameEnd() == Playing)
      }
      "Revealing a single non-mine continues playing" - {
        assert(boardState.revealTile(Position(0, 0)).checkGameEnd() == Playing)
      }
      "Revealing a mine loses the game" - {
        assert(boardState.revealTile(Position(0, 1)).checkGameEnd() == Lost)
      }
      "Revealing all non-mines wins the game" - {
        assert(boardState.revealTile(Position(0, 0)).revealTile(Position(1, 1)).checkGameEnd() == Won)
      }
    }

    "Showing tiles"- {
      "Shows hidden tiles as unknown"- {
        assert(boardState.at(Position(0,1)) == Unknown)
        assert(boardState.at(Position(0,0)) == Unknown)
      }
      "Shows mines when revealed"- {
        assert(boardState.revealTile(Position(0,1)).at(Position(0,1)) == RevealedMine)
      }
      "Shows all mines when game ended"- {
        assert(boardState.revealTile(Position(0,1)).at(Position(1,0)) == RevealedMine)
      }
    }
  }
}
