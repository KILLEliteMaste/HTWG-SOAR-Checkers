package controller.command.conreteCommand

import controller.controllerbase.Controller
import model.gamebase.GameImpl
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
