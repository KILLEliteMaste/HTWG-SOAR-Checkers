package controller.command.conreteCommand

import controller.Controller
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class RedoSpec extends AnyWordSpec with Matchers {
  "Redo" when {
    "game gets redo" should {
      val controller = new Controller
      "fails" in {
        Redo().handleCommand(List(""), controller) should be("Cannot redo")
      }
    }
  }
}
