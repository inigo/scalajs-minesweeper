package net.surguy.minesweeper

import net.surguy.minesweeper.shared._
import org.scalajs.dom.CanvasRenderingContext2D

/**
  * Displays the current state of the board and reacts to clicks.
  */
class Board(dimensions: BoardDimensions, mineCount: Int, tileWidth: Int, tileHeight: Int, debug: Boolean = false) {
  private val mines = new MinePlacer(dimensions).randomMines(mineCount)
  private var state = BoardState(dimensions, mines, List(), List())

  def render(implicit context: CanvasRenderingContext2D): Unit = {
    val boardText = new StringBuffer()
    for (y <- 0 to dimensions.y; x <- 0 to dimensions.x) {
      val pos = Position(x, y)
      if (pos.x==0) boardText.append(pos.y+" : ")
      boardText.append(state.at(pos).char+" ")
      if (pos.x==dimensions.x) boardText.append("\n")
      drawTile(pos, state.at(pos))
    }
    if (debug) {
      println("----")
      println(boardText)
      println("----")
    }
  }

  private[minesweeper] def getState = state

  def clickPixel(clientX: Double, clientY: Double): GameState = clickTile(toPosition(clientX, clientY))
  def rightClickPixel(clientX: Double, clientY: Double): Unit = rightClickTile(toPosition(clientX, clientY))

  private def clickTile(pos: Position): GameState = {
    println("Clicking tile at "+pos)
    state = state.revealTile(pos)
    state.gameState
  }
  private def rightClickTile(pos: Position) = {
    println("Flagging tile at "+pos)
    state = state.toggleFlag(pos)
  }

  private def toPosition(clientX: Double, clientY: Double) = Position( (clientX / tileWidth).toInt, (clientY / tileHeight).toInt )

  // 😀😎😞

  private def drawTile(pos: Position, tileState: TileState)(implicit ctx: CanvasRenderingContext2D): Unit = {
    import ctx._
    val backgroundColor = "#e0e0e0"
    val unknownColor = "#c0c0c0"

    def drawText(color: String, t: String) = { fillStyle=color; textBaseline = "top"; font = "28px sans-serif"; fillText(t, pos.x * tileWidth+4, pos.y * tileHeight) }
    def drawMine() = { fillStyle="black"; textBaseline = "top"; font = "24px sans-serif"; fillText("\uD83D\uDCA3", pos.x * tileWidth+2, pos.y * tileHeight+4) }
    def drawRect(color: String, offset: Int = 0) = { fillStyle = color; fillRect((pos.x * tileWidth)+offset, (pos.y * tileHeight)+offset, tileWidth-(offset*2), tileHeight-(offset*2))}

    drawRect(backgroundColor)
    tileState match {
      case Unknown => drawRect(unknownColor, 1)
      case RevealedClear(neighbours) => if (neighbours>0) drawText(colorForNeighbourCount(neighbours), ""+neighbours)
      case RevealedMine => drawMine() // 💣
      case Flagged => drawText("red", "⚑")
    }
  }

  private def colorForNeighbourCount(neighbourCount: Int) = {
    val colors = List("gray", "blue", "green", "red", "darkblue", "darkred", "darkcyan", "black", "darkgray")
    colors(neighbourCount)
  }

}