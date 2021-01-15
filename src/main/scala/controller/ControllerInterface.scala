package controller

import aview.gui.Gui
import util.{Observable, Position}
import model.{Cell, Field, FieldMatrix, Game}

trait ControllerInterface extends Observable {

  def createNewField(): Unit

  def createNewField(size: Int): Unit

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

  def load(): Unit

  def getGame: Game
}
