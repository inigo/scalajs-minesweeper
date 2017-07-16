package net.surguy.minesweeper

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Immutable representation of the current Minesweeper board - a new BoardState is created by revealing or flagging a tile.
  */
case class BoardState(dimensions: BoardDimensions, mines: List[Position], revealedTiles: List[Position], flaggedTiles: List[Position]) {
  val gameState: GameState = checkGameEnd()

  def at(pos: Position): TileState = {
    val gameEnded = gameState!=Playing
    pos match {
      case _ if (gameEnded || revealedTiles.contains(pos)) && mines.contains(pos) => RevealedMine
      case _ if gameEnded || revealedTiles.contains(pos) => RevealedClear(calculateNeighbours(pos))
      case _ if flaggedTiles.contains(pos) => Flagged
      case _  => Unknown
    }
  }

  private def checkGameEnd(): GameState = {
    if (revealedTiles.intersect(mines).nonEmpty) Lost
      else if (revealedTiles.length + mines.length == dimensions.totalSpaces) Won
      else Playing
  }

  private[minesweeper] def calculateNeighbours(pos: Position): Int = {
    val neighbours = neighbouringPositions(pos)
    neighbours.count(p => mines.contains(p))
  }

  private[minesweeper] def neighbouringPositions(pos: Position): Seq[Position] = {
    for (x <- pos.x - 1 to pos.x + 1;
         y <- pos.y-1 to pos.y+1
         if !(x==pos.x && y==pos.y)
         if x >= 0 && y >= 0 && x <= dimensions.x && y <= dimensions.y
        ) yield Position(x, y)
  }

  private[minesweeper] def calculateTilesToReveal(pos: Position): List[Position] = {
    val tilesToProcess = mutable.Queue[Position](pos)
    val tilesProcessed = mutable.Queue[Position]()
    val tilesToReveal = ListBuffer[Position]()
    while(tilesToProcess.nonEmpty) {
      val nextTile = tilesToProcess.dequeue()
      if (!revealedTiles.contains(nextTile) && !tilesProcessed.contains(nextTile)) {
        if (calculateNeighbours(nextTile)==0) neighbouringPositions(nextTile).foreach(p => tilesToProcess.enqueue(p))
        tilesToReveal += nextTile
      }
      tilesProcessed.enqueue(nextTile)
    }
    tilesToReveal.toList
  }

  def revealTile(pos: Position): BoardState = {
    val tilesToReveal = calculateTilesToReveal(pos)
    this.copy(revealedTiles = tilesToReveal ::: revealedTiles )
  }

  def toggleFlag(pos: Position): BoardState = {
    val newFlags = if (flaggedTiles.contains(pos)) flaggedTiles.filterNot(_==pos) else pos :: flaggedTiles
    this.copy(flaggedTiles = newFlags )
  }
}
