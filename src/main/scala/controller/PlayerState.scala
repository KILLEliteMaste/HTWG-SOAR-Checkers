package controller

import controller.controllerbase.Controller

trait PlayerState {
  def handle(controller: Controller)
}

class PlayerState1() extends PlayerState {
  override def handle(controller: Controller): Unit = {
    controller.playerState = new PlayerState2
  }

  override def toString = s"It's Player 1 turn"
}

class PlayerState2 extends PlayerState {
  override def handle(controller: Controller): Unit = {
    controller.playerState = new PlayerState1
  }

  override def toString = s"It's Player 2 turn"
}