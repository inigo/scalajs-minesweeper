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

  private def calculateNeighbours(pos: Position) = 0

  def revealTile(pos: Position): BoardState = this.copy(revealedTiles = pos :: revealedTiles )
  def flagTile(pos: Position): BoardState = this.copy(flaggedTiles = pos :: flaggedTiles )
}
