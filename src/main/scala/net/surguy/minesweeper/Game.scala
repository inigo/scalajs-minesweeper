package net.surguy.minesweeper

import org.scalajs.dom
import org.scalajs.dom.CanvasRenderingContext2D
import org.scalajs.dom.html.Canvas

import scala.collection.mutable.ArrayBuffer
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.util.Random

/**
  * Minesweeper entry point
  */
@JSExportTopLevel("minesweeper")
object Game {

  val board = new Board(9,9,10)

  @JSExport
  def play(canvas: Canvas): Unit = {
    val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
    board.render(ctx)
  }
}

case class Position(x: Int, y: Int)
case class BoardDimensions(x: Int, y: Int) {
  val totalSpaces: Int = (x+1) * (y+1)
}

abstract class TileState
case class Revealed(neighbours: Int) extends TileState
case object Flagged extends TileState
case object Unknown extends TileState

case class BoardState(dimensions: BoardDimensions, mines: List[Position], revealedTiles: List[Position], flaggedTiles: List[Position]) {
  def at(pos: Position): TileState = {
    pos match {
      case _ if revealedTiles.contains(pos) => Revealed(calculateNeighbours(pos))
      case _ if flaggedTiles.contains(pos) => Flagged
      case _  => Unknown
    }
  }

  private def calculateNeighbours(pos: Position) = 0

  def revealTile(pos: Position): BoardState = this.copy(revealedTiles = pos :: revealedTiles )
  def flagTile(pos: Position): BoardState = this.copy(flaggedTiles = pos :: flaggedTiles )
}

class Board(xSize: Int, ySize: Int, mineCount: Int) {

  private val dimensions = BoardDimensions(xSize, ySize)
  private val mines = new MinePlacer(dimensions).randomMines(mineCount)
  private var state = BoardState(dimensions, mines, List(), List())

  private val tileWidth = 30
  private val tileHeight = 30

  def render(implicit context: CanvasRenderingContext2D): Unit = {
    for (x <- 0 to xSize; y <- 0 to ySize) {
      val pos = Position(x, y)
      drawTile(pos, state.at(pos))
    }
  }

  def drawTile(pos: Position, tileState: TileState)(implicit context: CanvasRenderingContext2D): Unit = {
    tileState match {
      case Unknown =>
        context.fillStyle = "#333333"
        context.fillRect(pos.x * tileWidth, pos.y * tileHeight, tileWidth, tileHeight)
        context.fillStyle = "#a0a0a0"
        context.fillRect((pos.x * tileWidth)+2, (pos.y * tileHeight)+2, tileWidth-4, tileHeight-4)
      case Revealed(neighbours) =>
        context.fillStyle = "#333333"
        context.fillRect(pos.x * tileWidth, pos.y * tileHeight, tileWidth, tileHeight)
        context.fillStyle = "#990000"
        context.fillText(""+neighbours, pos.x * tileWidth, pos.y * tileHeight)
      case _ =>
        context.fillStyle = "#333333"
        context.fillRect(pos.x * tileWidth, pos.y * tileHeight, tileWidth, tileHeight)
        context.fillStyle = "#990000"
        context.fillText("F", pos.x * tileWidth, pos.y * tileHeight)
    }

  }
}

class MinePlacer(dimensions: BoardDimensions) {
  def randomMines(count: Int): List[Position] = {
    if (count > dimensions.totalSpaces) throw new IllegalArgumentException("Trying to create more mines than there are spaces")

    val minesSoFar = new ArrayBuffer[Position]()
    for (_ <- 0 to count) {
      var newMine: Position = null
      do {
        newMine = randomPosition()
      } while (minesSoFar.contains(newMine))
      minesSoFar += newMine
    }
    minesSoFar.toList
  }

  def randomPosition() = Position(Random.nextInt(dimensions.x), Random.nextInt(dimensions.y))
}
