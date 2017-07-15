package net.surguy.minesweeper

/**
  * Immutable representation of the current Minesweeper board - a new BoardState is created by revealing or flagging a tile.
  */
case class BoardState(dimensions: BoardDimensions, mines: List[Position], revealedTiles: List[Position], flaggedTiles: List[Position]) {

  def at(pos: Position): TileState = {
    val gameEnded = checkGameEnd()!=Playing
    pos match {
      case _ if (gameEnded || revealedTiles.contains(pos)) && mines.contains(pos) => RevealedMine
      case _ if gameEnded || revealedTiles.contains(pos) => RevealedClear(calculateNeighbours(pos))
      case _ if flaggedTiles.contains(pos) => Flagged
      case _  => Unknown
    }
  }

  def checkGameEnd(): GameState = {
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

  def revealTile(pos: Position): BoardState = this.copy(revealedTiles = pos :: revealedTiles )
  def flagTile(pos: Position): BoardState = this.copy(flaggedTiles = pos :: flaggedTiles )
}
