package aview

import controller.ControllerInterface
import controller.command.conreteCommand.{Save, Load, Move, New, Redo, Undo}
import controller.command.Command
import scalafx.beans.property.StringProperty

import scala.collection.mutable

abstract class UI extends UserInterface {
  val commands = new mutable.HashMap[String, Command]()
  commands.put("new", New())
  commands.put("move", Move())
  commands.put("undo", Undo())
  commands.put("redo", Redo())
  commands.put("load", Load())
  commands.put("save", Save())
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
