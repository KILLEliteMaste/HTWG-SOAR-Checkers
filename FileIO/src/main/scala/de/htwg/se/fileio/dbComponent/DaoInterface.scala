package de.htwg.se.fileio.dbComponent

import scala.util.Try
import de.htwg.se.board.Game
import scala.concurrent.Future

trait DaoInterface {

  def load(): Future[Try[Game]]
  def save(game: Game): Unit

}