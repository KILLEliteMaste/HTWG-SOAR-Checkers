package de.htwg.se.checkers.controller.controllerbase

import com.google.inject.{Guice, Inject, Injector}
import com.sun.media.jfxmedia.events.PlayerStateEvent.PlayerState
import de.htwg.se.board.{Cell, Field, FieldMatrix, Game, PlayerState1}
import de.htwg.se.board.*
import de.htwg.se.checkers.controller.ControllerInterface
import de.htwg.se.checkers.util.{Position, UndoManager}
import de.htwg.se.checkers.CheckersModule
import de.htwg.se.fileio.FileIO
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import de.htwg.se.fileio.dbComponent.DaoInterface

import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.collection.{immutable, mutable}
import scala.util.{Failure, Success}
import concurrent.ExecutionContext.Implicits.global
case class Controller @Inject()(var game: Game) extends ControllerInterface {

  private val undoManager = UndoManager(this)
  val injector: Injector = Guice.createInjector(CheckersModule())
  val fileIo: FileIO = injector.getInstance(classOf[FileIO])
  val database: DaoInterface = injector.getInstance(classOf[DaoInterface])
  
  val fileIoPort: Int = sys.env.getOrElse("FILE_IO_PORT", 8081).toString.toInt
  val fileIoHost: String = sys.env.getOrElse("FILE_IO_HOST", "localhost")
  
  game = game.recreate(field = createNewField())

  override def createNewField(): Field = createNewField(game.field.fieldSize)

  override def createNewField(size: 8 | 10 | 12): Field = {
    game = game.recreate(size, new PlayerState1, game.statusMessage, game.field.createNewField(size), GameState.RUNNING)
    notifyObservers()
    game = game.recreate(field = game.field.recreate(fieldStatistics = game.field.fieldStatistics + (1 -> countStones(1), 3 -> countStones(3))))
    game.field
  }

  def countStones(searchValue: Int): Int = game.field.fieldMatrix.rows.flatten.map(_.map(_.value).getOrElse(0)).count(_ == searchValue)

  override def changePlayerTurn(): Unit = {
    game = game.recreate(playerState = game.playerState.handle())
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
      case -1 :: 1 :: Nil => if (stoneToMove == 3 && game.field.fieldMatrix.cell(positionTo.x, positionTo.y).isEmpty && !alreadyMoved) game = game.recreate(field = game.field.recreate(fieldMatrix = moveToNewPosition(positionFrom, positionTo, game.field)))
      //JUMP OVER STONE
      case _ :: _ :: Nil =>
        if (Math.abs(differenceX) == 2 && Math.abs(differenceY) == 2)
          caseJumpOverStone(positionFrom, positionTo, stoneToMove, differenceX / 2, differenceY / 2)
    }
    for (i <- 0 until game.field.fieldSize) {
      if (game.field.fieldMatrix.rows(game.field.fieldSize - 1)(i).exists(cell => cell.value == 1)) {
        game = game.recreate(field = game.field.recreate(fieldMatrix = moveToNewPosition(positionTo, positionTo, game.field).replaceCell(game.field.fieldSize - 1, i, Some(game.field.fieldMatrix.rows(game.field.fieldSize - 1)(i).get.createNewKing))))
        game = game.recreate(field = game.field.decreaseFieldStatistics(1))
        game = game.recreate(field = game.field.increaseFieldStatistics(2))
      }
      if (game.field.fieldMatrix.rows(0)(i).exists(cell => cell.value == 3)) {
        game = game.recreate(field = game.field.recreate(fieldMatrix = moveToNewPosition(positionTo, positionTo, game.field).replaceCell(0, i, Some(game.field.fieldMatrix.rows(0)(i).get.createNewKing))))
        game = game.recreate(field = game.field.decreaseFieldStatistics(3))
        game = game.recreate(field = game.field.increaseFieldStatistics(4))
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
        game = game.recreate(field = game.field.recreate(fieldMatrix = moveToNewPosition(positionFrom, positionTo, game.field)))
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

  override def getGame: Game = game

  override def setGame(newGame: Game): Unit = game = newGame

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

  override def doStep(): Unit = undoManager.doStep()

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

  override def save(): Unit = {
    implicit val system = ActorSystem(Behaviors.empty, "SingleRequest")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.executionContext

    val responseFuture: Future[HttpResponse] = Http().singleRequest(
      HttpRequest(
        method= HttpMethods.POST,
        uri= s"http://$fileIoHost:$fileIoPort/json",
        entity = HttpEntity(ContentTypes.`application/json`, fileIo.serialize(game))
      )
    )
    game = game.recreate(gameState = GameState.SAVED)
    notifyObservers()
  }

  override def load(): String = {
    implicit val system = ActorSystem(Behaviors.empty, "SingleRequest")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.executionContext

    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = s"http://$fileIoHost:$fileIoPort/json"))

    var returnMessage = ""
    val waitFuture = responseFuture andThen  {
      case Failure(exception) =>
        game = game.recreate(gameState = GameState.FILE_COULD_NOT_BE_LOADED)
        notifyObservers()
        returnMessage = "ERROR: COULD NOT LOAD SAVE FILE"
      case Success(value) =>
        Unmarshal(value.entity).to[String] andThen  {
          case Failure(_) => sys.error("Something went wrong trying to unmarshal the FileIO response")
          case Success(result) =>
            returnMessage = fileIo.loadByString(result) match {
              case Success(gameSuccess) =>
                game = gameSuccess.recreate(gameState = GameState.LOADED)
                notifyObservers()
                "LOADED"
              case Failure(e) =>
                game = game.recreate(gameState = GameState.FILE_COULD_NOT_BE_LOADED)
                notifyObservers()
                "ERROR: COULD NOT LOAD SAVE FILE"
            }
        }
    }
    Await.ready(waitFuture, Duration.Inf)
    returnMessage
  }

  override def loadFromDB(): String = {
    database.load().onComplete {
      case Success(tryGame) => {
        tryGame match {
          case Success(result) =>
            game = result
            notifyObservers()
            "LOADED FROM DATABASE"
          case Failure(_) => {
            notifyObservers()
            "COULD NOT LOAD FROM DATABASE"
          }
        }
      }
      case Failure(t) => println("An error has occurred: " + t.getMessage)
    }
    "LOADED FROM DB"
  }

  override def saveToDB(): Unit = {
    database.save(game = game)
  }
}
