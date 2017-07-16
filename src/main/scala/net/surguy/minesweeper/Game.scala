package net.surguy.minesweeper

import org.scalajs.dom
import org.scalajs.dom.html.Canvas

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scalatags.JsDom.all._

/**
  * Minesweeper entry point
  */
@JSExportTopLevel("minesweeper")
object Game {
  @JSExport
  def play(canvas: Canvas): Unit = {
    gameSelector()
  }

  private val gameSizes = List(SmallGame, IntermediateGame, LargeGame)
  private val tileWidth = 30
  private val tileHeight = 30

  private def gameSelector() = {
    val ms = dom.document.getElementById("minesweeper")
    val msBoard = div().render

    val selectNode = select(gameSizes.map(gs => option(value:=gs.name, gs.name)) ).render

    def createCanvas() = {
      println("Starting game")

      while (msBoard.hasChildNodes()) { msBoard.removeChild(msBoard.childNodes(0))  }

      val gameSizeText = selectNode.childNodes.item(selectNode.selectedIndex).textContent
      val gameSize = gameSizes.find(_.name==gameSizeText).head
      val canvasNode = canvas ( style := "display: block").render
      canvasNode.setAttribute("width", ""+gameSize.dimensions.x * tileWidth)  // Doesn't work when setting width/height directly via the tag?
      canvasNode.setAttribute("height", ""+gameSize.dimensions.y * tileHeight)

      msBoard.appendChild(canvasNode)
      startGame(canvasNode, gameSize)
    }

    val controls = div( selectNode, button(onclick := { () => createCanvas() }, "Start")).render

    ms.appendChild(controls)
    ms.appendChild(msBoard)
  }

  private def startGame(canvas: Canvas, gameSize: GameSize) = {
    val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
    val board = new Board(gameSize.dimensions, gameSize.mineCount, tileWidth, tileHeight)

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