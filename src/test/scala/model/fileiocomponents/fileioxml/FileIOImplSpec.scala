package model.fileiocomponents.fileioxml

import model.GameState
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.fileiocomponent.fileioxml.FileIOImpl
import model.gamebase.GameImpl

class FileIOImplSpec extends AnyWordSpec with Matchers {
  "A FileIO system" should {
    val fileIO = FileIOImpl()
    val testFile = scala.xml.XML.loadFile("game.xml")

    "be able to save a game of size 10 to a file and load it" in {
      val game : GameImpl = GameImpl(8)
      fileIO.save(game)
      scala.xml.XML.loadFile("game.xml") should be(testFile)
    }
  }
}
