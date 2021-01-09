package util

import controller.controllerbase.Controller
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.fieldbase.GameImpl

class UndoManagerSpec extends AnyWordSpec with Matchers {
  "An UndoManager" when {

    val undoManager = UndoManager(new Controller(GameImpl(8)))
    val controller = new Controller(GameImpl(8))
    "have a do, undo and redo" should {
      "do" in {

      }

      "undo" in {

      }

      "redo" in {

      }

      "OldController" in {
        val oldController = undoManager.OldController(controller.game.getField.copyField, controller.game.getField.getFieldMatrix.copyFieldMatrix, controller.game.getPlayerState, controller.game.getGameState, controller.game.getStatusMessage)
        oldController.playerState shouldBe controller.game.getPlayerState
      }
    }
  }
}
