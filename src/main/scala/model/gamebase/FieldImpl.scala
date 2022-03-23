package model.gamebase

import com.google.inject.name.Named
import model.{Cell, Field, FieldMatrix}

import javax.inject.Inject
import scala.collection.mutable

case class FieldImpl @Inject()(@Named("DefaultSize") override val fieldSize: 8 | 10 | 12, override val fieldStatistics: mutable.HashMap[Int, Int] = new mutable.HashMap[Int, Int](), override val fieldMatrix: FieldMatrix[Option[Cell]] = new FieldMatrixImpl[Option[Cell]](FieldImplHelper.generateFieldMatrix(8))) extends Field(fieldSize, fieldStatistics, fieldMatrix) {

  fieldStatistics.put(1, 0)
  fieldStatistics.put(2, 0)
  fieldStatistics.put(3, 0)
  fieldStatistics.put(4, 0)

  fieldStatistics.put(3, countStones(3))
  fieldStatistics.put(1, countStones(1))

  def countStones(searchValue: Int): Int = {
    var counter = 0
    fieldMatrix.rows.foreach(vector => {
      counter = counter + vector.filter(_.isDefined).map(_.get).count(_.value == searchValue)
    })
    counter
  }

  override def recreate(fieldSize: 8 | 10 | 12 = fieldSize, fieldStatistics: mutable.HashMap[Int, Int] = fieldStatistics, fieldMatrix: FieldMatrix[Option[Cell]] = fieldMatrix): Field = copy(fieldSize, fieldStatistics, fieldMatrix)

  def updateFieldStatistics(value: Int)(cellValue: Int): Unit = fieldStatistics.updateWith(cellValue)({
    case Some(count) => Some(count + value)
    case None => Some(1)
  })

  override def decreaseFieldStatistics(cellValue: Int): Unit = updateFieldStatistics(-1)(cellValue)

  override def increaseFieldStatistics(cellValue: Int): Unit = updateFieldStatistics(+1)(cellValue)

  override def toString: String = {
    var str = "  "
    for (i <- 0 until fieldSize) {
      str += "   " + i
    }
    var output = str + "\n"
    var counter = 0
    for (row <- fieldMatrix.rows) {
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

  override def createNewField(size: 8 | 10 | 12): Field = FieldImpl(size, new mutable.HashMap[Int, Int](), new FieldMatrixImpl[Option[Cell]](FieldImplHelper.generateFieldMatrix(size)))

}

object FieldImplHelper {

  def generateFieldMatrix(fieldSize: Int): Vector[Vector[Option[CellImpl]]] = {
    val builder = Vector.newBuilder[Vector[Option[CellImpl]]]
    for {index <- 1 to fieldSize} {
      if (index < 4) {
        //Fill the rows for white
        if (index % 2 == 0) {
          builder.+=(Vector.tabulate(fieldSize)(n => if (n % 2 == 0) Some(CellImpl(1)) else None))
        } else {
          builder.+=(Vector.tabulate(fieldSize)(n => if (n % 2 == 0) None else Some(CellImpl(1))))
        }

      } else if (fieldSize - 3 < index) {
        //Fill the rows for black
        if (index % 2 == 0) {
          builder.+=(Vector.tabulate(fieldSize)(n => if (n % 2 == 0) Some(CellImpl(3)) else None))
        } else {
          builder.+=(Vector.tabulate(fieldSize)(n => if (n % 2 == 0) None else Some(CellImpl(3))))
        }

      } else {
        //Empty row
        builder.+=(Vector.fill(fieldSize)(None))
      }
    }
    builder.result()
  }
}