package controller.command.conreteCommand

import controller.command.Command
import controller.ControllerInterface

case class Redo() extends Command {
  override def handleCommand(input: List[String], controller: ControllerInterface): String = {
    controller.redo()
  }
}
