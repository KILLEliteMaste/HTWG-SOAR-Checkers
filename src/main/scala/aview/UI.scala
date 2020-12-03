package aview


import controller.{Controller, UserInterface}
import controller.command.conreteCommand.{Move, New, Quit}
import controller.command.{Command, UndoableCommand}

import scala.collection.mutable

abstract class UI extends UserInterface {
  val commands = new mutable.HashMap[String, Command]()
  val undoableCommand = new mutable.HashMap[String, UndoableCommand]()
  commands.put("new", New())
  commands.put("quit", Quit())
  commands.put("move", Move())
  undoableCommand.put("undo", Move())

  def processInputLine(input: String, controller: Controller): Unit = {
    val inputSplit = input.toLowerCase().split("\\s+").toList

    commands.get(inputSplit.head).foreach(command => println(command.handleCommand(inputSplit.drop(1), controller)))
    undoableCommand.get(inputSplit.head).foreach(command => command.undo(inputSplit.drop(1)))
  }

  def run(): Unit
}
