package model.fileiocomponent

import model.Game

trait FileIO {

  def load: Game

  def save(game: Game): Unit

}
