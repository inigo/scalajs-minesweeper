package net.surguy.minesweeper

import utest._
import utest.framework.{Test, Tree}

object BoardStateTest extends TestSuite {

  override def tests: Tree[Test] = TestSuite {
    // 2X
    // X2
    val boardState = BoardState(BoardDimensions(1, 1), mines = List(Position(0,1), Position(1,0)), revealedTiles = List(), flaggedTiles = List())
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

    "Flagging tiles"- {
      "Adds a flag to a tile"- {
        assert(boardState.toggleFlag(Position(0,0)).at(Position(0,0)) == Flagged )
      }
      "Removes a flag from an already flagged tile"- {
        assert(boardState.toggleFlag(Position(0,0)).toggleFlag(Position(0,0)).at(Position(0,0)) == Unknown )
      }
    }

    "Calculating neighbours"- {
      val largerBoardState = BoardState(BoardDimensions(8, 8), mines = List(Position(0,1), Position(1,0)), revealedTiles = List(), flaggedTiles = List())
      "identifies 3 neighbours in a corner"- {
        assert(largerBoardState.neighbouringPositions(Position(0,0)).size == 3)
        assert(largerBoardState.neighbouringPositions(Position(8,8)).size == 3)
      }
      "identifies 5 neighbours on an edge"- {
        assert(largerBoardState.neighbouringPositions(Position(3,0)).size == 5)
        assert(largerBoardState.neighbouringPositions(Position(0,3)).size == 5)
        assert(largerBoardState.neighbouringPositions(Position(8,3)).size == 5)
        assert(largerBoardState.neighbouringPositions(Position(3,8)).size == 5)
      }
      "identifies 8 neighbours in the middle"- {
        assert(largerBoardState.neighbouringPositions(Position(3,3)).size == 8)
      }

      "finds 2 mines for a corner with 2 mines in"- {
        assert(largerBoardState.calculateNeighbours(Position(0,0)) == 2)
      }
      "finds no mines when there are none"- {
        assert(largerBoardState.calculateNeighbours(Position(5,5)) == 0)
      }
      "finds 1 mine when there is one nearby"- {
        assert(largerBoardState.calculateNeighbours(Position(2,0)) == 1)
        assert(largerBoardState.calculateNeighbours(Position(0,2)) == 1)
      }
    }

    "Revealing neighbours"- {
      // 2X10
      // X121
      // 112X
      // 002X
      val mediumBoardState = BoardState(BoardDimensions(3, 3), mines = List(Position(0,1), Position(1,0), Position(3, 2), Position(3,3)), revealedTiles = List(), flaggedTiles = List())
      "reveals nothing additional when there is a mine next to the clicked on square"- {
        assert(mediumBoardState.calculateTilesToReveal(Position(0,0)) == List(Position(0,0)))
        assert(mediumBoardState.calculateTilesToReveal(Position(2,0)) == List(Position(2,0)))
      }
      "reveals empty spaces and their neighbours when starting with an empty space next to empty spaces"- {
        assert(mediumBoardState.calculateTilesToReveal(Position(0,3)).toSet == Set(Position(0,3), Position(1,3), Position(0,2), Position(1,2), Position(2,2), Position(2,3)))
      }
      "reveals neighbours when starting with an empty space"- {
        assert(mediumBoardState.calculateTilesToReveal(Position(3,0)).toSet == Set(Position(3,0), Position(3,1), Position(2,0), Position(2,1)))
      }
    }

  }
}
