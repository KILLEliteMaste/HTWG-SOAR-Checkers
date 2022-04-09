package de.htwg.se.board.gamebase

import com.google.inject.name.Named
import de.htwg.se.board.{Field, FieldMatrix, Cell}
import de.htwg.se.board.gamebase.{FieldImpl, FieldMatrixImpl, CellImpl}
import javax.inject.Inject
import de.htwg.se.board.*

case class FieldImpl @Inject()(@Named("DefaultSize")
                               override val fieldSize: 8 | 10 | 12,
                               override val fieldStatistics: Map[Int, Int] = Map[Int, Int](1 -> 0, 2 -> 0, 3 -> 0, 4 -> 0),
                               override val fieldMatrix: FieldMatrix[Option[Cell]] = new FieldMatrixImpl[Option[Cell]](FieldImplHelper.generateFieldMatrix(8)))
  extends Field(fieldSize, fieldStatistics, fieldMatrix) {

  def this(fieldSizeSecondary: 8 | 10 | 12) = this(fieldSize = fieldSizeSecondary, fieldMatrix = new FieldMatrixImpl[Option[Cell]](FieldImplHelper.generateFieldMatrix(fieldSizeSecondary)))

  override def recreate(fieldSize: 8 | 10 | 12 = fieldSize, fieldStatistics: Map[Int, Int] = fieldStatistics, fieldMatrix: FieldMatrix[Option[Cell]] = fieldMatrix): Field = copy(fieldSize, fieldStatistics, fieldMatrix)

  def updateFieldStatistics(value: Int)(cellValue: Int): Field = copy(fieldStatistics = fieldStatistics + (cellValue -> (fieldStatistics.getOrElse(cellValue, 0) + value)))
  override def decreaseFieldStatistics(cellValue: Int): Field = updateFieldStatistics(-1)(cellValue)
  override def increaseFieldStatistics(cellValue: Int): Field = updateFieldStatistics(+1)(cellValue)
  def numberToRow(index: Int): String = fieldMatrix.rows(index).map(_.getOrElse(" ")).mkString(index.toString + "  ▐ ", " ▐ ", " ▐\n")
  override def toString: String = (("    " + (0 until fieldSize).map(" " + _ + "  ").mkString("") + "\n") +: (0 until fieldSize).map(numberToRow(_))).mkString("")
  override def copyField: Field = copy()
  override def createNewField(size: 8 | 10 | 12): Field = FieldImpl(size, Map[Int, Int](1 -> 0, 2 -> 0, 3 -> 0, 4 -> 0), new FieldMatrixImpl[Option[Cell]](FieldImplHelper.generateFieldMatrix(size)))
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