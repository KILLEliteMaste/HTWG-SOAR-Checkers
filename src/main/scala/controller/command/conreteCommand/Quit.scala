package controller.command.conreteCommand

import controller.Controller
import controller.command.Command

case class Quit() extends Command {
  override def handleCommand(input: List[String], controller: Controller): String = {
    System.exit(0)
    "BYE"
  }
}
