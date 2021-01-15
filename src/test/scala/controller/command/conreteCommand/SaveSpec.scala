package controller.command.conreteCommand

import controller.controllerbase.Controller
import model.gamebase.GameImpl
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class SaveSpec extends AnyWordSpec with Matchers {
  "NewTest" when {
    "saved correctly when" should {
      val controller = new Controller(new GameImpl(8))
      val save = new Save
      "with same size" in {
        controller.createNewField(8)
        save.handleCommand(List("save"), controller) shouldBe "SAVED"
      }
    }
  }
}
