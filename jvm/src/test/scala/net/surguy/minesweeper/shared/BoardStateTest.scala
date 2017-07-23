package net.surguy.minesweeper.shared

import org.specs2.mutable.Specification


object BoardStateTest extends Specification {

  val boardState = BoardState(BoardDimensions(1, 1), mines = List(Position(0, 1), Position(1, 0)), revealedTiles = List(), flaggedTiles = List())

  "Checking for game end" should {
    "Initial game state is playing" in {
      boardState.gameState mustEqual Playing
    }
    "Revealing a single non-mine continues playing" in {
      boardState.revealTile(Position(0, 0)).gameState mustEqual Playing
    }
    "Revealing a mine loses the game" in {
      boardState.revealTile(Position(0, 1)).gameState mustEqual Lost
    }
    "Revealing all non-mines wins the game" in {
      boardState.revealTile(Position(0, 0)).revealTile(Position(1, 1)).gameState mustEqual Won
    }
  }

  "Showing tiles" should {
    "Shows hidden tiles as unknown" in {
      boardState.at(Position(0, 1)) mustEqual Unknown
      boardState.at(Position(0, 0)) mustEqual Unknown
    }
    "Shows mines when revealed" in {
      boardState.revealTile(Position(0, 1)).at(Position(0, 1)) mustEqual RevealedMine
    }
    "Shows all mines when game ended" in {
      boardState.revealTile(Position(0, 1)).at(Position(1, 0)) mustEqual RevealedMine
    }
  }

  "Flagging tiles" should {
    "Adds a flag to a tile" in {
      boardState.toggleFlag(Position(0, 0)).at(Position(0, 0)) mustEqual Flagged
    }
    "Removes a flag from an already flagged tile" in {
      boardState.toggleFlag(Position(0, 0)).toggleFlag(Position(0, 0)).at(Position(0, 0)) mustEqual Unknown
    }
  }

  "Calculating neighbours" should {
    val largerBoardState = BoardState(BoardDimensions(8, 8), mines = List(Position(0, 1), Position(1, 0)), revealedTiles = List(), flaggedTiles = List())
    "identifies 3 neighbours in a corner" in {
      largerBoardState.neighbouringPositions(Position(0, 0)).size mustEqual 3
      largerBoardState.neighbouringPositions(Position(8, 8)).size mustEqual 3
    }
    "identifies 5 neighbours on an edge" in {
      largerBoardState.neighbouringPositions(Position(3, 0)).size mustEqual 5
      largerBoardState.neighbouringPositions(Position(0, 3)).size mustEqual 5
      largerBoardState.neighbouringPositions(Position(8, 3)).size mustEqual 5
      largerBoardState.neighbouringPositions(Position(3, 8)).size mustEqual 5
    }
    "identifies 8 neighbours in the middle" in {
      largerBoardState.neighbouringPositions(Position(3, 3)).size mustEqual 8
    }

    "finds 2 mines for a corner with 2 mines in" in {
      largerBoardState.calculateNeighbours(Position(0, 0)) mustEqual 2
    }
    "finds no mines when there are none" in {
      largerBoardState.calculateNeighbours(Position(5, 5)) mustEqual 0
    }
    "finds 1 mine when there is one nearby" in {
      largerBoardState.calculateNeighbours(Position(2, 0)) mustEqual 1
      largerBoardState.calculateNeighbours(Position(0, 2)) mustEqual 1
    }
  }

  "Revealing neighbours" should {
    // 2X10
    // X121
    // 112X
    // 002X
    val mediumBoardState = BoardState(BoardDimensions(3, 3), mines = List(Position(0, 1), Position(1, 0), Position(3, 2), Position(3, 3)), revealedTiles = List(), flaggedTiles = List())
    "reveals nothing additional when there is a mine next to the clicked on square" in {
      mediumBoardState.calculateTilesToReveal(Position(0, 0)) mustEqual List(Position(0, 0))
      mediumBoardState.calculateTilesToReveal(Position(2, 0)) mustEqual List(Position(2, 0))
    }
    "reveals empty spaces and their neighbours when starting with an empty space next to empty spaces" in {
      mediumBoardState.calculateTilesToReveal(Position(0, 3)).toSet mustEqual Set(Position(0, 3), Position(1, 3), Position(0, 2), Position(1, 2), Position(2, 2), Position(2, 3))
    }
    "reveals neighbours when starting with an empty space" in {
      mediumBoardState.calculateTilesToReveal(Position(3, 0)).toSet mustEqual Set(Position(3, 0), Position(3, 1), Position(2, 0), Position(2, 1))
    }
  }

}
