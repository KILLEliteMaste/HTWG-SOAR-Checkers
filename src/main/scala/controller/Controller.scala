package controller

import model.{Cell, Field, FieldMatrix, Position}
import util.Observable

case class Controller(var field: Field) extends Observable {
  var player: Int = 1

  def createNewField():Unit = {
    field = Field(field.fieldSize)
  }
  def createNewField(size: Int): Unit = {
    field = Field(size)
  }

  def changePlayerTurn(): String = {
    if (player == 1) player = 2 else player = 1
    notifyObservers()
    "It's player " + player + " turn"
  }

  def moveFromPositionToPosition(positionFrom: Position, positionTo: Position, stoneToMove: Int, alreadyMoved: Boolean): Unit = {
    if (player == 1) {
      //1 == white stone  2 == white King stone
      if (stoneToMove == 1) {
        //WEISS
        //2,3                   Start
        //3,2   1,-1            Bewegen 1 feld LINKS(unten)
        //3,4   1,1             Bewegen 1 feld RECHTS(unten)
        //
        //4,1   2,-2            Über stein drüber LINKS(unten)              BEDINGUNG: Nur wenn schwarzer stein dazwischen   1,-1    3,2 liegt der stein
        //4,5   2,2             Über stein drüber RECHTS(unten)             BEDINGUNG: Nur wenn schwarzer stein dazwischen
        //
        //0,5   -2,2            Über stein drüber RÜCKWÄRTS RECHTS(oben)    BEDINGUNG: Nur wenn schwarzer stein dazwischen
        //0,1   -2,-2           Über stein drüber RÜCKWÄRTS LINKS(oben)     BEDINGUNG: Nur wenn schwarzer stein dazwischen
        List(positionTo.x - positionFrom.x, positionTo.y - positionFrom.y) match {
          case 1 :: -1 :: Nil => if (field.matrix.cell(positionTo.x, positionTo.y).value == 0 && !alreadyMoved) field.matrix = moveToNewPosition(positionFrom, positionTo, field)
          case 1 :: 1 :: Nil => if (field.matrix.cell(positionTo.x, positionTo.y).value == 0 && !alreadyMoved) field.matrix = moveToNewPosition(positionFrom, positionTo, field)
          case -2 :: -2 :: Nil =>
            val value = field.matrix.cell(positionFrom.x - 1, positionFrom.y - 1).value
            if (value == 3 || value == 4) {
              field.matrix = moveToNewPosition(positionFrom, positionTo, field).replaceCell(positionFrom.x - 1, positionFrom.y - 1, Cell(0))
            }
          case 2 :: -2 :: Nil =>
            val value = field.matrix.cell(positionFrom.x + 1, positionFrom.y - 1).value
            if (value == 3 || value == 4) {
              field.matrix = moveToNewPosition(positionFrom, positionTo, field).replaceCell(positionFrom.x + 1, positionFrom.y - 1, Cell(0))
            }
          case -2 :: 2 :: Nil =>
            val value = field.matrix.cell(positionFrom.x - 1, positionFrom.y + 1).value
            if (value == 3 || value == 4) {
              field.matrix = moveToNewPosition(positionFrom, positionTo, field).replaceCell(positionFrom.x - 1, positionFrom.y + 1, Cell(0))
            }
          case 2 :: 2 :: Nil =>
            val value = field.matrix.cell(positionFrom.x + 1, positionFrom.y + 1).value
            if (value == 3 || value == 4) {
              field.matrix = moveToNewPosition(positionFrom, positionTo, field).replaceCell(positionFrom.x + 1, positionFrom.y + 1, Cell(0))
            }
        }
      } else {
      }
    } else {
      if (stoneToMove == 3) {
        //SCHWARZ
        //5,2                  Start
        //4,1   -1,-1          Bewegen 1 feld LINKS(oben)
        //4,3   -1,1           Bewegen 1 feld RECHTS(oben)
        //
        //3,0   -2,-2          Über stein drüber LINKS(oben)                BEDINGUNG: Nur wenn weißer stein dazwischen
        //3,4   -2,2           Über stein drüber RECHTS(oben)               BEDINGUNG: Nur wenn weißer stein dazwischen
        //
        //7,4   2,2            Über stein drüber RÜCKWÄRTS RECHTS(unten)    BEDINGUNG: Nur wenn weißer stein dazwischen
        //7,0   2,-2           Über stein drüber RÜCKWÄRTS LINKS(unten)     BEDINGUNG: Nur wenn weißer stein dazwischen
        List(positionTo.x - positionFrom.x, positionTo.y - positionFrom.y) match {
          case -1 :: -1 :: Nil => if (field.matrix.cell(positionTo.x, positionTo.y).value == 0 && !alreadyMoved) field.matrix = moveToNewPosition(positionFrom, positionTo, field)
          case -1 :: 1 :: Nil => if (field.matrix.cell(positionTo.x, positionTo.y).value == 0 && !alreadyMoved) field.matrix = moveToNewPosition(positionFrom, positionTo, field)
          case -2 :: -2 :: Nil =>
            val value = field.matrix.cell(positionFrom.x - 1, positionFrom.y - 1).value
            if (value == 1 || value == 2) {
              field.matrix = moveToNewPosition(positionFrom, positionTo, field).replaceCell(positionFrom.x - 1, positionFrom.y - 1, Cell(0))
            }
          case 2 :: -2 :: Nil =>
            val value = field.matrix.cell(positionFrom.x + 1, positionFrom.y - 1).value
            if (value == 1 || value == 2) {
              field.matrix = moveToNewPosition(positionFrom, positionTo, field).replaceCell(positionFrom.x + 1, positionFrom.y - 1, Cell(0))
            }
          case -2 :: 2 :: Nil =>
            val value = field.matrix.cell(positionFrom.x - 1, positionFrom.y + 1).value
            if (value == 1 || value == 2) {
              field.matrix = moveToNewPosition(positionFrom, positionTo, field).replaceCell(positionFrom.x - 1, positionFrom.y + 1, Cell(0))
            }
          case 2 :: 2 :: Nil =>
            val value = field.matrix.cell(positionFrom.x + 1, positionFrom.y + 1).value
            if (value == 1 || value == 2) {
              field.matrix = moveToNewPosition(positionFrom, positionTo, field).replaceCell(positionFrom.x + 1, positionFrom.y + 1, Cell(0))
            }
        }
      } else {

      }
    }
  }

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
  def checkIfAllPositionsAreInBounds(vector: Vector[Position], field: Field): Boolean = {
    for (position <- vector) {
      if (position.x < 0 || position.x >= field.fieldSize || position.y < 0 || position.y >= field.fieldSize) {
        println("The given positions are not inside the field")
        return false
      }
    }
    true
  }

  def checkIfAllCellsAreEmpty(field: Field, positions: Vector[Position]): Boolean = {
    for (elem <- positions) {
      if (field.matrix.cell(elem.x, elem.y).value != 0) {
        println("One destination is not empty to be able to move to this position")
        return false
      }
    }
    true
  }

  def checkIfAllCellsBelongToPlayer(player: Int, field: Field, positions: Vector[Position]): Boolean = {
    if (player == 1) {
      for (elem <- positions) {
        if (!List(1, 2).contains(field.matrix.cell(elem.x, elem.y).value)) {
          println("Cell does not contain a stone that belongs to Player 1")
          return false
        }
      }
    } else {
      for (elem <- positions) {
        if (!List(3, 4).contains(field.matrix.cell(elem.x, elem.y).value)) {
          println("Cell does not contain a stone that belongs to Player 2")
          return false
        }
      }
    }
    true
  }

  def matrixToString: String = field.toString
}
