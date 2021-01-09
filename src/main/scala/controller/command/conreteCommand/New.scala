package controller.command.conreteCommand

import controller.ControllerInterface
import controller.command.Command
import controller.controllerbase.Controller

case class New() extends Command {
  override def handleCommand(input: List[String],controller: ControllerInterface): String = {
    controller.doStep()
    if (input.isEmpty) {
      controller.createNewField()
    } else {
      controller.createNewField(input.head.toInt)
    }
    "Created a new field with size: " + controller.getGame.getField.getFieldSize
  }
}
