package de.htwg.se.checkers.aview

import de.htwg.se.checkers.controller.command.conreteCommand.{Load, Move, New, Redo, Save, SaveDB, Undo, LoadDB}
import de.htwg.se.checkers.controller.ControllerInterface
import de.htwg.se.checkers.controller.command.Command
import scalafx.beans.property.StringProperty

abstract class UI extends UserInterface {
  val commands = Map[String, Command](
    "new" -> New(),
    "move" -> Move(),
    "undo" -> Undo(),
    "redo" -> Redo(),
    "load" -> Load(),
    "save" -> Save(),
    "loaddb" -> LoadDB(),
    "savedb" -> SaveDB()
  )
  
  var returnMessage: StringProperty = new StringProperty() {
    onChange { (_, oldValue, newValue) => println(newValue) }
  }

  def processInputLine(input: String, controller: ControllerInterface): String = Option(input).map(processInput(_, controller)).getOrElse("Could not process Command")

  def processInput(input: String, controller: ControllerInterface): String = synchronized {
    val inputSplit = input.toLowerCase().split("\\s+").toList
    returnMessage.value = commands.get(inputSplit.head).map(command => command.handleCommand(inputSplit.drop(1), controller))
      .getOrElse("No matching command found")

    returnMessage.value
  }

  def run(): Unit
}
