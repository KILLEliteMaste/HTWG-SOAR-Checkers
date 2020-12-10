package controller.command.conreteCommand

import controller.Controller
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class NewTest extends AnyWordSpec with Matchers {
  "NewTest" when {
    "create new field" should {
      val controller = new Controller
      val new0 = new New
      "with same size" in {
        controller.createNewField(8)
        new0.handleCommand(List(),controller)
        controller.field.fieldSize shouldBe 8
      }
      "with different size" in {
        controller.createNewField(10)
        new0.handleCommand(List("10"),controller)
        controller.field.fieldSize shouldBe 10
      }
    }
  }
}
