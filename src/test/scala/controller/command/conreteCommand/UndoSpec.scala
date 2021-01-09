package controller.command.conreteCommand

import controller.controllerbase.Controller
import model.fieldbase.GameImpl
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class UndoSpec extends AnyWordSpec with Matchers {
  "Undo" when {
    "gets undone" should {
      "undo unsuccessful" in {
        val controller = new Controller(new GameImpl(8))
        Undo().handleCommand(List(""), controller) shouldBe "Cannot undo"
      }
    }
  }
}
