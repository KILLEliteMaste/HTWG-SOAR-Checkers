package controller
import util.Position
import model.{Cell, Field, FieldMatrix}

trait ControllerInterface {

  def createNewField(): Unit

  def createNewField(size: Int): Unit

  def changePlayerTurn(): Unit

  def isStoneOpponentsColor(stoneToJumpOver: Int, stoneToMove: Int): Boolean

  def caseJumpOverStone(positionFrom: Position, positionTo: Position, stoneToMove: Int, x: Int, y: Int): Unit

  def moveFromPositionToPosition(positionFrom: Position, positionTo: Position, stoneToMove: Int, alreadyMoved: Boolean): Unit

  def checkGameState(): Unit

  /**
   *
   * @param origin      The Origin position to move from
   * @param destination The destination position to move to
   * @param field       The field which will be moved in
   * @return A new FieldMatrix
   */
  def moveToNewPosition(origin: Position, destination: Position, field: Field): FieldMatrix[Option[Cell]]

  /**
   *
   * @param vector A vector containing one or more positions inside another vector
   * @param field  The field with the fieldsize
   * @return If the given position are inside the bound of the fieldsize
   */
  def checkIfAllPositionsAreInBounds(vector: Vector[Position], field: Field): Boolean

  def checkIfAllCellsAreEmpty(field: Field, positions: Vector[Position]): Boolean

  def checkIfAllCellsBelongToPlayer(field: Field, positions: Vector[Position]): Boolean

  def doStep(): Unit

  def undo(): String

  def redo(): String

  def matrixToString: String
}
