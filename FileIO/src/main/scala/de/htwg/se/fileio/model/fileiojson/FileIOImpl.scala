package de.htwg.se.fileio.model.fileiojson

import com.google.inject.Guice
import com.google.inject.name.Names
import de.htwg.se.fileio.model.FileIO
import model.fileiocomponent.FileIO
import model.*
import net.codingwell.scalaguice.InjectorExtensions.*
import play.api.libs.json.*

import scala.io.Source
import scala.util.{Failure, Try}

case class FileIOImpl() extends FileIO {

  override def load: Try[Game] = Try {
    val injector = Guice.createInjector(new CheckersModule)
    val source: String = Source.fromFile("game.json").getLines.mkString
    val gameJson: JsValue = Json.parse(source)
    val fieldJson = gameJson \ "field"

    val fieldSize: 8 | 10 | 12 = (fieldJson \ "size").get.as[Int] match {
      case 8 => 8
      case 10 => 10
      case 12 => 12
    }

    val playerState = if ((gameJson \ "playerState").toString.contains("1")) new PlayerState1 else new PlayerState2

    val game: Game = injector.getInstance(classOf[Game]).recreate(
      playerState = playerState,
      gameState = GameState.valueOf((gameJson \ "gameState").get.as[String]),
      size = fieldSize)

    val s1 = (fieldJson \ "fieldStatistic1").get.as[Int]
    val s2 = (fieldJson \ "fieldStatistic2").get.as[Int]
    val s3 = (fieldJson \ "fieldStatistic3").get.as[Int]
    val s4 = (fieldJson \ "fieldStatistic4").get.as[Int]
    //Rows
    val f = injector.getInstance(classOf[FieldMatrix[Option[Cell]]])
    val c = injector.getInstance(classOf[Cell])
    val vectorInts = (fieldJson \ "rows").get.as[Vector[Vector[Int]]]
    val vectorCell = vectorInts.map(_.map(x => if (x != 0) Some(c.createNewCell(x)) else None))

    game.recreate(field = game.field.recreate(fieldSize = fieldSize, fieldMatrix = f.createNewFieldMatrix(vectorCell), fieldStatistics = Map(1 -> s1, 2 -> s2, 3 -> s3, 4 -> s4)))
  }

  override def save(game: Game): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("game.json"))
    pw.write(Json.prettyPrint(gameToJson(game)))
    pw.close()
  }

  def gameToJson(game: Game): JsObject = {
    val rowsBuilder = Vector.newBuilder[Vector[Int]]

    for (row <- game.field.fieldMatrix.rows) {
      val builder = Vector.newBuilder[Int]
      row.foreach(entry => {
        if (entry.isEmpty) builder.+=(0) else builder.+=(entry.get.value)
      })
      rowsBuilder.+=(builder.result)
    }
    Json.obj(
      "field" -> Json.obj(
        "size" -> JsNumber(game.field.fieldSize),
        "fieldStatistic1" -> JsNumber(game.field.fieldStatistics.get(1).get),
        "fieldStatistic2" -> JsNumber(game.field.fieldStatistics.get(2).get),
        "fieldStatistic3" -> JsNumber(game.field.fieldStatistics.get(3).get),
        "fieldStatistic4" -> JsNumber(game.field.fieldStatistics.get(4).get),
        "rows" -> Json.toJson(rowsBuilder.result)
      ),
      "playerState" -> JsString(game.playerState.toString),
      "gameState" -> JsString(game.gameState.toString)
    )

  }
}