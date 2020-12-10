package controller.command

import controller.Controller

trait Command {
  def handleCommand(input: List[String], controller: Controller): String
}
