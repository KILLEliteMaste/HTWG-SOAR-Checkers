package model

import scala.collection.immutable

trait Field(val fieldSize: 8 | 10 | 12, val fieldStatistics: immutable.HashMap[Int, Int], val fieldMatrix: FieldMatrix[Option[Cell]]) {

  def recreate(fieldSize: 8 | 10 | 12 = fieldSize, fieldStatistics: immutable.HashMap[Int, Int] = fieldStatistics, fieldMatrix: FieldMatrix[Option[Cell]] = fieldMatrix): Field

  def createNewField(size: 8 | 10 | 12): Field

  def decreaseFieldStatistics(cellValue: Int): Field

  def increaseFieldStatistics(cellValue: Int): Field

  def copyField: Field
}
