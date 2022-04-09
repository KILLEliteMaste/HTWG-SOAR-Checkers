package de.htwg.se.board

trait PlayerState {
  def handle(): PlayerState
}

class PlayerState1() extends PlayerState {
  override def handle(): PlayerState = {
    new PlayerState2()
  }

  override def toString = "It's Player 1 turn"
}

class PlayerState2 extends PlayerState {
  override def handle(): PlayerState = {
    new PlayerState1()
  }

  override def toString = "It's Player 2 turn"
}