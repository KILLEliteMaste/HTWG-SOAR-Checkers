package de.htwg.se.fileio.fileioxml

import com.google.inject.Guice
import com.google.inject.name.Names
import de.htwg.se.fileio.FileIO
import de.htwg.se.fileio.{FileIO, FileIOModule}
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import de.htwg.se.board.*
import java.io.*
import scala.util.Try
import scala.xml.*

case class FileIOImpl() extends FileIO {
  override def load: Try[Game] = Try {
    val file = scala.xml.XML.load("game.xml")
    load(file)
  }

  override def loadByString(gameString: String): Try[Game] = Try {
    val file = scala.xml.XML.loadString(gameString)
    load(file)
  }

  def load(file : Elem) = {
    val fieldSize: 8 | 10 | 12 = (file \\ "game" \ "fieldSize").text.trim.toInt match {
      case 8 => 8
      case 10 => 10
      case 12 => 12
    }

    val injector = Guice.createInjector(new FileIOModule)

    val game: Game = injector.getInstance(classOf[Game]).recreate(
      gameState = GameState.valueOf((file \\ "game" \ "gameState").text.trim),
      playerState = if ((file \\ "game" \ "playerState").text.trim.contains("1")) new PlayerState1 else new PlayerState2,
      statusMessage = (file \\ "game" \ "statusMessage").text.trim,
      size = fieldSize)

    val s1 = (file \\ "game" \ "fieldStatistic1").text.trim.toInt
    val s2 = (file \\ "game" \ "fieldStatistic2").text.trim.toInt
    val s3 = (file \\ "game" \ "fieldStatistic3").text.trim.toInt
    val s4 = (file \\ "game" \ "fieldStatistic4").text.trim.toInt

    val fieldMatrix = file \\ "game" \ "fieldMatrix"
    val vectorBuilder = Vector.newBuilder[Vector[Option[Cell]]]
    for (row <- (fieldMatrix \ "cell").sliding(fieldSize, fieldSize)) {
      val rowBuilder = Vector.newBuilder[Option[Cell]]
      for (cell <- row) {
        val c = injector.getInstance(classOf[Cell])
        val cellVal = (cell \ "value").text.trim.toInt
        if (cellVal != 0) rowBuilder.+=(Some(c.createNewCell(cellVal))) else rowBuilder.+=(None)
      }
      vectorBuilder.+=(rowBuilder.result)
    }
    val f = injector.getInstance(classOf[FieldMatrix[Option[Cell]]])
    game.recreate(field = game.field.recreate(fieldSize = fieldSize, fieldMatrix = f.createNewFieldMatrix(vectorBuilder.result), fieldStatistics = collection.immutable.HashMap(1 -> s1, 2 -> s2, 3 -> s3, 4 -> s4)))
  }


  override def save(game: Game): Unit = saveString(game)

  def saveString(game: Game): Unit = {
    val pw = new PrintWriter(new File("game.xml"))
    val prettyPrinter = new PrettyPrinter(120, 4)
    val xml = prettyPrinter.format(gameToXml(game))
    pw.write(xml)
    pw.close()
  }

  override def save(gameString: String): Unit = {
    val pw = new PrintWriter(new File("game.xml"))
    val prettyPrinter = new PrettyPrinter(120, 4)
    pw.write(gameString)
    pw.close()
  }

  override def serialize(game: Game): String = gameToXml(game).toString()

  def gameToXml(game: Game): Elem = {
    <game>
      <fieldSize>
        {game.field.fieldSize}
      </fieldSize>
      <playerState>
        {game.playerState.toString}
      </playerState>
      <statusMessage>
        {game.statusMessage}
      </statusMessage>
      <gameState>
        {game.gameState.toString}
      </gameState>
      <fieldStatistic1>
        {game.field.fieldStatistics.get(1).get}
      </fieldStatistic1>
      <fieldStatistic2>
        {game.field.fieldStatistics.get(2).get}
      </fieldStatistic2>
      <fieldStatistic3>
        {game.field.fieldStatistics.get(3).get}
      </fieldStatistic3>
      <fieldStatistic4>
        {game.field.fieldStatistics.get(4).get}
      </fieldStatistic4>
      <fieldMatrix>
        {for {
        row <- 0 until game.field.fieldSize
        col <- 0 until game.field.fieldSize
      } yield getCell(game, row, col)}
      </fieldMatrix>
    </game>
  }

  def getCell(game: Game, row: Int, col: Int): Elem = {
    <cell>
      <value>
        {game.field.fieldMatrix.cell(row, col).map(cell => cell.value).getOrElse(0).toString}
      </value>
    </cell>
  }
}
