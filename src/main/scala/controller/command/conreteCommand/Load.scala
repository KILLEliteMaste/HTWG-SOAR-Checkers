package controller.command.conreteCommand

import controller.ControllerInterface
import controller.command.Command

case class Load() extends Command:
  override def handleCommand(input: List[String], controller: ControllerInterface): String = controller.load()
