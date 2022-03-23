package util

import controller.controllerbase.Controller
import model.gamebase.GameImpl
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class UndoManagerSpec extends AnyWordSpec with Matchers {
  "An UndoManager" when {

    val undoManager = UndoManager(Controller(new GameImpl(8)))
    val controller = Controller(new GameImpl(8))
    "have a do, undo and redo" should {
      "OldController" in {
        val oldController = undoManager.OldController(controller.game.field.copyField, controller.game.field.fieldStatistics.get(1).get,
          controller.game.field.fieldStatistics.get(2).get,
          controller.game.field.fieldStatistics.get(3).get,
          controller.game.field.fieldStatistics.get(4).get, controller.game.field.fieldMatrix.copyFieldMatrix, controller.game.playerState, controller.game.gameState, controller.game.statusMessage)
        oldController.playerState shouldBe controller.game.playerState
      }
    }
  }
}
