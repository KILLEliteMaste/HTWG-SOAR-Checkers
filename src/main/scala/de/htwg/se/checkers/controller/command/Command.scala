package de.htwg.se.checkers.controller.command

import de.htwg.se.checkers.controller.ControllerInterface
import de.htwg.se.checkers.controller.controllerbase.Controller

trait Command {
  def handleCommand(input: List[String], controller: ControllerInterface): String
}
