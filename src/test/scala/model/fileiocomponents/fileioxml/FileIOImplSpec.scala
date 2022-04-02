package model.fileiocomponents.fileioxml

import controller.controllerbase.Controller
import model.fileiocomponent.fileioxml.FileIOImpl
import model.gamebase.GameImpl
import model.{Game, GameState}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class FileIOImplSpec extends AnyWordSpec with Matchers {
  "A FileIO system" should {
    val fileIO = FileIOImpl()

    "be able to save a game of size 8 to a file and load it" in {
      val game: Game = new GameImpl(8)
      fileIO.save(Controller(game.recreate()).game)
      fileIO.load.field.fieldSize shouldBe 8
    }
  }
}
