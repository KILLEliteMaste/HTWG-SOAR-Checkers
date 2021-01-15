package controller.command.conreteCommand

import controller.controllerbase.Controller
import model.gamebase.GameImpl
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class RedoSpec extends AnyWordSpec with Matchers {
  "Redo" when {
    "game gets redo" should {
      val controller = new Controller(new GameImpl(8))
      "fails" in {
        Redo().handleCommand(List(""), controller) shouldBe "Cannot redo"
      }
    }
  }
}
