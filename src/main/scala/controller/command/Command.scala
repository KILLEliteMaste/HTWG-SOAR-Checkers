package controller.command

import controller.ControllerInterface
import controller.controllerbase.Controller

trait Command {
  def handleCommand(input: List[String], controller: ControllerInterface): String
}
