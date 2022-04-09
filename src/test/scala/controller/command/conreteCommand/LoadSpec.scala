package controller.command.conreteCommand

import de.htwg.se.board.gamebase.GameImpl
import de.htwg.se.checkers.controller.command.conreteCommand.Load
import de.htwg.se.checkers.controller.controllerbase.Controller
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class LoadSpec extends AnyWordSpec with Matchers {
  "LoadTest" when {
    "loaded correctly when" should {
      val controller = new Controller(new GameImpl(8))
      val load = new Load
      "with same size" in {
        controller.createNewField(8)
        load.handleCommand(List("load"), controller) shouldBe "LOADED"
      }
    }
  }
}
