package controller.command.conreteCommand

import controller.ControllerInterface
import controller.command.Command
import util.Position

case class Move() extends Command {

  val numberRegex = "(\\d{1,2})"
  val splitAtRegex = "\\s+"
  var statusMessage = ""

  override def handleCommand(input: List[String], controller: ControllerInterface): String = {
    //ORIGIN POSITION
    if !isOriginInputValid(input) then return statusMessage

    val origin = Vector(Position(input.head.toInt, input(1).toInt))

    if (!controller.checkIfAllPositionsAreInBounds(origin, controller.getGame.getField)) {
      return controller.getGame.getStatusMessage
    }

    if (!controller.checkIfAllCellsBelongToPlayer(controller.getGame.getField, origin)) {
      return controller.getGame.getStatusMessage
    }
    statusMessage = "MOVE FROM: " + input.head + " " + input(1) + " "

    //DESTINATION POSITIONS
    val destinationInput = input.drop(2)
    if (!isDestinationInputValid(destinationInput)) return statusMessage

    val vectorBuilder = Vector.newBuilder[Position]
    for (elem <- destinationInput.sliding(2, 2)) {
      vectorBuilder.+=(Position(elem.head.toInt, elem(1).toInt))
    }
    val destinations = vectorBuilder.result()


    val checkIfAllPositionsAreInBoundsDestinations = controller.checkIfAllPositionsAreInBounds(destinations, controller.getGame.getField)
    if (!checkIfAllPositionsAreInBoundsDestinations) {
      return controller.getGame.getStatusMessage
    }

    //Has to be empty otherwise you cannot move to this position
    val checkIfAllCellsAreEmpty = controller.checkIfAllCellsAreEmpty(controller.getGame.getField, destinations)
    if (!checkIfAllCellsAreEmpty) {
      return controller.getGame.getStatusMessage
    }

    controller.doStep()

    //Will always be executed as it the least amount you want to jump
    val m1 = controller.getGame.getField.getFieldMatrix.toString
    controller.moveFromPositionToPosition(origin(0), destinations(0), controller.getGame.getField.getFieldMatrix.cell(origin(0).x, origin(0).y).map(cell => cell.getValue).getOrElse(0), alreadyMoved = false)
    if (m1.equals(controller.getGame.getField.getFieldMatrix.toString))
      return "Could not execute move"
    end if

    if destinations.size != 1 then
      for (elem <- destinations.sliding(2, 1)) {
        controller.moveFromPositionToPosition(elem(0), elem(1), controller.getGame.getField.getFieldMatrix.cell(elem(0).x, elem(0).y).map(cell => cell.getValue).getOrElse(0), alreadyMoved = true)
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
