package controller.controllerbase

import controller.{ControllerInterface, GameState, PlayerState, PlayerState1}
import model.{Cell, Field, FieldMatrix}
import util.{Observable, Position, UndoManager}

case class Controller(var field: Field) extends Observable with ControllerInterface {
//  def this() = this(FieldImpl(8))

  var gameState: GameState.Value = GameState.IDLE
  var statusMessage: String = ""
  var playerState: PlayerState = new PlayerState1
  private val undoManager = UndoManager(this)

  override def createNewField(): Unit = {
    createNewField(field.getFieldSize)
  }

  override def createNewField(size: Int): Unit = {
    field = field.getNewField(size)
    playerState = new PlayerState1
    gameState = GameState.RUNNING
    statusMessage = ""
    notifyObservers()
  }

  override def changePlayerTurn(): Unit = {
    playerState.handle(this)
    notifyObservers()
  }

  override def isStoneOpponentsColor(stoneToJumpOver: Int, stoneToMove: Int): Boolean = {
    if (stoneToMove == 1 || stoneToMove == 2) {
      stoneToJumpOver == 3 || stoneToJumpOver == 4
    } else {
      stoneToJumpOver == 1 || stoneToJumpOver == 2
    }
  }

  override def caseJumpOverStone(positionFrom: Position, positionTo: Position, stoneToMove: Int, x: Int, y: Int): Unit = {
    field.getFieldMatrix.cell(positionFrom.x + x, positionFrom.y + y) match {
      case Some(cell) =>
        if (isStoneOpponentsColor(cell.getValue, stoneToMove)) {
          field.setFieldMatrix(moveToNewPosition(positionFrom, positionTo, field).replaceCell(positionFrom.x + x, positionFrom.y + y, None))
        }
      case None =>
    }
  }

  override def moveFromPositionToPosition(positionFrom: Position, positionTo: Position, stoneToMove: Int, alreadyMoved: Boolean): Unit = {
    val differenceX = positionTo.x - positionFrom.x
    val differenceY = positionTo.y - positionFrom.y

    List(differenceX, differenceY) match {
      //WHITE
      case 1 :: -1 :: Nil => if (stoneToMove == 1 && field.getFieldMatrix.cell(positionTo.x, positionTo.y).isEmpty && !alreadyMoved) field.setFieldMatrix(moveToNewPosition(positionFrom, positionTo, field))
      case 1 :: 1 :: Nil => if (stoneToMove == 1 && field.getFieldMatrix.cell(positionTo.x, positionTo.y).isEmpty && !alreadyMoved) field.setFieldMatrix(moveToNewPosition(positionFrom, positionTo, field))
      //BLACK
      case -1 :: -1 :: Nil => if (stoneToMove == 3 && field.getFieldMatrix.cell(positionTo.x, positionTo.y).isEmpty && !alreadyMoved) field.setFieldMatrix(moveToNewPosition(positionFrom, positionTo, field))
      case -1 :: 1 :: Nil => if (stoneToMove == 3 && field.getFieldMatrix.cell(positionTo.x, positionTo.y).isEmpty && !alreadyMoved) field.setFieldMatrix(moveToNewPosition(positionFrom, positionTo, field))
      //JUMP OVER STONE
      case _ :: _ :: Nil =>
        if (Math.abs(differenceX) == 2 && Math.abs(differenceY) == 2)
          caseJumpOverStone(positionFrom, positionTo, stoneToMove, differenceX / 2, differenceY / 2)
    }
    for (i <- 0 until field.getFieldSize) {
      if (field.getFieldMatrix.getRows(field.getFieldSize - 1)(i).exists(cell => cell.getValue == 1)) {
        field.setFieldMatrix(moveToNewPosition(positionTo, positionTo, field).replaceCell(field.getFieldSize - 1, i, Some(field.getFieldMatrix.getRows(field.getFieldSize - 1)(i).get.createNewKing)))
      }
      if (field.getFieldMatrix.getRows(0)(i).exists(cell => cell.getValue == 3)) {
        field.setFieldMatrix(moveToNewPosition(positionTo, positionTo, field).replaceCell(0, i, Some(field.getFieldMatrix.getRows(0)(i).get.createNewKing)))
      }
    }
    if ((stoneToMove == 2 || stoneToMove == 4) && !alreadyMoved) {
      if (Math.abs(differenceX).equals(Math.abs(differenceY))) {
        val directionX = if (differenceX < 0) -1 else 1
        val directionY = if (differenceY < 0) -1 else 1
        var posX = directionX
        var posY = directionY
        var i = 0
        while (i < Math.abs(differenceX)) {

          if (field.getFieldMatrix.getRows(positionFrom.x + posX)(positionFrom.y + posY).exists(cell => cell.getValue != 0)) {
            if (isStoneOpponentsColor(field.getFieldMatrix.getRows(positionFrom.x + posX)(positionFrom.y + posY).get.getValue, stoneToMove)) {
              if (positionFrom.x + posX + posX != positionTo.x) {
                field.setFieldMatrix(moveToNewPosition(positionFrom, positionTo, field).replaceCell(positionTo.x - directionX, positionTo.y - directionY, None))
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
      }
    }
    checkGameState()
  }

  override def checkGameState(): Unit = {
    Tuple4(field.getFieldStatistics(1), field.getFieldStatistics(2),
      field.getFieldStatistics(3), field.getFieldStatistics(4)) match {
      //Only 1 black / white king on each side left
      case x if (x._1 == 0) && (x._2 == 1) && (x._3 == 0) && (x._4 == 1) => gameState = GameState.DRAW
      case x if (x._1 >= 0) && (x._2 >= 0) && (x._3 == 0) && (x._4 == 0) => gameState = GameState.P1_WON
      case x if (x._1 == 0) && (x._2 == 0) && (x._3 >= 0) && (x._4 >= 0) => gameState = GameState.P2_WON
      case _ =>
    }
  }

  override def moveToNewPosition(origin: Position, destination: Position, field: Field): FieldMatrix[Option[Cell]] = {
    val cellOrigin = field.getFieldMatrix.cell(origin.x, origin.y)
    val cellDestination = field.getFieldMatrix.cell(destination.x, destination.y)

    field.setFieldMatrix(field.getFieldMatrix.replaceCell(destination.x, destination.y, cellOrigin))
    field.setFieldMatrix(field.getFieldMatrix.replaceCell(origin.x, origin.y, cellDestination))
    field.getFieldMatrix
  }

  override def checkIfAllPositionsAreInBounds(vector: Vector[Position], field: Field): Boolean = {
    for (position <- vector) {
      if (position.x < 0 || position.x >= field.getFieldSize || position.y < 0 || position.y >= field.getFieldSize) {
        statusMessage = "The given positions are not inside the field"
        return false
      }
    }
    true
  }

  override def checkIfAllCellsAreEmpty(field: Field, positions: Vector[Position]): Boolean = {
    for (elem <- positions) {
      if (field.getFieldMatrix.cell(elem.x, elem.y).exists(cell => cell.getValue != 0)) {
        statusMessage = "One destination is not empty to be able to move to this position"
        return false
      }
    }
    true
  }

  override def checkIfAllCellsBelongToPlayer(field: Field, positions: Vector[Position]): Boolean = {
    if (playerState.isInstanceOf[PlayerState1]) {
      for (elem <- positions) {
        if (field.getFieldMatrix.cell(elem.x, elem.y).exists(cell => cell.getColor != "WHITE")) {
          statusMessage = "Cell does not contain a stone that belongs to Player 1"
          return false
        }
      }
    } else {
      for (elem <- positions) {
        if (field.getFieldMatrix.cell(elem.x, elem.y).exists(cell => cell.getColor != "BLACK")) {
          statusMessage = "Cell does not contain a stone that belongs to Player 2"
          return false
        }
      }
    }
    true
  }

  override def doStep(): Unit = {
    undoManager.doStep()
  }

  override def undo(): String = {
    val ret = undoManager.undoStep()
    notifyObservers()
    ret
  }

  override def redo(): String = {
    val ret = undoManager.redoStep()
    notifyObservers()
    ret
  }

  override def matrixToString: String = field.toString
}
