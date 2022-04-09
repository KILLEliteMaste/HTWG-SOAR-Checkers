package controller.command.conreteCommand

import de.htwg.se.board.gamebase.GameImpl
import de.htwg.se.checkers.controller.command.conreteCommand.Redo
import de.htwg.se.checkers.controller.controllerbase.Controller
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
