package de.htwg.se.board.dbComponent.slickImpls

import scala.util.{Failure, Random, Success, Try}
import de.htwg.se.board.{Cell, Game, GameState, PlayerState1, PlayerState2}
import de.htwg.se.board.dbComponent.DaoInterface
import de.htwg.se.board.gamebase.{CellImpl, FieldImpl, FieldMatrixImpl, GameImpl}
import slick.jdbc.GetResult
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.MySQLProfile.api.*

import java.lang.StackWalker
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

class DaoSlickImpl extends DaoInterface {

  val database = Database.forConfig("slick-mariadb")

  val gameId = sys.env.getOrElse("GAME_ID", "1").toInt
  val fieldId = gameId

  override def load(): Try[Game] = Try {
    loadGame()
  }

  def loadGame(): Game = {
    val gameQuery = sql"""select * from GAME where GAME_ID = $gameId""".as[(Int, String, String)]
    val fieldQuery = sql"""select * from FIELD where GAME_ID = $gameId and FIELD_ID = $gameId""".as[(Int, Int, Int, Int, Int, Int, Int, String)]

    val gameResult = Await.result(database.run(gameQuery), Duration.Inf).head
    val fieldResult = Await.result(database.run(fieldQuery), Duration.Inf).head

    val s1 = fieldResult(3)
    val s2 = fieldResult(4)
    val s3 = fieldResult(5)
    val s4 = fieldResult(6)

    val fieldSize: 8 | 10 | 12 = fieldResult(2) match {
      case 8 => 8
      case 10 => 10
      case 12 => 12
    }

    val fieldMatrix = createFieldMatrix(fieldResult(7), fieldSize)

    fieldMatrix.internalRows.foreach(x=> println(x))

    val gameState = GameState.valueOf(gameResult(2))

    val playerState = if (new PlayerState1().toString.equals(gameResult(1))) new PlayerState1() else new PlayerState2()

    val loadedField = new FieldImpl(fieldSize = fieldSize, fieldStatistics = Map(1 -> s1, 2 -> s2, 3 -> s3, 4 -> s4), fieldMatrix = fieldMatrix)

    val loadedGame = new GameImpl(size = loadedField.fieldSize, playerState = playerState, statusMessage = "", field = loadedField, gameState = gameState)

    loadedGame
  }

  def createFieldMatrix(stones: String, fieldSize : Int) = {
    val stonesArray = stones.split(" ")
    val vectorBuilder = Vector.newBuilder[Vector[Option[Cell]]]

    stonesArray.sliding(fieldSize, fieldSize).foreach(stonesInOneRow => {
      val rowBuilder = Vector.newBuilder[Option[Cell]]
      stonesInOneRow.foreach(stone => {
        val stoneInt = stone.toInt
        rowBuilder.addOne(if (stoneInt == 0) Option.empty else Option(CellImpl(stoneInt)))
      })
      vectorBuilder.addOne(rowBuilder.result())
    })

    new FieldMatrixImpl[Option[Cell]](internalRows = vectorBuilder.result())
  }

  def save(gameParam: Game): Unit = {
    val stones = gameParam.field.fieldMatrix.rows.flatten.map((maybeCell: Option[Cell]) => maybeCell.getOrElse(0)).mkString(" ")
    val setup = DBIO.seq(
      (gameTableQuery.schema ++ fieldTableQuery.schema).createIfNotExists,

      gameTableQuery.delete,

      gameTableQuery += (gameId, gameParam.playerState.toString, gameParam.gameState.toString),

      fieldTableQuery += (fieldId, gameId, gameParam.field.fieldSize, gameParam.field.fieldStatistics.getOrElse(0, 0),
        gameParam.field.fieldStatistics.getOrElse(1, 0), gameParam.field.fieldStatistics.getOrElse(2, 0), gameParam.field.fieldStatistics.getOrElse(3, 0), stones),
    )
    val setupFuture = database.run(setup).andThen {
      case Success(_) => println("Daten erfolgreich gespeichert")
      case Failure(e) => println(s"Fehler beim Speichern in die Datenbank: ${e.getMessage}")
    }
  }
}