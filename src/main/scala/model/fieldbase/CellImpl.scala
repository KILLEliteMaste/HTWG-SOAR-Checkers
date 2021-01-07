package model.fieldbase

import model.Cell

/**
 * Cell value definition:
 * 1: white men
 * 2: white king
 * 3: black men
 * 4: black king
 */
case class CellImpl(value: Int) extends Cell {
  override def isSet: Boolean = value != 0

  override def getColor: String = {
    value match {
      case 1 | 2 => "WHITE"
      case 3 | 4 => "BLACK"
      case _ => "UNKNOWN"
    }
  }

  override def toString: String = {
    "▐ " + value.toString + " ▐"
  }

  override def getValue: Int = value

  override def createNewKing: Cell = CellImpl(value + 1)
}
