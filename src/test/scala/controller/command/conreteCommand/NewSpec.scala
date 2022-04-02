package controller.command.conreteCommand

import controller.controllerbase.Controller
import model.gamebase.GameImpl
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class NewSpec extends AnyWordSpec with Matchers {
  "NewTest" when {
    "create new field" should {
      val controller = Controller(new GameImpl(8))
      val new0 = new New
      "with same size" in {
        controller.createNewField(8)
        new0.handleCommand(List(), controller)
        controller.game.field.fieldSize shouldBe 8
      }
      "with different size 10" in {
        controller.createNewField(10)
        new0.handleCommand(List("10"), controller)
        controller.game.field.fieldSize shouldBe 10
      }
      "with different size 12" in {
        controller.createNewField(12)
        new0.handleCommand(List("12"), controller)
        controller.game.field.fieldSize shouldBe 12
      }
    }
  }
}

