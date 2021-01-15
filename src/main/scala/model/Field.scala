package model

trait Field {

  def getTotalFieldSize: Int

  def getFieldSize: Int

  def getFieldStatistics(stone: Int): Int

  def setFieldStatistics(stone: Int, amount: Int): Unit

  def getFieldMatrix: FieldMatrix[Option[Cell]]

  def setFieldMatrix(newMatrix: FieldMatrix[Option[Cell]]): Unit

  def getNewField(size: Int): Field

  def copyField: Field
}
