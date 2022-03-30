package controller.controllerbase

import com.google.inject.{Guice, Inject, Injector}
import controller.ControllerInterface
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import util.{Position, UndoManager}
import model.{Cell, CheckersModule, Field, FieldMatrix, Game, GameState, PlayerState1}
import model.fileiocomponent.FileIO

import scala.collection.{immutable, mutable}

case class Controller @Inject()(var game: Game) extends ControllerInterface {

  private val undoManager = UndoManager(this)
  val injector: Injector = Guice.createInjector(CheckersModule())
  val fileIo: FileIO = injector.getInstance(classOf[FileIO])

  game = game.recreate(field = createNewField())//game.recreate(field = game.field.recreate(fieldStatistics = game.field.fieldStatistics + (1-> countStones(1), 3-> countStones(3))))

  override def createNewField(): Field = {
    createNewField(game.field.fieldSize)
  }

  override def createNewField(size: 8 | 10 | 12): Field = {
    game = game.recreate(size, new PlayerState1, game.statusMessage, game.field.createNewField(size), GameState.RUNNING)
    notifyObservers()
    game = game.recreate(field = game.field.recreate(fieldStatistics = game.field.fieldStatistics + (1-> countStones(1), 3-> countStones(3))))
    game.field
  }

  def countStones(searchValue: Int): Int = {
    var counter = 0
    game.field.fieldMatrix.rows.foreach(vector => {
      counter = counter + vector.filter(_.isDefined).map(_.get).count(_.value == searchValue)
    })
    counter
  }

  override def changePlayerTurn(): Unit = {
    game.playerState.handle(this)
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
    game.field.fieldMatrix.cell(positionFrom.x + x, positionFrom.y + y) match {
      case Some(cell) =>
        if (isStoneOpponentsColor(cell.value, stoneToMove)) {
          game = game.recreate(field = game.field.recreate(fieldMatrix = moveToNewPosition(positionFrom, positionTo, game.field).replaceCell(positionFrom.x + x, positionFrom.y + y, None)))
          game = game.recreate(field = game.field.decreaseFieldStatistics(cell.value))
        }
      case None =>
    }
  }

  override def moveFromPositionToPosition(positionFrom: Position, positionTo: Position, stoneToMove: Int, alreadyMoved: Boolean): Unit = {
    val differenceX = positionTo.x - positionFrom.x
    val differenceY = positionTo.y - positionFrom.y

    List(differenceX, differenceY) match {
      //WHITE
      case 1 :: -1 :: Nil => if (stoneToMove == 1 && game.field.fieldMatrix.cell(positionTo.x, positionTo.y).isEmpty && !alreadyMoved) game = game.recreate(field = game.field.recreate(fieldMatrix = moveToNewPosition(positionFrom, positionTo, game.field)))
      case 1 :: 1 :: Nil => if (stoneToMove == 1 && game.field.fieldMatrix.cell(positionTo.x, positionTo.y).isEmpty && !alreadyMoved) game = game.recreate(field = game.field.recreate(fieldMatrix = moveToNewPosition(positionFrom, positionTo, game.field)))
      //BLACK
      case -1 :: -1 :: Nil => if (stoneToMove == 3 && game.field.fieldMatrix.cell(positionTo.x, positionTo.y).isEmpty && !alreadyMoved) game = game.recreate(field = game.field.recreate(fieldMatrix = moveToNewPosition(positionFrom, positionTo, game.field)))
      case -1 :: 1 :: Nil => if (stoneToMove == 3 && game.field.fieldMatrix.cell(positionTo.x, positionTo.y).isEmpty && !alreadyMoved) game = game.recreate(field = game.field.recreate(fieldMatrix= moveToNewPosition(positionFrom, positionTo, game.field)))
      //JUMP OVER STONE
      case _ :: _ :: Nil =>
        if (Math.abs(differenceX) == 2 && Math.abs(differenceY) == 2)
          caseJumpOverStone(positionFrom, positionTo, stoneToMove, differenceX / 2, differenceY / 2)
    }
    for (i <- 0 until game.field.fieldSize) {
      if (game.field.fieldMatrix.rows(game.field.fieldSize - 1)(i).exists(cell => cell.value == 1)) {
        game = game.recreate(field = game.field.recreate(fieldMatrix = moveToNewPosition(positionTo, positionTo, game.field).replaceCell(game.field.fieldSize - 1, i, Some(game.field.fieldMatrix.rows(game.field.fieldSize - 1)(i).get.createNewKing))))
        game =game.recreate(field = game.field.decreaseFieldStatistics(1))
        game =game.recreate(field = game.field.increaseFieldStatistics(2))
      }
      if (game.field.fieldMatrix.rows(0)(i).exists(cell => cell.value == 3)) {
        game = game.recreate(field = game.field.recreate(fieldMatrix = moveToNewPosition(positionTo, positionTo, game.field).replaceCell(0, i, Some(game.field.fieldMatrix.rows(0)(i).get.createNewKing))))
        game =game.recreate(field = game.field.decreaseFieldStatistics(3))
        game =game.recreate(field = game.field.increaseFieldStatistics(4))
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
          if (game.field.fieldMatrix.rows(positionFrom.x + posX)(positionFrom.y + posY).exists(cell => cell.value != 0)) {
            if (isStoneOpponentsColor(game.field.fieldMatrix.rows(positionFrom.x + posX)(positionFrom.y + posY).get.value, stoneToMove)) {
              if (positionFrom.x + posX + posX != positionTo.x) {
                val cell: Cell = game.field.fieldMatrix.cell(positionTo.x - directionX, positionTo.y - directionY).get
                //game.field.fieldStatistics(cell.getValue, game.field.getFieldStatistics(cell.getValue) - 1)
                game = game.recreate(field = game.field.decreaseFieldStatistics(cell.value))
                game = game.recreate(field = game.field.recreate(fieldMatrix = moveToNewPosition(positionFrom, positionTo, game.field).replaceCell(positionTo.x - directionX, positionTo.y - directionY, None)))
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
        moveToNewPosition(positionFrom, positionTo, game.field)
      }
    }
    checkGameState()
  }

  override def checkGameState(): Unit = {
    Tuple4(game.field.fieldStatistics.get(1).get, game.field.fieldStatistics.get(2).get,
      game.field.fieldStatistics.get(3).get, game.field.fieldStatistics.get(4).get) match {
      //Only 1 black / white king on each side left
      case x if (x._1 == 0) && (x._2 == 1) && (x._3 == 0) && (x._4 == 1) => game = game.recreate(gameState = GameState.DRAW)
      case x if (x._1 >= 0) && (x._2 >= 0) && (x._3 == 0) && (x._4 == 0) => game = game.recreate(gameState = GameState.P1_WON)
      case x if (x._1 == 0) && (x._2 == 0) && (x._3 >= 0) && (x._4 >= 0) => game = game.recreate(gameState = GameState.P2_WON)
      case _ => game = game.recreate(gameState = GameState.RUNNING)
    }
  }

  override def moveToNewPosition(origin: Position, destination: Position, field: Field): FieldMatrix[Option[Cell]] = {
    val cellOrigin = field.fieldMatrix.cell(origin.x, origin.y)
    val cellDestination = field.fieldMatrix.cell(destination.x, destination.y)

    var newField = field.recreate(fieldMatrix = field.fieldMatrix.replaceCell(destination.x, destination.y, cellOrigin))
    newField = newField.recreate(fieldMatrix = newField.fieldMatrix.replaceCell(origin.x, origin.y, cellDestination))
    newField.fieldMatrix
  }

  override def checkIfAllPositionsAreInBounds(vector: Vector[Position], field: Field): Boolean = {
    for (position <- vector) {
      if (position.x < 0 || position.x >= field.fieldSize || position.y < 0 || position.y >= field.fieldSize) {
        game = game.recreate(statusMessage = "The given positions are not inside the field")
        return false
      }
    }
    true
  }

  override def checkIfAllCellsAreEmpty(field: Field, positions: Vector[Position]): Boolean = {
    for (elem <- positions) {
      if (field.fieldMatrix.cell(elem.x, elem.y).exists(cell => cell.value != 0)) {
        game = game.recreate(statusMessage = "One destination is not empty to be able to move to this position")
        return false
      }
    }
    true
  }

  override def checkIfAllCellsBelongToPlayer(field: Field, positions: Vector[Position]): Boolean = {
    if (game.playerState.isInstanceOf[PlayerState1]) {
      for (elem <- positions) {
        if (field.fieldMatrix.cell(elem.x, elem.y).exists(cell => cell.getColor != "WHITE")) {
          game = game.recreate(statusMessage = "Cell does not contain a stone that belongs to Player 1")
          return false
        }
      }
    } else {
      for (elem <- positions) {
        if (field.fieldMatrix.cell(elem.x, elem.y).exists(cell => cell.getColor != "BLACK")) {
          game = game.recreate(statusMessage = "Cell does not contain a stone that belongs to Player 2")
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

  override def matrixToString: String = game.field.toString

  override def setGame(newGame :Game): Unit = game = newGame

  def save(): Unit = {
    game = game.recreate(gameState = GameState.SAVED)
    fileIo.save(game)
    notifyObservers()
  }

  def load(): Unit = {
    game = fileIo.load
    game = game.recreate(gameState = GameState.LOADED)
    notifyObservers()
  }

  override def getGame: Game = {
    game
  }
}
