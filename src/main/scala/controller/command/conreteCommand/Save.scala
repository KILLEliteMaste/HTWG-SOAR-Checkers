package controller.command.conreteCommand

import controller.ControllerInterface
import controller.command.Command

case class Save() extends Command {
  override def handleCommand(input: List[String],controller: ControllerInterface): String = {
    controller.save()
    "SAVED"
  }
}

