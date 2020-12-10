package controller.command.conreteCommand

import controller.Controller
import controller.command.Command

case class Redo() extends Command {
  override def handleCommand(input: List[String], controller: Controller): String = {
    controller.redo()
  }
}
