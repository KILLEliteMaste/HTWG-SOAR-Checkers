package model

trait Field {

  def getTotalFieldSize: Int

  def getFieldSize: 8 | 10 | 12

  def getFieldStatistics(stone: Int): Int

  def setFieldStatistics(stone: Int, amount: Int): Unit

  def getFieldMatrix: FieldMatrix[Option[Cell]]

  def setFieldMatrix(newMatrix: FieldMatrix[Option[Cell]]): Unit

  def getNewField(size: 8 | 10 | 12): Field

  def copyField: Field
}
