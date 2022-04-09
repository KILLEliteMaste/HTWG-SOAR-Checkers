package de.htwg.se.checkers.controller.command.conreteCommand

import de.htwg.se.checkers.controller.ControllerInterface
import de.htwg.se.checkers.controller.command.Command
import de.htwg.se.checkers.util.Position

case class Move() extends Command {

  val numberRegex = "(\\d{1,2})"
  val splitAtRegex = "\\s+"
  var statusMessage = ""

  override def handleCommand(input: List[String], controller: ControllerInterface): String = {
    //ORIGIN POSITION
    if !isOriginInputValid(input) then return statusMessage

    val origin = Vector(Position(input.head.toInt, input(1).toInt))

    if !controller.checkIfAllPositionsAreInBounds(origin, controller.getGame.field) then
      return controller.getGame.statusMessage
    end if

    if !controller.checkIfAllCellsBelongToPlayer(controller.getGame.field, origin) then
      return controller.getGame.statusMessage
    end if

    statusMessage = "MOVE FROM: " + input.head + " " + input(1) + " "

    //DESTINATION POSITIONS
    val destinationInput = input.drop(2)
    if !isDestinationInputValid(destinationInput) then return statusMessage

    val vectorBuilder = Vector.newBuilder[Position]
    for (elem <- destinationInput.sliding(2, 2)) {
      vectorBuilder.+=(Position(elem.head.toInt, elem(1).toInt))
    }
    val destinations = vectorBuilder.result()


    val checkIfAllPositionsAreInBoundsDestinations = controller.checkIfAllPositionsAreInBounds(destinations, controller.getGame.field)
    if !checkIfAllPositionsAreInBoundsDestinations then
      return controller.getGame.statusMessage
    end if

    //Has to be empty otherwise you cannot move to this position
    val checkIfAllCellsAreEmpty = controller.checkIfAllCellsAreEmpty(controller.getGame.field, destinations)
    if !checkIfAllCellsAreEmpty then
      return controller.getGame.statusMessage
    end if

    controller.doStep()

    //Will always be executed as it the least amount you want to jump
    val m1 = controller.getGame.field.fieldMatrix.toString
    controller.moveFromPositionToPosition(origin(0), destinations(0), controller.getGame.field.fieldMatrix.cell(origin(0).x, origin(0).y).map(cell => cell.value).getOrElse(0), alreadyMoved = false)
    if m1.equals(controller.getGame.field.fieldMatrix.toString) then
      return "Could not execute move"
    end if

    if destinations.size != 1 then
      for (elem <- destinations.sliding(2, 1)) {
        controller.moveFromPositionToPosition(elem(0), elem(1), controller.getGame.field.fieldMatrix.cell(elem(0).x, elem(0).y).map(cell => cell.value).getOrElse(0), alreadyMoved = true)
      }
    end if

    controller.changePlayerTurn()
    statusMessage += "TO: " + destinationInput.head + " " + destinationInput(1)
    statusMessage
  }

  def isDestinationInputValid(input: List[String]): Boolean = {
    if input.size % 2 == 0 then
      if allStringsMatchNumber(input) then
        return true
      else
        statusMessage = "Wrong amount of arguments for the destination position"
      end if
    end if

    false
  }

  def isOriginInputValid(input: List[String]): Boolean = {
    if input.size >= 4 then
      if allStringsMatchNumber(input) then
        return true

    statusMessage = "You need at least a start and end position"
    false
  }

  def allStringsMatchNumber(list: List[String]): Boolean = {
    for (elem <- list) {
      if !elem.matches(numberRegex) then
        statusMessage = "One or more arguments do not match a number or are too long"
        return false
    }
    true
  }
}
