package net.surguy.minesweeper

import org.scalajs.dom.CanvasRenderingContext2D

/**
  * Displays the current state of the board and reacts to clicks.
  */
class Board(dimensions: BoardDimensions, mineCount: Int) {
  private val mines = new MinePlacer(dimensions).randomMines(mineCount)
  private var state = BoardState(dimensions, mines, List(), List())

  private val tileWidth = 30
  private val tileHeight = 30

  def render(implicit context: CanvasRenderingContext2D): Unit = {
    for (x <- 0 to dimensions.x; y <- 0 to dimensions.y) {
      val pos = Position(x, y)
      drawTile(pos, state.at(pos))
    }
  }

  def clickPixel(clientX: Double, clientY: Double): GameState = clickTile(toPosition(clientX, clientY))
  private def clickTile(pos: Position): GameState = {
    println("Clicking tile at "+pos)
    state = state.revealTile(pos)
    state.checkGameEnd()
  }

  private def toPosition(clientX: Double, clientY: Double) = Position( (clientX / tileWidth).toInt, (clientY / tileHeight).toInt )

  private def drawTile(pos: Position, tileState: TileState)(implicit ctx: CanvasRenderingContext2D): Unit = {
    import ctx._
    val backgroundColor = "#333333"
    def drawText(t: String) = { textBaseline = "top"; font = "28px Verdana"; fillText(t, pos.x * tileWidth+3, pos.y * tileHeight) }
    def drawRect(color: String) = { fillStyle = color; fillRect(pos.x * tileWidth, pos.y * tileHeight, tileWidth, tileHeight) }

    tileState match {
      case Unknown =>
        drawRect(backgroundColor)
        fillStyle = "#a0a0a0"
        fillRect((pos.x * tileWidth)+2, (pos.y * tileHeight)+2, tileWidth-4, tileHeight-4)
      case RevealedClear(neighbours) =>
        drawRect(backgroundColor)
        fillStyle = "#990000"
        drawText(""+neighbours)
      case RevealedMine =>
        drawRect(backgroundColor)
        fillStyle = "#990000"
        drawText("X")
      case Flagged =>
        drawRect(backgroundColor)
        drawText("F")
    }
  }

}