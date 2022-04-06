package controller

import aview.gui.Gui
import model.{Cell, Field, FieldMatrix, Game}
import util.{Observable, Position, UndoManager}

trait ControllerInterface extends Observable :
  def createNewField(): Field
  def createNewField(size: 8 | 10 | 12): Field
  def changePlayerTurn(): Unit
  def isStoneOpponentsColor(stoneToJumpOver: Int, stoneToMove: Int): Boolean
  def caseJumpOverStone(positionFrom: Position, positionTo: Position, stoneToMove: Int, x: Int, y: Int): Unit
  def moveFromPositionToPosition(positionFrom: Position, positionTo: Position, stoneToMove: Int, alreadyMoved: Boolean): Unit
  def checkGameState(): Unit
  def moveToNewPosition(origin: Position, destination: Position, field: Field): FieldMatrix[Option[Cell]]
  def checkIfAllPositionsAreInBounds(vector: Vector[Position], field: Field): Boolean
  def checkIfAllCellsAreEmpty(field: Field, positions: Vector[Position]): Boolean
  def checkIfAllCellsBelongToPlayer(field: Field, positions: Vector[Position]): Boolean
  def doStep(): Unit
  def undo(): String
  def redo(): String
  def matrixToString: String
  def save(): Unit
  def load(): String
  def getGame: Game
  def setGame(newGame :Game): Unit
