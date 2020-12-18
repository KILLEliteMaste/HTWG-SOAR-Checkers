package util

import controller.GameState.GameState
import controller.PlayerState
import controller.controllerbase.Controller
import model.{Cell, Field, FieldMatrix}

import scala.collection.mutable

case class UndoManager(controller: Controller) {
  private val undoStack = mutable.Stack[OldController]()
  private val redoStack = mutable.Stack[OldController]()

  def doStep(): Unit = {
    undoStack.push(OldController(controller.field.copyField, controller.field.getFieldMatrix.copyFieldMatrix, controller.playerState, controller.gameState, controller.statusMessage))
  }

  def undoStep(): String = {
    if (undoStack.isEmpty)
      return "Cannot undo"
    redoStack.push(OldController(controller.field.copyField, controller.field.getFieldMatrix.copyFieldMatrix, controller.playerState, controller.gameState, controller.statusMessage))
    setControllerToOldState(undoStack.pop())
    "Undo to old state"
  }

  def redoStep(): String = {
    if (redoStack.isEmpty)
      return "Cannot redo"
    undoStack.push(OldController(controller.field.copyField, controller.field.getFieldMatrix.copyFieldMatrix, controller.playerState, controller.gameState, controller.statusMessage))
    val f2 = redoStack.pop()
    setControllerToOldState(f2)
    "Redo to old state"
  }

  def setControllerToOldState(oldController: OldController): Unit = {
    controller.field = oldController.field
    controller.field.setFieldMatrix(oldController.fieldMatrix)
    controller.playerState = oldController.playerState
    controller.gameState = oldController.gameState
    controller.statusMessage = oldController.statusMessage
  }

  case class OldController(field: Field, fieldMatrix: FieldMatrix[Option[Cell]], playerState: PlayerState, gameState: GameState, statusMessage: String)

}