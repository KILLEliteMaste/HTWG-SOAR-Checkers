package model

import GameState.GameState

trait Game {
  def getGameState: GameState

  def setGameState(gameState: GameState)

  def getPlayerState: PlayerState

  def setPlayerState(playerState: PlayerState)

  def getStatusMessage: String

  def setStatusMessage(status: String)

  def getField: Field

  def setField(field: Field)
}
