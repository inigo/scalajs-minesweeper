package net.surguy.minesweeper

import net.surguy.minesweeper.shared._
import org.scalajs.dom
import org.scalajs.dom.MouseEvent
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
  val tileWidth = 30
  val tileHeight = 30

  private def gameSelector() = {
    val ms = dom.document.getElementById("minesweeper")
    val msBoard = div().render

    val selectNode = select(gameSizes.map(gs => option(value:=gs.name, gs.name)) ).render

    def createCanvas() = {
      println("Starting game")

      while (msBoard.hasChildNodes()) { msBoard.removeChild(msBoard.childNodes(0))  }

      val gameSizeText = selectNode.childNodes.item(selectNode.selectedIndex).textContent
      val gameSize = gameSizes.find(_.name==gameSizeText).head
      val canvasNode: Canvas = createCanvasNode(gameSize)

      msBoard.appendChild(canvasNode)
      startGame(canvasNode, gameSize)
    }

    val controls = div( selectNode, button(onclick := { () => createCanvas() }, "Start")).render

    ms.appendChild(controls)
    ms.appendChild(msBoard)
  }

  private[minesweeper] def createCanvasNode(gameSize: GameSize) = {
    val canvasNode = canvas(style := "display: block").render
    canvasNode.setAttribute("width", "" + gameSize.dimensions.x * tileWidth) // Doesn't work when setting width/height directly via the tag?
    canvasNode.setAttribute("height", "" + gameSize.dimensions.y * tileHeight)
    canvasNode
  }

  private[minesweeper] def startGame(canvas: Canvas, gameSize: GameSize) = {
    val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
    val board = new Board(gameSize.dimensions, gameSize.mineCount, tileWidth, tileHeight)

    board.render(ctx)
    canvas.oncontextmenu = { e: MouseEvent =>
      board.rightClickPixel(e.clientX - canvas.offsetLeft, e.clientY - canvas.offsetTop)
      board.render(ctx)
      false
    }
    canvas.onclick = { e: MouseEvent =>
      val gameState = board.clickPixel(e.clientX - canvas.offsetLeft, e.clientY - canvas.offsetTop)
      board.render(ctx)
      gameState match {
        case Won => dom.window.alert("Congratulations!")
        case Lost => dom.window.alert("Boom!")
        case _ => // Continue
      }
    }
    board
  }
}
