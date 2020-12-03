package controller.command.conreteCommand

import controller.Controller
import controller.command.UndoableCommand
import model.Position

case class Move() extends UndoableCommand {

  val numberRegex = "(\\d+)"
  val splitAtRegex = "\\s+"
  var statusMessage = ""

  override def handleCommand(input: List[String], controller: Controller): String = {
    //ORIGIN POSITION
    if (!isOriginInputValid(input)) return statusMessage
    val origin = Vector(Position(input.head.toInt, input(1).toInt))

    val checkIfAllPositionsAreInBounds = controller.checkIfAllPositionsAreInBounds(origin, controller.field)

    if (!checkIfAllPositionsAreInBounds) {
      return controller.statusMessage
    }

    val checkIfAllCellsBelongToPlayer = controller.checkIfAllCellsBelongToPlayer(controller.field, origin)
    if (!checkIfAllCellsBelongToPlayer) {
      return controller.statusMessage
    }
    statusMessage = "MOVE FROM: " + input.head + " " + input(1) + " to:"

    //DESTINATION POSITIONS

    val destinationInput = input.drop(2)
    if (!isDestinationInputValid(destinationInput)) return statusMessage

    val vectorBuilder = Vector.newBuilder[Position]
    for (elem <- destinationInput.sliding(2, 2)) {
      vectorBuilder.+=(Position(elem.head.toInt, elem(1).toInt))
    }
    val destinations = vectorBuilder.result()


    val checkIfAllPositionsAreInBoundsDestinations = controller.checkIfAllPositionsAreInBounds(destinations, controller.field)
    if (!checkIfAllPositionsAreInBoundsDestinations) {
      return controller.statusMessage
    }

    //Has to be empty otherwise you cannot move to this position
    val checkIfAllCellsAreEmpty = controller.checkIfAllCellsAreEmpty(controller.field, destinations)
    if (!checkIfAllCellsAreEmpty) {
      return controller.statusMessage
    }

    //Will always be executed as it the least amount you want to jump
    val m1 = controller.field.matrix.toString
    controller.moveFromPositionToPosition(origin(0), destinations(0), controller.field.matrix.cell(origin(0).x, origin(0).y).value, alreadyMoved = false)
    if (m1.equals(controller.field.matrix.toString))
      return "Could not execute move"

    if (destinations.size != 1) {
      for (elem <- destinations.sliding(2, 1)) {
        controller.moveFromPositionToPosition(elem(0), elem(1), controller.field.matrix.cell(elem(0).x, elem(0).y).value, alreadyMoved = true)
      }
    }

    controller.changePlayerTurn()
    statusMessage += "TO: " + destinationInput.head + "   " + destinationInput(1)
    statusMessage
  }

  override def undo(input: List[String]): String = {
    "UNDO the last move"
  }

  def isDestinationInputValid(input: List[String]): Boolean = {
    if (input.size % 2 == 0) {
      if (allStringsMatchNumber(input))
        return true
    } else {
      statusMessage = "Wrong amount of arguments for the destination position"
    }
    false
  }

  def isOriginInputValid(input: List[String]): Boolean = {
    if (input.size >= 2) {
      if (allStringsMatchNumber(input))
        return true
    }
    false
  }

  def allStringsMatchNumber(list: List[String]): Boolean = {
    for (elem <- list) {
      if (!elem.matches(numberRegex)) {
        statusMessage = "One or more arguments do not match a number"
        return false
      }
    }
    true
  }
}
