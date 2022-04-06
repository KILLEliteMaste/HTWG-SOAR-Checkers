package aview

import controller.ControllerInterface
import controller.command.conreteCommand.{Save, Load, Move, New, Redo, Undo}
import controller.command.Command
import scalafx.beans.property.StringProperty

abstract class UI extends UserInterface {
  val commands = Map[String, Command](
    "new" -> New(),
    "move" -> Move(),
    "undo" -> Undo(),
    "redo" -> Redo(),
    "load" -> Load(),
    "save" -> Save())
  var returnMessage: StringProperty = new StringProperty() {
    onChange { (_, oldValue, newValue) => println(newValue) }
  }

  def processInputLine(input: String, controller: ControllerInterface): String = {
    val inputSplit = input.toLowerCase().split("\\s+").toList
    returnMessage.value = commands.get(inputSplit.head).map(command => command.handleCommand(inputSplit.drop(1), controller))
      .getOrElse("No matching command found")

    returnMessage.value
  }

  def run(): Unit
}
