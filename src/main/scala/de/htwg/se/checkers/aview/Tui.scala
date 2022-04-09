package de.htwg.se.checkers.aview

import de.htwg.se.board.GameState
import de.htwg.se.checkers.controller.ControllerInterface
import de.htwg.se.checkers.controller.controllerbase.Controller
import de.htwg.se.checkers.util.Observer

import scala.io.StdIn.*

case class Tui(controller: ControllerInterface) extends UI with Observer {
  controller.add(this)

  def run(): Unit = {
    println("The following commands are available for the text UI:")
    println("new <Number>   | Create a new field. When a number is given as an argument the field size will be changed")
    println("save | Saves the state of the game to a .json|.xml file")
    println("load | Loads a game from an .json|.xml file")
    println("undo | Undo move")
    println("redo | redo move")
    println("quit   | Quits the game")
    println("move <X Y> <X Y X Y...>   | Move a stone <FROM> <TO> with <TO> taking multiple target positions to jump over stones")

    var input: String = ""
    update()

    controller.setGame(controller.getGame.recreate(gameState = GameState.RUNNING))
    while (!input.toLowerCase().equals("quit")) {
      input = readLine()
      processInputLine(input, controller)
    }
    println("Game ended")
  }

  override def update(): Unit = {
    println(controller.matrixToString)
    println(controller.getGame.playerState)
  }
}
