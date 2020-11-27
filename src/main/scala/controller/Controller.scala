package controller

import model.{Cell, Field, FieldMatrix, Position}
import util.Observable

case class Controller(var field: Field) extends Observable {
  var updated = false
  var ret: Int = 0
  var player: Int = 1

  def createNewField(): Unit = {
    field = Field(field.fieldSize)
    player = 1
  }

  def createNewField(size: Int): Unit = {
    field = Field(size)
    player = 1
  }

  def changePlayerTurn(): String = {
    if (player == 1) player = 2 else player = 1
    notifyObservers()
    "It's player " + player + " turn"
  }

  def isStoneOpponentsColor(stoneToJumpOver: Int, stoneToMove: Int): Boolean = {
    if (stoneToMove == 1 || stoneToMove == 2) {
      stoneToJumpOver == 3 || stoneToJumpOver == 4
    } else {
      stoneToJumpOver == 1 || stoneToJumpOver == 2
    }
  }

  def caseJumpOverStone(positionFrom: Position, positionTo: Position, stoneToMove: Int, x: Int, y: Int): Unit = {
    val value = field.matrix.cell(positionFrom.x + x, positionFrom.y + y).value
    if (isStoneOpponentsColor(value, stoneToMove)) {
      field.matrix = moveToNewPosition(positionFrom, positionTo, field).replaceCell(positionFrom.x + x, positionFrom.y + y, Cell(0))
    }
  }

  def moveFromPositionToPosition(positionFrom: Position, positionTo: Position, stoneToMove: Int, alreadyMoved: Boolean): Unit = {
    val differenceX = positionTo.x - positionFrom.x
    val differenceY = positionTo.y - positionFrom.y

    List(differenceX, differenceY) match {
      //WHITE
      case 1 :: -1 :: Nil => if (stoneToMove == 1 && field.matrix.cell(positionTo.x, positionTo.y).value == 0 && !alreadyMoved) field.matrix = moveToNewPosition(positionFrom, positionTo, field)
      case 1 :: 1 :: Nil => if (stoneToMove == 1 && field.matrix.cell(positionTo.x, positionTo.y).value == 0 && !alreadyMoved) field.matrix = moveToNewPosition(positionFrom, positionTo, field)
      //BLACK
      case -1 :: -1 :: Nil => if (stoneToMove == 3 && field.matrix.cell(positionTo.x, positionTo.y).value == 0 && !alreadyMoved) field.matrix = moveToNewPosition(positionFrom, positionTo, field)
      case -1 :: 1 :: Nil => if (stoneToMove == 3 && field.matrix.cell(positionTo.x, positionTo.y).value == 0 && !alreadyMoved) field.matrix = moveToNewPosition(positionFrom, positionTo, field)
      //JUMP OVER STONE
      case _ :: _ :: Nil => {
        if (Math.abs(differenceX) == 2 && Math.abs(differenceY) == 2)
          caseJumpOverStone(positionFrom, positionTo, stoneToMove, differenceX / 2, differenceY / 2)
      }
    }
    for (i <- 0 until field.fieldSize) {
      if (field.matrix.rows(field.fieldSize - 1)(i).value == 1) {
        field.matrix = moveToNewPosition(positionTo, positionTo, field).replaceCell(field.fieldSize - 1, i, Cell(2))
      }
      if (field.matrix.rows(0)(i).value == 3) {
        field.matrix = moveToNewPosition(positionTo, positionTo, field).replaceCell(0, i, Cell(4))
      }
    }
    if ((stoneToMove == 2 || stoneToMove == 4) && !alreadyMoved) {
      if (Math.abs(differenceX).equals(Math.abs(differenceY))) {
        //Nur laufen
        //Laufen mit eigener stein
        //Laufen mit gegnerischer stein (jump)
        val directionX = if (differenceX < 0) -1 else 1
        val directionY = if (differenceY < 0) -1 else 1
        var posX = directionX
        var posY = directionY
        var i = 0
        while (i < differenceX) {
          if (field.matrix.rows(positionFrom.x + posX)(positionFrom.y + posY).value != 0) {
            if (isStoneOpponentsColor(field.matrix.rows(positionFrom.x + posX)(positionFrom.y + posY).value, stoneToMove)) {
              if (positionFrom.x + posX + posX != positionTo.x) {
                field.matrix = moveToNewPosition(positionFrom, positionTo, field)
                return //true
              } else {
                return //false
              }
            } else {
              return
            }
          }
          posX = posX + directionX
          posY = posY + directionY
          i = i + 1
        }
        moveToNewPosition(positionFrom, positionTo, field)
      } else {
        println("CANNOT EXECUTE")
      }
    }
  }

  /**
   *
   * @param origin      The Origin position to move from
   * @param destination The destination position to move to
   * @param field       The field which will be moved in
   * @return A new FieldMatrix
   */
  def moveToNewPosition(origin: Position, destination: Position, field: Field): FieldMatrix[Cell] = {
    val cellOrigin = field.matrix.cell(origin.x, origin.y)
    val cellDestination = field.matrix.cell(destination.x, destination.y)

    field.matrix = field.matrix.replaceCell(destination.x, destination.y, cellOrigin)
    field.matrix = field.matrix.replaceCell(origin.x, origin.y, cellDestination)
    field.matrix
  }

  /**
   *
   * @param vector A vector containing one or more positions inside another vector
   * @param field  The field with the fieldsize
   * @return If the given position are inside the bound of the fieldsize
   */
  def checkIfAllPositionsAreInBounds(vector: Vector[Position], field: Field): (Boolean, String) = {
    for (position <- vector) {
      if (position.x < 0 || position.x >= field.fieldSize || position.y < 0 || position.y >= field.fieldSize) {
        return (false, "The given positions are not inside the field")
      }
    }
    (true, "")
  }

  def checkIfAllCellsAreEmpty(field: Field, positions: Vector[Position]): (Boolean, String) = {
    for (elem <- positions) {
      if (field.matrix.cell(elem.x, elem.y).value != 0) {
        return (false, "One destination is not empty to be able to move to this position")
      }
    }
    (true, "")
  }

  def checkIfAllCellsBelongToPlayer(player: Int, field: Field, positions: Vector[Position]): (Boolean, String) = {
    if (player == 1) {
      for (elem <- positions) {
        if (!List(1, 2).contains(field.matrix.cell(elem.x, elem.y).value)) {
          return (false, "Cell does not contain a stone that belongs to Player 1")
        }
      }
    } else {
      for (elem <- positions) {
        if (!List(3, 4).contains(field.matrix.cell(elem.x, elem.y).value)) {
          return (false, "Cell does not contain a stone that belongs to Player 2")
        }
      }
    }
    (true, "")
  }

  def matrixToString: String = field.toString
}
