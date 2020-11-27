package aview

import controller.Controller
import model.Position
import util.Observer

import scala.io.StdIn._

class TUI(controller: Controller) extends Observer {

  controller.add(this)

  val numberRegex = "(\\d+)"
  val splitAtRegex = "\\s+"

  def processInputLine(input: String): Unit = {
    val player = controller.player
    val inputSplit = input.split(splitAtRegex).toList

    inputSplit match {
      case "new" :: Nil => controller.createNewField()
      case "q" :: Nil =>
      case _ => {
        //ORIGIN POSITION
        if (!isOriginInputValid(inputSplit)) return
        val origin = Vector(Position(inputSplit.head.toInt, inputSplit(1).toInt))

        val (checkIfAllPositionsAreInBounds, checkIfAllPositionsAreInBoundsStr) = controller.checkIfAllPositionsAreInBounds(origin, controller.field)
        if (!checkIfAllPositionsAreInBounds) {
          println(checkIfAllPositionsAreInBoundsStr)
          return
        }

        val (checkIfAllCellsBelongToPlayer, checkIfAllCellsBelongToPlayerStr) = controller.checkIfAllCellsBelongToPlayer(player, controller.field, origin)
        if (!checkIfAllCellsBelongToPlayer) {
          println(checkIfAllCellsBelongToPlayerStr)
          return
        }

        println("MOVE FROM: " + inputSplit.head + " " + inputSplit(1) + " to:")

        //DESTINATION POSITIONS
        val destinationInput = readLine("Choose destinations:\n").split(splitAtRegex).toList
        if (!isDestinationInputValid(destinationInput)) return

        val vectorBuilder = Vector.newBuilder[Position]
        for (elem <- destinationInput.sliding(2, 2)) {
          vectorBuilder.+=(Position(elem.head.toInt, elem(1).toInt))
        }
        val destinations = vectorBuilder.result()


        val (checkIfAllPositionsAreInBoundsDestinations, checkIfAllPositionsAreInBoundsStrDestinations) = controller.checkIfAllPositionsAreInBounds(destinations, controller.field)
        if (!checkIfAllPositionsAreInBoundsDestinations) {
          println(checkIfAllPositionsAreInBoundsStrDestinations)
          return
        }

        //Has to be empty otherwise you cannot move to this position
        val (checkIfAllCellsAreEmpty, checkIfAllCellsAreEmptyStr) = controller.checkIfAllCellsAreEmpty(controller.field, destinations)
        if (!checkIfAllCellsAreEmpty) {
          println(checkIfAllCellsAreEmptyStr)
          return
        }

        //Will always be executed as it the least amount you want to jump
        controller.moveFromPositionToPosition(origin(0), destinations(0), controller.field.matrix.cell(origin(0).x, origin(0).y).value, alreadyMoved = false)

        if (destinations.size != 1) {
          for (elem <- destinations.sliding(2, 1)) {
            controller.moveFromPositionToPosition(elem(0), elem(1), controller.field.matrix.cell(elem(0).x, elem(0).y).value, alreadyMoved = true)
          }
        }

        println("TO: " + destinationInput.head + "   " + destinationInput(1))
        println(controller.changePlayerTurn())
      }
    }
  }

  def isDestinationInputValid(input: List[String]): Boolean = {
    if (input.size % 2 == 0) {
      if (allStringsMatchNumber(input))
        return true
    } else {
      println("Wrong amount of arguments for the destination position")
    }
    false
  }

  def isOriginInputValid(input: List[String]): Boolean = {
    if (input.size == 2) {
      if (allStringsMatchNumber(input))
        return true
    } else {
      println("Wrong amount of arguments for the origin position")
    }
    false
  }

  def allStringsMatchNumber(list: List[String]): Boolean = {
    for (elem <- list) {
      if (!elem.matches(numberRegex)) {
        println("One or more arguments do not match a number")
        return false
      }
    }
    true
  }

  override def update(): Unit = println(controller.matrixToString)
}
