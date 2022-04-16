package de.htwg.se.fileio

import scala.util.Try
import de.htwg.se.board.Game

trait FileIO:
  def loadByString(gameString: String): Try[Game]
  def load: Try[Game]
  def save(game: Game): Unit
  def save(gameAsString: String): Unit
  def serialize(game: Game): String
