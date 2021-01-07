package model

trait FieldMatrix[T] {
  def cell(row: Int, col: Int): T

  def replaceCell(row: Int, col: Int, cell: T): FieldMatrix[T]

  def getRows: Vector[Vector[T]]

  def getSize: Int

  def copyFieldMatrix: FieldMatrix[T]
}
