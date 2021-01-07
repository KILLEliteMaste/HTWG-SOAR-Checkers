package controller.command.conreteCommand

import controller.command.Command
import controller.controllerbase.Controller

case class Undo() extends Command {
  override def handleCommand(input: List[String], controller: Controller): String = {
    controller.undo()
  }
}
