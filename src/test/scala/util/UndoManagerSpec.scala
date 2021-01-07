package util

import controller.controllerbase
import controller.controllerbase.Controller
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.fieldbase.FieldImpl

class UndoManagerSpec extends AnyWordSpec with Matchers {
  "An UndoManager" when {

    val undoManager = UndoManager(new Controller(FieldImpl(8)))
    val controller = new Controller(FieldImpl(8))
    "have a do, undo and redo" should {
      "do" in {

      }

      "undo" in {

      }

      "redo" in {

      }

      "OldController" in {
        val oldController = undoManager.OldController(controller.field.copyField, controller.field.getFieldMatrix.copyFieldMatrix, controller.playerState, controller.gameState, controller.statusMessage)
        oldController.playerState shouldBe controller.playerState
      }
    }
  }
}
