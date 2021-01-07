package controller.command.conreteCommand

import controller.controllerbase.Controller
import model.fieldbase.FieldImpl
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class NewSpec extends AnyWordSpec with Matchers {
  "NewTest" when {
    "create new field" should {

      val controller = new Controller(FieldImpl(8))
      val new0 = new New
      "with same size" in {
        controller.createNewField(8)
        new0.handleCommand(List(),controller)
        controller.field.getFieldSize shouldBe 8
      }
      "with different size" in {
        controller.createNewField(10)
        new0.handleCommand(List("10"),controller)
        controller.field.getFieldSize shouldBe 10
      }
    }
  }
}
