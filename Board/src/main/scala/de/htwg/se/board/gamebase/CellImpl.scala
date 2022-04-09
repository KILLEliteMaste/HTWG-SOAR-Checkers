package de.htwg.se.board.gamebase

import de.htwg.se.board.*

/**
 * Cell value definition:
 * 1: white men
 * 2: white king
 * 3: black men
 * 4: black king
 */
case class CellImpl(override val value: Int) extends Cell(value) {

  override def isSet: Boolean = value != 0

  override def getColor: String = {
    value match {
      case 1 | 2 => "WHITE"
      case 3 | 4 => "BLACK"
      case _ => "UNKNOWN"
    }
  }

  override def toString: String = {
    //"▐ " + value.toString + " ▐"
    value.toString
  }

  override def createNewKing: Cell = CellImpl(value + 1)

  override def createNewCell(value: Int): Cell = CellImpl(value)
}
