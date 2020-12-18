package model


trait Cell {
  def getValue: Int

  def isSet: Boolean

  def getColor: String

  def createNewKing: Cell
}
