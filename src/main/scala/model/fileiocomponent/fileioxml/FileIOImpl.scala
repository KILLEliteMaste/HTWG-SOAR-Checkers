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

    val game: Game = injector.getInstance(classOf[Game])
    game.setGameState(GameState.valueOf((file \\ "game" \ "gameState").text.trim))
    game.setPlayerState(if ((file \\ "game" \ "playerState").text.trim.contains("1")) new PlayerState1 else new PlayerState2)
    game.setStatusMessage((file \\ "game" \ "statusMessage").text.trim)
    game.getField.setFieldStatistics(1, (file \\ "game" \ "fieldStatistic1").text.trim.toInt)
    game.getField.setFieldStatistics(2, (file \\ "game" \ "fieldStatistic2").text.trim.toInt)
    game.getField.setFieldStatistics(3, (file \\ "game" \ "fieldStatistic3").text.trim.toInt)
    game.getField.setFieldStatistics(4, (file \\ "game" \ "fieldStatistic4").text.trim.toInt)

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
    game.getField.setFieldMatrix(f.createNewFieldMatrix(vectorBuilder.result))
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
        {game.getField.getFieldSize}
      </fieldSize>
      <playerState>
        {game.getPlayerState.toString}
      </playerState>
      <statusMessage>
        {game.getStatusMessage}
      </statusMessage>
      <gameState>
        {game.getGameState.toString}
      </gameState>
      <fieldStatistic1>
        {game.getField.getFieldStatistics(1)}
      </fieldStatistic1>
      <fieldStatistic2>
        {game.getField.getFieldStatistics(2)}
      </fieldStatistic2>
      <fieldStatistic3>
        {game.getField.getFieldStatistics(3)}
      </fieldStatistic3>
      <fieldStatistic4>
        {game.getField.getFieldStatistics(4)}
      </fieldStatistic4>
      <fieldMatrix>
        {for {
        row <- 0 until game.getField.getFieldSize
        col <- 0 until game.getField.getFieldSize
      } yield getCell(game, row, col)}
      </fieldMatrix>
    </game>
  }

  def getCell(game: Game, row: Int, col: Int): Elem = {
    <cell>
      <value>
        {game.getField.getFieldMatrix.cell(row, col).map(cell => cell.getValue).getOrElse(0).toString}
      </value>
    </cell>
  }
}
