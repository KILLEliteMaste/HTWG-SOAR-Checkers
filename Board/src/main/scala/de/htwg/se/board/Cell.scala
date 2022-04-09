package de.htwg.se.board

trait Cell(val value: Int) {

  def isSet: Boolean

  def getColor: String

  def createNewKing: Cell

  def createNewCell(value:Int):Cell
}
