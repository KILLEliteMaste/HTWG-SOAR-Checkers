package model.fileiocomponent.fileioxml

import com.google.inject.Guice
import com.google.inject.name.Names
import model.fileiocomponent.FileIO
import model.{GameState, PlayerState1, PlayerState2, _}
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector

import java.io._
import scala.xml._

case class FileIOImpl() extends FileIO {
  override def load: Game = {
    val file = scala.xml.XML.loadFile("game.xml")
    val size = (file \\ "game" \ "fieldSize").text.trim.toInt

    val injector = Guice.createInjector(new CheckersModule)

    var  game: Game = injector.getInstance(classOf[Game])
    game = game.recreate(gameState = GameState.valueOf((file \\ "game" \ "gameState").text.trim), playerState = if ((file \\ "game" \ "playerState").text.trim.contains("1")) new PlayerState1 else new PlayerState2, statusMessage = (file \\ "game" \ "statusMessage").text.trim)
    game.field.fieldStatistics.put(1, (file \\ "game" \ "fieldStatistic1").text.trim.toInt)
    game.field.fieldStatistics.put(2, (file \\ "game" \ "fieldStatistic2").text.trim.toInt)
    game.field.fieldStatistics.put(3, (file \\ "game" \ "fieldStatistic3").text.trim.toInt)
    game.field.fieldStatistics.put(4, (file \\ "game" \ "fieldStatistic4").text.trim.toInt)

    val fieldMatrix = file \\ "game" \ "fieldMatrix"
    val vectorBuilder = Vector.newBuilder[Vector[Option[Cell]]]
    for (row <- (fieldMatrix \ "cell").sliding(size, size)) {
      val rowBuilder = Vector.newBuilder[Option[Cell]]
      for (cell <- row) {
        val c = injector.getInstance(classOf[Cell])
        val cellVal = (cell \ "value").text.trim.toInt
        if (cellVal != 0) rowBuilder.+=(Some(c.createNewCell(cellVal))) else rowBuilder.+=(None)
      }
      vectorBuilder.+=(rowBuilder.result)
    }
    val f = injector.getInstance(classOf[FieldMatrix[Option[Cell]]])
    game = game.recreate(field = game.field.recreate(fieldMatrix = f.createNewFieldMatrix(vectorBuilder.result)))
    game
  }

  override def save(game: Game): Unit = saveString(game)

  def saveString(game: Game): Unit = {
    val pw = new PrintWriter(new File("game.xml"))
    val prettyPrinter = new PrettyPrinter(120, 4)
    val xml = prettyPrinter.format(gameToXml(game))
    pw.write(xml)
    pw.close()
  }

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
