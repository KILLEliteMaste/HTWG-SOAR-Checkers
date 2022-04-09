package de.htwg.se.checkers.controller.command.conreteCommand

import de.htwg.se.checkers.controller.ControllerInterface
import de.htwg.se.checkers.controller.command.Command

case class Load() extends Command:
  override def handleCommand(input: List[String], controller: ControllerInterface): String = controller.load()
