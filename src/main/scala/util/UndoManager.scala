package util

import model.GameState.GameState
import controller.controllerbase.Controller
import model.{Cell, Field, FieldMatrix, PlayerState}

import scala.collection.mutable

case class UndoManager(controller: Controller) {
  private val undoStack = mutable.Stack[OldController]()
  private val redoStack = mutable.Stack[OldController]()

  def doStep(): Unit = {
    undoStack.push(OldController(controller.game.getField.copyField, controller.game.getField.getFieldMatrix.copyFieldMatrix, controller.game.getPlayerState, controller.game.getGameState, controller.game.getStatusMessage))
  }

  def undoStep(): String = {
    if (undoStack.isEmpty)
      return "Cannot undo"
    redoStack.push(OldController(controller.game.getField.copyField, controller.game.getField.getFieldMatrix.copyFieldMatrix, controller.game.getPlayerState, controller.game.getGameState, controller.game.getStatusMessage))
    setControllerToOldState(undoStack.pop())
    "Undo to old state"
  }

  def redoStep(): String = {
    if (redoStack.isEmpty)
      return "Cannot redo"
    undoStack.push(OldController(controller.game.getField.copyField, controller.game.getField.getFieldMatrix.copyFieldMatrix, controller.game.getPlayerState, controller.game.getGameState, controller.game.getStatusMessage))
    val f2 = redoStack.pop()
    setControllerToOldState(f2)
    "Redo to old state"
  }

  def setControllerToOldState(oldController: OldController): Unit = {
    controller.game.setField(oldController.field)
    controller.game.getField.setFieldMatrix(oldController.fieldMatrix)
    controller.game.setPlayerState(oldController.playerState)
    controller.game.setGameState(oldController.gameState)
    controller.game.setStatusMessage(oldController.statusMessage)
  }

  case class OldController(field: Field, fieldMatrix: FieldMatrix[Option[Cell]], playerState: PlayerState, gameState: GameState, statusMessage: String)

}