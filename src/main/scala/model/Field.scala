package model

import scala.collection.mutable

case class Field(fieldSize: Int) {
  val fieldStatistics = new mutable.HashMap[Int, Int]()
  fieldStatistics.put(1, 0)
  fieldStatistics.put(2, 0)
  fieldStatistics.put(3, 0)
  fieldStatistics.put(4, 0)
  var statusString: String = ""

  def totalFieldSize: Int = fieldSize * fieldSize

  var matrix: FieldMatrix[Cell] = {
    val builder = Vector.newBuilder[Vector[Cell]]
    for {index <- 1 to fieldSize} {
      if (index < 4) {
        //Fill the rows for white
        if (index % 2 == 0) {
          builder.+=(Vector.tabulate(fieldSize)(n => if (n % 2 == 0) Cell(1) else Cell(0)))
        } else {
          builder.+=(Vector.tabulate(fieldSize)(n => if (n % 2 == 0) Cell(0) else Cell(1)))
        }
        fieldStatistics.put(1, (fieldStatistics.get(1).sum + fieldSize / 2))
      } else if (fieldSize - 3 < index) {
        //Fill the rows for black
        if (index % 2 == 0) {
          builder.+=(Vector.tabulate(fieldSize)(n => if (n % 2 == 0) Cell(3) else Cell(0)))
        } else {
          builder.+=(Vector.tabulate(fieldSize)(n => if (n % 2 == 0) Cell(0) else Cell(3)))
        }
        fieldStatistics.put(3, (fieldStatistics.get(3).sum + fieldSize / 2))
      } else {
        //Empty row
        builder.+=(Vector.fill(fieldSize)(Cell(0)))
      }
    }
    FieldMatrix(builder.result())
  }

  override def toString: String = {
    var str = "  "
    for (i <- 0 until fieldSize) {
      str += "   " + i
    }
    var output = str + "\n"
    var counter = 0
    for (row <- matrix.rows) {
      output += counter + "  "
      counter = counter + 1
      for (cell <- row) {
        output += cell
      }
      output += "\n"
    }
    output = output.replace("▐▐", "▐")
    output
  }
}

case class FieldMatrix[T](rows: Vector[Vector[T]]) {
  def this(size: Int, filling: T) = this(Vector.tabulate(size, size) { (row, col) => filling })

  val size: Int = rows.size

  def cell(row: Int, col: Int): T = rows(row)(col)

  def replaceCell(row: Int, col: Int, cell: T): FieldMatrix[T] = copy(rows.updated(row, rows(row).updated(col, cell)))

  def fill(filling: T): FieldMatrix[T] = copy(Vector.tabulate(size, size) { (row, col) => filling })

  override def toString: String = {
    rows.toString()
  }
}