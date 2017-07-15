package net.surguy.minesweeper

import org.scalajs.dom
import org.scalajs.dom.html.Canvas

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

/**
  * Minesweeper entry point
  */
@JSExportTopLevel("minesweeper")
object Game {
  val board = new Board(BoardDimensions(9,9), mineCount = 10)

  @JSExport
  def play(canvas: Canvas): Unit = {
    val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
    board.render(ctx)

    canvas.onclick = { e =>
      val gameState = board.clickPixel(e.clientX, e.clientY)
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

sealed abstract class TileState
case class RevealedClear(neighbours: Int) extends TileState
case object RevealedMine extends TileState
case object Flagged extends TileState
case object Unknown extends TileState

case class Position(x: Int, y: Int)

case class BoardDimensions(x: Int, y: Int) {
  val totalSpaces: Int = (x+1) * (y+1)
}