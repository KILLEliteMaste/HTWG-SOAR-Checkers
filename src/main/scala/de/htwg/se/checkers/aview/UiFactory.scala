package de.htwg.se.checkers.aview

import de.htwg.se.checkers.aview.gui.Gui
import de.htwg.se.checkers.controller.ControllerInterface
import de.htwg.se.checkers.controller.controllerbase.Controller
import javafx.embed.swing.JFXPanel

import java.util.concurrent.ExecutorService
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

trait UserInterface {
  def processInputLine(input: String, controller: ControllerInterface): String
}

object UserInterface {
  def apply(kind: String, controller: ControllerInterface): Unit = {
    if (kind.contains("tui")) {
      if (kind.contains("rui") || kind.contains("gui")) {
        ExecutionContext.global.execute(() => {
          Tui(controller).run()
        })
      } else {
        Tui(controller).run()
      }
    }

    if (kind.contains("rui")) {
      if (kind.contains("gui")) {
        ExecutionContext.global.execute(() => {
          RestTui(controller).run()
        })
      } else {
        RestTui(controller).run()
      }
    }

    if (kind.contains("gui")) {
      //Workaround for ScalaFX toolkit initialization. This fix is broadly used among other users.
      new JFXPanel()
      Gui(controller).run()
    }
  }
}