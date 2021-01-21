package util

import controller.controllerbase.Controller
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.gamebase.GameImpl

class UndoManagerSpec extends AnyWordSpec with Matchers {
  "An UndoManager" when {

    val undoManager = UndoManager(new Controller(GameImpl(8)))
    val controller = new Controller(GameImpl(8))
    "have a do, undo and redo" should {
      "OldController" in {
        val oldController = undoManager.OldController(controller.game.getField.copyField, controller.getGame.getField.getFieldStatistics(1),
          controller.getGame.getField.getFieldStatistics(2),
          controller.getGame.getField.getFieldStatistics(3),
          controller.getGame.getField.getFieldStatistics(4), controller.game.getField.getFieldMatrix.copyFieldMatrix, controller.game.getPlayerState, controller.game.getGameState, controller.game.getStatusMessage)
        oldController.playerState shouldBe controller.game.getPlayerState
      }
    }
  }
}
