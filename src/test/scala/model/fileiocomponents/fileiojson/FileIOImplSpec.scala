package model.fileiocomponents.fileiojson

import controller.controllerbase.Controller
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.gamebase.GameImpl
import model.fileiocomponent.fileiojson.FileIOImpl

import scala.util.{Failure, Success}
import org.scalatest.TryValues._

class FileIOImplSpec extends AnyWordSpec with Matchers {
  "A FileIO system" should {
    val fileIO = FileIOImpl()
    "be able to save and load a game" in {
      val game = new GameImpl(10)
      fileIO.save(game)
      val loaded = fileIO.load

      loaded.success.value.field.fieldSize shouldBe 10
    }
  }
}
