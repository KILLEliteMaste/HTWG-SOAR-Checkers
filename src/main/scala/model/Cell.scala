package model

/**
 * Cell value definition:
 * 0: empty field
 * 1: white men
 * 2: white king
 * 3: black men
 * 4: black king
 */
case class Cell(value: Int) {
  def isSet: Boolean = value != 0

  override def toString: String = {
    ("▐ " + value.toString + " ▐").replace("▐ 0 ▐", "▐   ▐")
  }
}
