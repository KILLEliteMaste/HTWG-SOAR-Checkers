package de.htwg.se.fileio.fileioxml

import de.htwg.se.board.gamebase.GameImpl
import de.htwg.se.board.Game
import de.htwg.se.fileio.fileioxml.FileIOImpl
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.board.*
import scala.util.{Failure, Success}
import org.scalatest.TryValues.*

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
