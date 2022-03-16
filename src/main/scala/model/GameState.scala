package model

enum GameState:
  case IDLE, RUNNING, DRAW, P1_WON, P2_WON, LOADED, SAVED

  val map: Map[GameState, String] = Map[GameState, String](
    IDLE -> "Currently no active game",
    RUNNING -> "Game is currently running",
    DRAW -> "Draw!",
    P1_WON -> "Player 1 won the game!",
    P2_WON -> "Player 2 won the game!",
    LOADED -> "Game has been loaded!",
    SAVED -> "Game has been saved!"
  )

  def message(gameState: GameState): String = {
    map(gameState)
  }
