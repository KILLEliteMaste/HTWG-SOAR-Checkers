package model.fileiocomponent.fileiojson

import com.google.inject.Guice
import com.google.inject.name.Names
import model.fileiocomponent.FileIO
import model._
import net.codingwell.scalaguice.InjectorExtensions._
import play.api.libs.json._

import scala.io.Source


case class FileIOImpl() extends FileIO {

  override def load: Game = {
    val injector = Guice.createInjector(new CheckersModule)
    val source: String = Source.fromFile("game.json").getLines.mkString
    val gameJson: JsValue = Json.parse(source)
    val fieldJson = gameJson \ "field"
    val fieldSize = (fieldJson \ "size").get.as[Int]

    val game: Game = injector.instance[Game](Names.named(fieldSize.toString))

    //States
    val playerState = if ((gameJson \ "playerState").toString.contains("1")) new PlayerState1 else new PlayerState2
    game.setPlayerState(playerState)
    game.setGameState(GameState.withName((gameJson \ "gameState").get.as[String]))
    game.getField.setFieldStatistics(1, (fieldJson \ "fieldStatistic1").get.as[Int])
    game.getField.setFieldStatistics(2, (fieldJson \ "fieldStatistic2").get.as[Int])
    game.getField.setFieldStatistics(3, (fieldJson \ "fieldStatistic3").get.as[Int])
    game.getField.setFieldStatistics(4, (fieldJson \ "fieldStatistic4").get.as[Int])
    //Rows
    val f = injector.instance[FieldMatrix[Option[Cell]]]
    val c = injector.instance[Cell]
    val vectorInts = (fieldJson \ "rows").get.as[Vector[Vector[Int]]]
    val vectorCell = vectorInts.map(_.map(x => if (x != 0) Some(c.createNewCell(x)) else None))
    game.getField.setFieldMatrix(f.createNewFieldMatrix(vectorCell))
    game
  }

  override def save(game: Game): Unit = {
    import java.io._
    val pw = new PrintWriter(new File("game.json"))
    pw.write(Json.prettyPrint(gameToJson(game)))
    pw.close()
  }

  def gameToJson(game: Game): JsObject = {

    val rowsBuilder = Vector.newBuilder[Vector[Int]]

    for (row <- game.getField.getFieldMatrix.getRows) {
      val builder = Vector.newBuilder[Int]
      row.foreach(entry => {
        if (entry.isEmpty) builder.+=(0) else builder.+=(entry.get.getValue)
      })
      rowsBuilder.+=(builder.result)
    }
    Json.obj(
      "field" -> Json.obj(
        "size" -> JsNumber(game.getField.getFieldSize),
        "fieldStatistic1" -> JsNumber(game.getField.getFieldStatistics(1)),
        "fieldStatistic2" -> JsNumber(game.getField.getFieldStatistics(2)),
        "fieldStatistic3" -> JsNumber(game.getField.getFieldStatistics(3)),
        "fieldStatistic4" -> JsNumber(game.getField.getFieldStatistics(4)),
        "rows" -> Json.toJson(rowsBuilder.result)
      ),
      "playerState" -> JsString(game.getPlayerState.toString),
      "gameState" -> JsString(game.getGameState.toString)
    )

  }
}