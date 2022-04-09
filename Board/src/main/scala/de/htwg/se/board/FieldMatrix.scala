package de.htwg.se.board

trait FieldMatrix[T](val size: Int, val rows: Vector[Vector[T]]) {
  def cell(row: Int, col: Int): T

  def replaceCell(row: Int, col: Int, cell: T): FieldMatrix[T]

  def copyFieldMatrix: FieldMatrix[T]

  def createNewFieldMatrix(value: Vector[Vector[T]]): FieldMatrix[T]
}
