package aview


import controller.Controller
import controller.command.conreteCommand.{Move, New, Undo, Redo}
import controller.command.Command

import scala.collection.mutable

abstract class UI extends UserInterface {
  val commands = new mutable.HashMap[String, Command]()
  commands.put("new", New())
  commands.put("move", Move())
  commands.put("undo", Undo())
  commands.put("redo", Redo())

  def processInputLine(input: String, controller: Controller): Unit = {
    val inputSplit = input.toLowerCase().split("\\s+").toList

    commands.get(inputSplit.head).foreach(command => println(command.handleCommand(inputSplit.drop(1), controller)))
  }

  def run(): Unit
}
