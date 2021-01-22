package controller.controllerbase

import com.google.inject.{Guice, Inject, Injector}
import controller.ControllerInterface
import model.fileiocomponent.FileIO
import model._
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import util.{Position, UndoManager}


case class Controller @Inject()(var game: Game) extends ControllerInterface {

  private val undoManager = UndoManager(this)
  val injector: Injector = Guice.createInjector(CheckersModule())
  val fileIo: FileIO = injector.instance[FileIO]


  override def createNewField(): Unit = {
    createNewField(game.getField.getFieldSize)
  }

  override def createNewField(size: Int): Unit = {
    game.setField(game.getField.getNewField(size))
    game.setPlayerState(new PlayerState1)
    game.setGameState(GameState.RUNNING)
    game.setStatusMessage("")
    notifyObservers()
  }

  override def changePlayerTurn(): Unit = {
    game.getPlayerState.handle(this)
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
    game.getField.getFieldMatrix.cell(positionFrom.x + x, positionFrom.y + y) match {
      case Some(cell) =>
        if (isStoneOpponentsColor(cell.getValue, stoneToMove)) {
          game.getField.setFieldMatrix(moveToNewPosition(positionFrom, positionTo, game.getField).replaceCell(positionFrom.x + x, positionFrom.y + y, None))
          game.getField.setFieldStatistics(cell.getValue, game.getField.getFieldStatistics(cell.getValue) - 1)

        }
      case None =>
    }
  }

  override def moveFromPositionToPosition(positionFrom: Position, positionTo: Position, stoneToMove: Int, alreadyMoved: Boolean): Unit = {
    val differenceX = positionTo.x - positionFrom.x
    val differenceY = positionTo.y - positionFrom.y

    List(differenceX, differenceY) match {
      //WHITE
      case 1 :: -1 :: Nil => if (stoneToMove == 1 && game.getField.getFieldMatrix.cell(positionTo.x, positionTo.y).isEmpty && !alreadyMoved) game.getField.setFieldMatrix(moveToNewPosition(positionFrom, positionTo, game.getField))
      case 1 :: 1 :: Nil => if (stoneToMove == 1 && game.getField.getFieldMatrix.cell(positionTo.x, positionTo.y).isEmpty && !alreadyMoved) game.getField.setFieldMatrix(moveToNewPosition(positionFrom, positionTo, game.getField))
      //BLACK
      case -1 :: -1 :: Nil => if (stoneToMove == 3 && game.getField.getFieldMatrix.cell(positionTo.x, positionTo.y).isEmpty && !alreadyMoved) game.getField.setFieldMatrix(moveToNewPosition(positionFrom, positionTo, game.getField))
      case -1 :: 1 :: Nil => if (stoneToMove == 3 && game.getField.getFieldMatrix.cell(positionTo.x, positionTo.y).isEmpty && !alreadyMoved) game.getField.setFieldMatrix(moveToNewPosition(positionFrom, positionTo, game.getField))
      //JUMP OVER STONE
      case _ :: _ :: Nil =>
        if (Math.abs(differenceX) == 2 && Math.abs(differenceY) == 2)
          caseJumpOverStone(positionFrom, positionTo, stoneToMove, differenceX / 2, differenceY / 2)
    }
    for (i <- 0 until game.getField.getFieldSize) {
      if (game.getField.getFieldMatrix.getRows(game.getField.getFieldSize - 1)(i).exists(cell => cell.getValue == 1)) {
        game.getField.setFieldMatrix(moveToNewPosition(positionTo, positionTo, game.getField).replaceCell(game.getField.getFieldSize - 1, i, Some(game.getField.getFieldMatrix.getRows(game.getField.getFieldSize - 1)(i).get.createNewKing)))
        game.getField.setFieldStatistics(1, game.getField.getFieldStatistics(1) - 1)
        game.getField.setFieldStatistics(2, game.getField.getFieldStatistics(2) + 1)
      }
      if (game.getField.getFieldMatrix.getRows(0)(i).exists(cell => cell.getValue == 3)) {
        game.getField.setFieldMatrix(moveToNewPosition(positionTo, positionTo, game.getField).replaceCell(0, i, Some(game.getField.getFieldMatrix.getRows(0)(i).get.createNewKing)))
        game.getField.setFieldStatistics(3, game.getField.getFieldStatistics(3) - 1)
        game.getField.setFieldStatistics(4, game.getField.getFieldStatistics(4) + 1)
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
          if (game.getField.getFieldMatrix.getRows(positionFrom.x + posX)(positionFrom.y + posY).exists(cell => cell.getValue != 0)) {
            if (isStoneOpponentsColor(game.getField.getFieldMatrix.getRows(positionFrom.x + posX)(positionFrom.y + posY).get.getValue, stoneToMove)) {
              if (positionFrom.x + posX + posX != positionTo.x) {
                val cell: Cell = game.getField.getFieldMatrix.cell(positionTo.x - directionX, positionTo.y - directionY).get
                game.getField.setFieldStatistics(cell.getValue, game.getField.getFieldStatistics(cell.getValue) - 1)
                game.getField.setFieldMatrix(moveToNewPosition(positionFrom, positionTo, game.getField).replaceCell(positionTo.x - directionX, positionTo.y - directionY, None))
              }
            } else {
              checkGameState()
              return
            }
          }
          posX = posX + directionX
          posY = posY + directionY
          i = i + 1
        }
        moveToNewPosition(positionFrom, positionTo, game.getField)
      }
    }
    checkGameState()
  }

  override def checkGameState(): Unit = {
    Tuple4(game.getField.getFieldStatistics(1), game.getField.getFieldStatistics(2),
      game.getField.getFieldStatistics(3), game.getField.getFieldStatistics(4)) match {
      //Only 1 black / white king on each side left
      case x if (x._1 == 0) && (x._2 == 1) && (x._3 == 0) && (x._4 == 1) => game.setGameState(GameState.DRAW)
      case x if (x._1 >= 0) && (x._2 >= 0) && (x._3 == 0) && (x._4 == 0) => game.setGameState(GameState.P1_WON)
      case x if (x._1 == 0) && (x._2 == 0) && (x._3 >= 0) && (x._4 >= 0) => game.setGameState(GameState.P2_WON)
      case _ => game.setGameState(GameState.RUNNING)
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
        game.setStatusMessage("The given positions are not inside the field")
        return false
      }
    }
    true
  }

  override def checkIfAllCellsAreEmpty(field: Field, positions: Vector[Position]): Boolean = {
    for (elem <- positions) {
      if (field.getFieldMatrix.cell(elem.x, elem.y).exists(cell => cell.getValue != 0)) {
        game.setStatusMessage("One destination is not empty to be able to move to this position")
        return false
      }
    }
    true
  }

  override def checkIfAllCellsBelongToPlayer(field: Field, positions: Vector[Position]): Boolean = {
    if (game.getPlayerState.isInstanceOf[PlayerState1]) {
      for (elem <- positions) {
        if (field.getFieldMatrix.cell(elem.x, elem.y).exists(cell => cell.getColor != "WHITE")) {
          game.setStatusMessage("Cell does not contain a stone that belongs to Player 1")
          return false
        }
      }
    } else {
      for (elem <- positions) {
        if (field.getFieldMatrix.cell(elem.x, elem.y).exists(cell => cell.getColor != "BLACK")) {
          game.setStatusMessage("Cell does not contain a stone that belongs to Player 2")
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

  override def matrixToString: String = game.getField.toString

  def save(): Unit = {
    game.setGameState(GameState.SAVED)
    fileIo.save(game)
    notifyObservers()
  }

  def load(): Unit = {
    game = fileIo.load
    game.setGameState(GameState.LOADED)
    notifyObservers()
  }

  override def getGame: Game = {
    game
  }
}
