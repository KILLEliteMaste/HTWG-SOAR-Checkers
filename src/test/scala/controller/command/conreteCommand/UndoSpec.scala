package controller.command.conreteCommand

import controller.Controller
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class UndoSpec extends AnyWordSpec with Matchers {
  "Undo" when {
    "gets undone" should {
      "undo unsuccessful" in {
        val controller = new Controller
        Undo().handleCommand(List(""), controller) should be("Cannot undo")
      }
    }
  }
}

