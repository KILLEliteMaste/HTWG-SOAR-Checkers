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
        field.matrix = moveToNewPosition(positionTo, positionTo, field).replaceCell(field.fieldSize - 1, i, Cell(4))
      }
    }
    if (stoneToMove == 2 || stoneToMove == 4) {
      if (Math.abs(differenceX) == Math.abs(differenceY)) {
        //Nur laufen
        //Laufen mit eigener stein
        //Laufen mit gegnerischer stein (jump)
        if (!alreadyMoved){



        }

        caseJumpOverStone(positionFrom, positionTo, stoneToMove, differenceX / 2, differenceY / 2)
      } else {
        println("CANNOT EXECUTE")
      }
    }
  }

  def moveFromPositionToPositionAAAA(positionFrom: Position, positionTo: Position, stoneToMove: Int, alreadyMoved: Boolean): Unit = {
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
        for (i <- 0 to 7) {
          if (field.matrix.rows(7)(i).value == 1) {
            field.matrix = moveToNewPosition(positionTo, positionTo, field).replaceCell(7, i, Cell(2))
          }
        }
      } else {
        List(positionTo.x - positionFrom.x, positionTo.y - positionFrom.y) match {
          /* wenn in x- und y- Richtung gleich weit gezogen wird und dabei nicht das Spielfeld verlassen wird, prüfe ob
          *  zwischen Start- und Endpunkt andere Figuren stehen. Falls nein update Position.
          * */
          case _ :: _ :: Nil =>
            updated = false
            if ((positionTo.x - positionFrom.x) % (positionTo.y - positionFrom.y) == 0 && positionTo.x < 9 &&
              positionTo.y < 9 && positionTo.x >= 0 && positionTo.y >= 0) {
              for (i <- positionFrom.x to (positionTo.x - positionFrom.x)) {
                if (field.matrix.cell(positionFrom.x + i, positionFrom.y + i).value != 0) {
                  //falls sich ein Stein im Laufweg befindet, wird dieser vom Spielfeld entfernt und der King wird diagonal hinter dem Token platziert
                  if (field.matrix.cell(positionFrom.x + i, positionFrom.y + i).value == 3 | field.matrix.cell(positionFrom.x + i, positionFrom.y + i).value == 4) {
                    field.matrix = moveToNewPosition(positionFrom, Position(positionFrom.x + i + getDirectionx(positionFrom, positionTo),
                      positionFrom.y + i + getDirectiony(positionTo, positionTo))
                      , field).replaceCell(positionFrom.x + i, positionFrom.y + i, Cell(0))
                    updated = true
                  }
                }
              }
            }
            if (!updated) {
              field.matrix = moveToNewPosition(positionFrom, positionTo, field)
            }
        }
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
        for (i <- 0 to 7) {
          if (field.matrix.rows(0)(i).value == 3) {
            field.matrix = moveToNewPosition(positionTo, positionTo, field).replaceCell(0, i, Cell(4))
          }
        }
      } else {

      }
    }
  }

  def getDirectionx(origin: Position, destination: Position): Int = {
    if (destination.x - origin.x > 0) {
      ret = 1
    }
    else if (destination.x - origin.x == 0) {
      ret = 0
    }
    else {
      ret = -1
    }
    ret
  }

  def getDirectiony(origin: Position, destination: Position): Int = {
    if (destination.y - origin.y > 0) {
      ret = 1
    }
    else if (destination.y - origin.y == 0) {
      ret = 0
    }
    else {
      ret = -1
    }
    ret
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
