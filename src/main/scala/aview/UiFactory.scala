package aview

import controller.Controller

trait UserInterface {
  def processInputLine(input: String,controller: Controller)
}

object UserInterface {
  def apply(kind: String, controller: Controller): Unit = kind match {
    case "gui" | "GUI" => Gui(controller).run()
    case "tui" | "TUI" => Tui(controller).run()
    case _ => throw new Exception("No valid user interface")
  }
}