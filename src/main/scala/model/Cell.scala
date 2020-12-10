package model

import model.Color.{BLACK, WHITE, UNKNOWN, Color}

/**
 * Cell value definition:
 * 1: white men
 * 2: white king
 * 3: black men
 * 4: black king
 */
case class Cell(value: Int) {
  def isSet: Boolean = value != 0

  def color: Color = {
    value match {
      case 1 | 2 => WHITE
      case 3 | 4 => BLACK
      case _ => UNKNOWN
    }
  }

  override def toString: String = {
    ("▐ " + value.toString + " ▐")
  }
}

case object Color extends Enumeration {
  type Color = Value
  val BLACK, WHITE, UNKNOWN = Value
}