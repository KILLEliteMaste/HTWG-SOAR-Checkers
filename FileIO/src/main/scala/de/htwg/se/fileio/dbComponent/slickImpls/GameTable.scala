package de.htwg.se.fileio.dbComponent.slickImpls

import slick.jdbc.MySQLProfile.api.*

class GameTable(tag: Tag) extends Table[(Int /*ID*/, String /*PlayerState*/, String /*StatusMessage*/)](tag, "GAME") {
  def id = column[Int]("GAME_ID", O.PrimaryKey) // This is the primary key column

  def playerState = column[String]("PlayerState")

  def gameState = column[String]("GameState")

  def * = (id, playerState, gameState)
}
val gameTableQuery = TableQuery[GameTable]

class FieldTable(tag: Tag) extends Table[(Int, Int, Int,Int,Int,Int,Int, String)](tag, "FIELD") {
  def id = column[Int]("FIELD_ID", O.PrimaryKey)

  def gameID = column[Int]("GAME_ID")

  def size = column[Int]("Size")

  def fieldStatistics1 = column[Int]("FieldStatistics1")

  def fieldStatistics2 = column[Int]("FieldStatistics2")

  def fieldStatistics3 = column[Int]("FieldStatistics3")

  def fieldStatistics4 = column[Int]("FieldStatistics4")

  def stones = column[String]("Stones")

  def * = (id, gameID, size, fieldStatistics1,fieldStatistics2,fieldStatistics3,fieldStatistics4, stones)

  // A reified foreign key relation that can be navigated to create a join
  def gameFK = foreignKey("GAME_FK", gameID, gameTableQuery)(targetColumns = _.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
}
val fieldTableQuery = TableQuery[FieldTable]
