package net.surguy.minesweeper

import org.scalajs.dom
import org.scalajs.dom.CanvasRenderingContext2D
import org.scalajs.dom.html.Canvas

import scala.scalajs.js.annotation.JSExport

/**
  * Minesweeper entry point
  */
@JSExport
object Game {

  val board = new Board(9,9,10)

  @JSExport
  def play(canvas: Canvas): Unit = {
    val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
    board.render(ctx)
  }

}

class Board(xSize: Int, ySize: Int, mines: Int) {

  private val tileWidth = 30
  private val tileHeight = 30

  def render(implicit context: CanvasRenderingContext2D): Unit = {
    for (x <- 0 to xSize; y <- 0 to ySize) {
      drawTile(x, y)
    }
  }

  def drawTile(x: Int, y: Int)(implicit context: CanvasRenderingContext2D): Unit = {
    context.fillStyle = "#333333"
    context.fillRect(x * tileWidth, y * tileHeight, tileWidth, tileHeight)
    context.fillStyle = "#a0a0a0"
    context.fillRect((x * tileWidth)+2, (y * tileHeight)+2, tileWidth-4, tileHeight-4)
  }

}
