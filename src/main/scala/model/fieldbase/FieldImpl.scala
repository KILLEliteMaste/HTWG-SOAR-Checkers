package model.fieldbase

import model.{Field, FieldMatrix}

import scala.collection.mutable

case class FieldImpl(fieldSize: Int) extends Field {

  val fieldStatistics = new mutable.HashMap[Int, Int]()
  fieldStatistics.put(1, 0)
  fieldStatistics.put(2, 0)
  fieldStatistics.put(3, 0)
  fieldStatistics.put(4, 0)


  var matrix: FieldMatrix[Option[model.Cell]] = {
    val builder = Vector.newBuilder[Vector[Option[CellImpl]]]
    for {index <- 1 to fieldSize} {
      if (index < 4) {
        //Fill the rows for white
        if (index % 2 == 0) {
          builder.+=(Vector.tabulate(fieldSize)(n => if (n % 2 == 0) Some(CellImpl(1)) else None))
        } else {
          builder.+=(Vector.tabulate(fieldSize)(n => if (n % 2 == 0) None else Some(CellImpl(1))))
        }
        fieldStatistics.put(1, fieldStatistics.get(1).sum + fieldSize / 2)
      } else if (fieldSize - 3 < index) {
        //Fill the rows for black
        if (index % 2 == 0) {
          builder.+=(Vector.tabulate(fieldSize)(n => if (n % 2 == 0) Some(CellImpl(3)) else None))
        } else {
          builder.+=(Vector.tabulate(fieldSize)(n => if (n % 2 == 0) None else Some(CellImpl(3))))
        }
        fieldStatistics.put(3, fieldStatistics.get(3).sum + fieldSize / 2)
      } else {
        //Empty row
        builder.+=(Vector.fill(fieldSize)(None))
      }
    }
    FieldMatrixImpl(builder.result())
  }

  override def getFieldMatrix: FieldMatrix[Option[model.Cell]] = matrix

  override def setFieldMatrix(newMatrix: FieldMatrix[Option[model.Cell]]): Unit = matrix = newMatrix

  override def getFieldStatistics(stone: Int): Int = fieldStatistics.getOrElse(stone, 0)

  override def setFieldStatistics(stone: Int, amount: Int): Unit = fieldStatistics.put(stone, amount)

  override def getTotalFieldSize: Int = fieldSize * fieldSize

  override def getFieldSize: Int = fieldSize

  override def toString: String = {
    var str = "  "
    for (i <- 0 until fieldSize) {
      str += "   " + i
    }
    var output = str + "\n"
    var counter = 0
    for (row <- matrix.getRows) {
      output += counter + "  "
      counter = counter + 1
      for (cell <- row) {
        output += cell.getOrElse("▐   ▐")
      }
      output += "\n"
    }
    output = output.replace("▐▐", "▐")
    output
  }

  override def copyField: Field = copy()

  override def getNewField(size: Int): Field = FieldImpl(size)
}
