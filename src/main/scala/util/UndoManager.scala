package util

import model.GameState.GameState
import controller.controllerbase.Controller
import model.{Cell, Field, FieldMatrix, PlayerState}

import scala.collection.mutable

case class UndoManager(controller: Controller) {
  private val undoStack = mutable.Stack[OldController]()
  private val redoStack = mutable.Stack[OldController]()

  def doStep(): Unit = {
    controller.getGame.getField.getFieldStatistics(1)
    undoStack.push(OldController(controller.game.getField.copyField, controller.getGame.getField.getFieldStatistics(1),
      controller.getGame.getField.getFieldStatistics(2),
      controller.getGame.getField.getFieldStatistics(3),
      controller.getGame.getField.getFieldStatistics(4),
      controller.game.getField.getFieldMatrix.copyFieldMatrix,
      controller.game.getPlayerState, controller.game.getGameState, controller.game.getStatusMessage))
  }

  def undoStep(): String = {
    if (undoStack.isEmpty)
      return "Cannot undo"
    redoStack.push(OldController(controller.game.getField.copyField, controller.getGame.getField.getFieldStatistics(1),
      controller.getGame.getField.getFieldStatistics(2),
      controller.getGame.getField.getFieldStatistics(3),
      controller.getGame.getField.getFieldStatistics(4), controller.game.getField.getFieldMatrix.copyFieldMatrix, controller.game.getPlayerState, controller.game.getGameState, controller.game.getStatusMessage))
    setControllerToOldState(undoStack.pop())
    "Undo to old state"
  }

  def redoStep(): String = {
    if (redoStack.isEmpty)
      return "Cannot redo"
    undoStack.push(OldController(controller.game.getField.copyField, controller.getGame.getField.getFieldStatistics(1),
      controller.getGame.getField.getFieldStatistics(2),
      controller.getGame.getField.getFieldStatistics(3),
      controller.getGame.getField.getFieldStatistics(4), controller.game.getField.getFieldMatrix.copyFieldMatrix, controller.game.getPlayerState, controller.game.getGameState, controller.game.getStatusMessage))
    val f2 = redoStack.pop()
    setControllerToOldState(f2)
    "Redo to old state"
  }

  def setControllerToOldState(oldController: OldController): Unit = {
    controller.game.setField(oldController.field)
    controller.game.getField.setFieldStatistics(1, oldController.s1)
    controller.game.getField.setFieldStatistics(2, oldController.s2)
    controller.game.getField.setFieldStatistics(3, oldController.s3)
    controller.game.getField.setFieldStatistics(4, oldController.s4)
    controller.game.getField.setFieldMatrix(oldController.fieldMatrix)
    controller.game.setPlayerState(oldController.playerState)
    controller.game.setGameState(oldController.gameState)
    controller.game.setStatusMessage(oldController.statusMessage)
  }

  case class OldController(field: Field, s1: Int, s2: Int, s3: Int, s4: Int, fieldMatrix: FieldMatrix[Option[Cell]], playerState: PlayerState, gameState: GameState, statusMessage: String)

}