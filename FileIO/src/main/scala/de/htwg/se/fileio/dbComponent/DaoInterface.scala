package de.htwg.se.fileio.dbComponent

import scala.util.Try
import de.htwg.se.board.Game
import scala.concurrent.Future

trait DaoInterface {

  def load(): Try[Game]
  def save(game: Game): Unit

}