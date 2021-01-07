package aview

import aview.gui.Gui
import controller.controllerbase.Controller

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

trait UserInterface {
  def processInputLine(input: String, controller: Controller): String
}

object UserInterface {
  def apply(kind: String, controller: Controller): Unit = kind match {
    case "gui" | "GUI" => Gui(controller).run()
    case "tui" | "TUI" => Tui(controller).run()
    case "both" | "BOTH" =>
      ExecutionContext.global.execute(() => {
        Try(UserInterface("tui", controller)) match {
          case Failure(v) => println("Could not start UI because: " + v.getMessage + v.printStackTrace())
        }
      })
      Try(UserInterface("gui", controller)) match {
        case Failure(v) => println("Could not start UI because: " + v.getMessage + v.printStackTrace())
        case Success(v) => println("GOOD BYE")
      }
    case _ => throw new Exception("No valid user interface")
  }
}