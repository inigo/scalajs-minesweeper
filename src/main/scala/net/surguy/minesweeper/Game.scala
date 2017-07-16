package net.surguy.minesweeper

import org.scalajs.dom
import org.scalajs.dom.html.Canvas

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

/**
  * Minesweeper entry point
  */
@JSExportTopLevel("minesweeper")
object Game {
  val board = new Board(BoardDimensions(8,8), mineCount = 10)

  @JSExport
  def play(canvas: Canvas): Unit = {
    val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
    board.render(ctx)
    canvas.oncontextmenu = { e =>
      board.rightClickPixel(e.clientX - canvas.offsetLeft, e.clientY - canvas.offsetTop)
      board.render(ctx)
      false
    }
    canvas.onclick = { e =>
      val gameState = board.clickPixel(e.clientX - canvas.offsetLeft, e.clientY - canvas.offsetTop)
      board.render(ctx)
      gameState match {
        case Won => dom.window.alert("Congratulations!")
        case Lost => dom.window.alert("Boom!")
        case _ => // Continue
      }
    }
  }
}

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