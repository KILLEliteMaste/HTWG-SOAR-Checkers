package model.gamebase

import model.FieldMatrix

case class FieldMatrixImpl[T](internalRows: Vector[Vector[T]]) extends FieldMatrix[T](internalRows.size, internalRows) {
  def this(size: Int, filling: T) = this(Vector.tabulate(size, size) { (row, col) => filling })

  override def cell(row: Int, col: Int): T = internalRows(row)(col)

  override def replaceCell(row: Int, col: Int, cell: T): FieldMatrixImpl[T] = copy(internalRows.updated(row, internalRows(row).updated(col, cell)))

  override def toString: String = {
    internalRows.toString()
  }

  override def copyFieldMatrix: FieldMatrix[T] = this.copy()

  override def createNewFieldMatrix(value: Vector[Vector[T]]): FieldMatrix[T] = new FieldMatrixImpl[T](value)
}
