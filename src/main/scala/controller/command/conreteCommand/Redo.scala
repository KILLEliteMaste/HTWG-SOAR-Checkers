package controller.command.conreteCommand

import controller.command.Command
import controller.controllerbase.Controller

case class Redo() extends Command {
  override def handleCommand(input: List[String], controller: Controller): String = {
    controller.redo()
  }
}
