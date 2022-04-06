package model.fileiocomponents.fileioxml

import controller.controllerbase.Controller
import model.fileiocomponent.fileioxml.FileIOImpl
import model.gamebase.GameImpl
import model.{Game, GameState}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.util.{Failure, Success}
import org.scalatest.TryValues._

class FileIOImplSpec extends AnyWordSpec with Matchers {
  "A FileIO system" should {
    val fileIO = FileIOImpl()

    "be able to save a game of size 8 to a file and load it" in {
      val game: Game = new GameImpl(10)
      fileIO.save(game)
      val loaded = fileIO.load

      loaded.success.value.field.fieldSize shouldBe 10
    }
  }
}
