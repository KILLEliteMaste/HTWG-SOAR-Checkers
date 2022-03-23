package model

import scala.collection.mutable

trait Field(val fieldSize: 8 | 10 | 12, val fieldStatistics: mutable.HashMap[Int, Int], val fieldMatrix: FieldMatrix[Option[Cell]]) {

  def recreate(fieldSize: 8 | 10 | 12 = fieldSize, fieldStatistics: mutable.HashMap[Int, Int] = fieldStatistics, fieldMatrix: FieldMatrix[Option[Cell]] = fieldMatrix): Field

  def createNewField(size: 8 | 10 | 12): Field

  def decreaseFieldStatistics(cellValue: Int): Unit

  def increaseFieldStatistics(cellValue: Int): Unit

  def copyField: Field
}
