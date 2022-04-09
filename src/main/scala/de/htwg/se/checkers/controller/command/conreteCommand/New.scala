package de.htwg.se.checkers.controller.command.conreteCommand

import de.htwg.se.checkers.controller.ControllerInterface
import de.htwg.se.checkers.controller.command.Command

case class New() extends Command {

  override def handleCommand(input: List[String], controller: ControllerInterface): String = {
    controller.doStep()
    if (input.isEmpty) {
      controller.createNewField()
    } else {
      input.head.toInt match {
        case 8 => controller.createNewField(8)
        case 10 => controller.createNewField(10)
        case 12 => controller.createNewField(12)
      }

    }
    "Created a new field with size: " + controller.getGame.field.fieldSize
  }
}
