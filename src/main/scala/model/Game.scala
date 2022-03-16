package model

import GameState.GameState

trait Game {
  def getGameState: GameState

  def setGameState(gameState: GameState): Unit

  def getPlayerState: PlayerState

  def setPlayerState(playerState: PlayerState): Unit

  def getStatusMessage: String

  def setStatusMessage(status: String): Unit

  def getField: Field

  def setField(field: Field): Unit
}
