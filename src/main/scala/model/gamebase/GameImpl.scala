package model.gamebase

import com.google.inject.name.Named
import model.GameState.GameState
import model.{Field, Game, GameState, PlayerState, PlayerState1}

import javax.inject.Inject

case class GameImpl @Inject()(@Named("DefaultSize") size: Int) extends Game {
  var gameState: GameState.Value = GameState.IDLE
  var statusMessage: String = ""
  var playerState: PlayerState = new PlayerState1
  var field: Field = FieldImpl(size)

  override def getGameState: GameState = gameState

  override def getPlayerState: PlayerState = playerState

  override def getStatusMessage: String = statusMessage

  override def getField: Field = field

  override def setGameState(gameState: GameState): Unit = this.gameState = gameState

  override def setPlayerState(playerState: PlayerState): Unit = this.playerState = playerState

  override def setStatusMessage(status: String): Unit = statusMessage = status

  override def setField(field: Field): Unit = this.field = field
}
