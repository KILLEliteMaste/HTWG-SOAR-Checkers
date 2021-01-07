package aview

import controller.command.conreteCommand.{Move, New, Redo, Undo}
import controller.command.Command
import controller.controllerbase.Controller
import scalafx.beans.property.StringProperty

import scala.collection.mutable

abstract class UI extends UserInterface {
  val commands = new mutable.HashMap[String, Command]()
  commands.put("new", New())
  commands.put("move", Move())
  commands.put("undo", Undo())
  commands.put("redo", Redo())
  var returnMessage: StringProperty = new StringProperty() {
    onChange { (_, oldValue, newValue) => println(newValue) }
  }

  def processInputLine(input: String, controller: Controller): String = {
    val inputSplit = input.toLowerCase().split("\\s+").toList
    returnMessage.value = commands.get(inputSplit.head).map(command => command.handleCommand(inputSplit.drop(1), controller)).getOrElse("No matching command found")
    returnMessage.value
  }

  def run(): Unit
}
