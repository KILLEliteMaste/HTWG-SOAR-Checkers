package model.fileiocomponents.fileiojson

import controller.controllerbase.Controller
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.gamebase.GameImpl
import model.fileiocomponent.fileiojson.FileIOImpl


class FileIOImplSpec extends AnyWordSpec with Matchers {
  "A FileIO system" should {
    val fileIO = new FileIOImpl()
    "be able to save and load a game" in {
      fileIO.save(Controller(GameImpl(8)).getGame)
      fileIO.load.getField.getFieldSize shouldBe 8
    }
  }
}
