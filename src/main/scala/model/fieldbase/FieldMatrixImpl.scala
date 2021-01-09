package model.fieldbase

import model.FieldMatrix


case class FieldMatrixImpl[T](rows: Vector[Vector[T]]) extends FieldMatrix[T] {
  def this(size: Int, filling: T) = this(Vector.tabulate(size, size) { (row, col) => filling })
  def this() = this(null)

  override def cell(row: Int, col: Int): T = rows(row)(col)

  override def replaceCell(row: Int, col: Int, cell: T): FieldMatrixImpl[T] = copy(rows.updated(row, rows(row).updated(col, cell)))

  override def getRows: Vector[Vector[T]] = rows

  override def getSize: Int = rows.size

  override def toString: String = {
    rows.toString()
  }

  override def copyFieldMatrix: FieldMatrix[T] = this.copy()

  override def createNewFieldMatrix(value: Vector[Vector[T]]): FieldMatrix[T] = new FieldMatrixImpl[T](value)
}
