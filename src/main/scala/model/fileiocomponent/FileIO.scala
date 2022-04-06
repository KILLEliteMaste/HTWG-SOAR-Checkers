package model.fileiocomponent

import model.Game

import scala.util.Try

trait FileIO:
  def load: Try[Game]
  def save(game: Game): Unit