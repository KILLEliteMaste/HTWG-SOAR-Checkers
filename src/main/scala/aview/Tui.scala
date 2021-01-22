package aview

import controller.ControllerInterface
import controller.controllerbase.Controller
import model.GameState
import util.Observer

import scala.io.StdIn._

case class Tui(controller: ControllerInterface) extends UI with Observer {
  controller.add(this)

  def run(): Unit = {
    println("The following commands are available for the text UI:")
    println("new <Number>   | Create a new field. When a number is given as an argument the field size will be changed")
    println("save | Saves the state of the game to a .json|.xml file")
    println("load | Loades a game from an .json|.xml file")
    println("undo | Undo move")
    println("redo | redo move")
    println("quit   | Quits the game")
    println("move <X Y> <X Y X Y...>   | Move a stone <FROM> <TO> with <TO> taking multiple target positions to jump over stones")

    var input: String = ""
    update()

    controller.getGame.setGameState(GameState.RUNNING)
    while (!input.toLowerCase().equals("quit")) {
      input = readLine()
      processInputLine(input, controller)
    }
    println("Game ended")
  }

  override def update(): Unit = {
    println(controller.matrixToString)
    println(controller.getGame.getPlayerState)
  }
}
