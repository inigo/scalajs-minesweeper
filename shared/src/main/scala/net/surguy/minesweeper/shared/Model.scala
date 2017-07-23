package net.surguy.minesweeper.shared

sealed abstract class GameSize(val dimensions: BoardDimensions, val mineCount: Int, val name: String)
case object SmallGame extends GameSize(BoardDimensions(8, 8), 10, "Small")
case object IntermediateGame extends GameSize(BoardDimensions(15, 15), 40, "Intermediate")
case object LargeGame extends GameSize(BoardDimensions(29, 15), 99, "Expert")

sealed abstract class GameState
case object Won extends GameState
case object Lost extends GameState
case object Playing extends GameState

sealed abstract class TileState(val char: String)
case class RevealedClear(neighbours: Int) extends TileState(""+neighbours)
case object RevealedMine extends TileState("X")
case object Flagged extends TileState("F")
case object Unknown extends TileState("_")

case class Position(x: Int, y: Int)

case class BoardDimensions(x: Int, y: Int) {
  val totalSpaces: Int = (x+1) * (y+1)
}