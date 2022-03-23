package model.fileiocomponents.fileioxml

import controller.controllerbase.Controller
import model.GameState
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.fileiocomponent.fileioxml.FileIOImpl
import model.gamebase.GameImpl

class FileIOImplSpec extends AnyWordSpec with Matchers {
  "A FileIO system" should {
    val fileIO = FileIOImpl()

    "be able to save a game of size 8 to a file and load it" in {
      fileIO.save(Controller(new GameImpl(8)).getGame)
      fileIO.load.field.fieldSize shouldBe 8
    }
  }
}
