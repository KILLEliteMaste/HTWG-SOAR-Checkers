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

    val game: Game = injector.instance[Game](Names.named(size.toString))

    val gameState = (file \\ "game" \ "gameState").text.trim
    //game.setGameState(GameState.IDLE)
    //println("++++++++++++"+gameState+"++++++++++")
    game.setGameState(GameState.withName(gameState))
    val playerState = (file \\ "game" \ "playerState").text.trim
    game.setPlayerState(if (playerState.contains("1")) new PlayerState1 else new PlayerState2)
    val statusMessage = (file \\ "game" \ "statusMessage").text.trim
    game.setStatusMessage(statusMessage)
    val fieldStatistic1 = (file \\ "game" \ "fieldStatistic1").text.trim.toInt
    game.getField.setFieldStatistics(1, fieldStatistic1)
    val fieldStatistic2 = (file \\ "game" \ "fieldStatistic2").text.trim.toInt
    game.getField.setFieldStatistics(2, fieldStatistic2)
    val fieldStatistic3 = (file \\ "game" \ "fieldStatistic3").text.trim.toInt
    game.getField.setFieldStatistics(3, fieldStatistic3)
    val fieldStatistic4 = (file \\ "game" \ "fieldStatistic4").text.trim.toInt
    game.getField.setFieldStatistics(4, fieldStatistic4)

    val fieldMatrix = file \\ "game" \ "fieldMatrix"
    val vectorBuilder = Vector.newBuilder[Vector[Option[Cell]]]
    for (row <- (fieldMatrix \ "cell").sliding(size, size)) {
      val rowBuilder = Vector.newBuilder[Option[Cell]]
      for (cell <- row) {
        val c = injector.instance[Cell]
        val cellVal = (cell \ "value").text.trim.toInt
        if (cellVal != 0) rowBuilder.+=(Some(c.createNewCell(cellVal))) else rowBuilder.+=(None)
      }
      vectorBuilder.+=(rowBuilder.result)
    }
    val f = injector.instance[FieldMatrix[Option[Cell]]]
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
