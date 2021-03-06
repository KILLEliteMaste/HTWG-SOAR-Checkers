package de.htwg.se.board

trait Game(val size: 8 | 10 | 12, val playerState: PlayerState, val statusMessage: String, val field: Field, val gameState: GameState):
  def recreate(size: 8 | 10 | 12 = size, playerState: PlayerState = playerState, statusMessage: String = statusMessage, field: Field = field, gameState: GameState = gameState): Game
