package controller.command

import controller.controllerbase.Controller

trait Command {
  def handleCommand(input: List[String], controller: Controller): String
}
