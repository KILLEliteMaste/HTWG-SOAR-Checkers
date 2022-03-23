package model

import model.PlayerState

trait Game(val size: 8 | 10 | 12, val playerState: PlayerState, val statusMessage: String, val field: Field, val gameState: GameState) {

  def recreate(size: 8 | 10 | 12 = 8, playerState: PlayerState = playerState, statusMessage: String = statusMessage, field: Field = field, gameState: GameState = gameState): Game
}
