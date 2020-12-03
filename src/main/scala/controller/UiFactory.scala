package controller

import aview.{GUI, TUI}

trait UserInterface {
  def processInputLine(input: String,controller: Controller)
}

object UserInterface {
  def apply(kind: String, controller: Controller): Unit = kind match {
    case "gui" | "GUI" => GUI(controller).run()
    case "tui" | "TUI" => TUI(controller).run()
  }
}