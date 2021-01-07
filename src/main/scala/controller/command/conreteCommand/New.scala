package controller.command.conreteCommand

import controller.command.Command
import controller.controllerbase.Controller

case class New() extends Command {
  override def handleCommand(input: List[String],controller: Controller): String = {
    controller.doStep()
    if (input.isEmpty) {
      controller.createNewField()
    } else {
      controller.createNewField(input.head.toInt)
    }
    "Created a new field with size: " + controller.field.getFieldSize
  }
}
