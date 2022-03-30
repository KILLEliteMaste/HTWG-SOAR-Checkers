package util

import controller.controllerbase.Controller
import model.{Cell, Field, FieldMatrix, GameState, PlayerState}

import scala.collection.mutable

case class UndoManager(controller: Controller) {
  private val undoStack = mutable.Stack[OldController]()
  private val redoStack = mutable.Stack[OldController]()

  def doStep(): Unit = {
    controller.game.field.fieldStatistics.get(1).get
    undoStack.push(OldController(controller.game.field.copyField, controller.game.field.fieldStatistics.get(1).get,
      controller.game.field.fieldStatistics.get(2).get,
      controller.game.field.fieldStatistics.get(3).get,
      controller.game.field.fieldStatistics.get(4).get,
      controller.game.field.fieldMatrix.copyFieldMatrix,
      controller.game.playerState, controller.game.gameState, controller.game.statusMessage))
  }

  def undoStep(): String = {
    if (undoStack.isEmpty)
      return "Cannot undo"
    redoStack.push(OldController(controller.game.field.copyField, controller.game.field.fieldStatistics.get(1).get,
      controller.game.field.fieldStatistics.get(2).get,
      controller.game.field.fieldStatistics.get(3).get,
      controller.game.field.fieldStatistics.get(4).get, controller.game.field.fieldMatrix.copyFieldMatrix, controller.game.playerState, controller.game.gameState, controller.game.statusMessage))
    setControllerToOldState(undoStack.pop())
    "Undo to old state"
  }

  def redoStep(): String = {
    if (redoStack.isEmpty)
      return "Cannot redo"
    undoStack.push(OldController(controller.game.field.copyField, controller.game.field.fieldStatistics.get(1).get,
      controller.game.field.fieldStatistics.get(2).get,
      controller.game.field.fieldStatistics.get(3).get,
      controller.game.field.fieldStatistics.get(4).get, controller.game.field.fieldMatrix.copyFieldMatrix, controller.game.playerState, controller.game.gameState, controller.game.statusMessage))
    val f2 = redoStack.pop()
    setControllerToOldState(f2)
    "Redo to old state"
  }

  def setControllerToOldState(oldController: OldController): Unit = {
    controller.setGame(controller.game.recreate(field = oldController.field, playerState = oldController.playerState, gameState = oldController.gameState, statusMessage = oldController.statusMessage))

    controller.setGame(controller.game.recreate(field = controller.game.field.recreate(fieldMatrix = oldController.fieldMatrix, fieldStatistics = controller.game.field.fieldStatistics  + (1-> oldController.s1, 2-> oldController.s2, 3-> oldController.s3, 4-> oldController.s4))))
  }

  case class OldController(field: Field, s1: Int, s2: Int, s3: Int, s4: Int, fieldMatrix: FieldMatrix[Option[Cell]], playerState: PlayerState, gameState: GameState, statusMessage: String)

}