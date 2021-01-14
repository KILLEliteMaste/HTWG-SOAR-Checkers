package model.fileiocomponents.fileiojson

import controller.controllerbase.Controller
import model.GameState
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.gamebase.GameImpl
import model.fileiocomponent.fileiojson.FileIOImpl
import play.api.libs.json.{JsValue, Json}

import scala.io.{BufferedSource, Source}

class FileIOImplSpec extends AnyWordSpec with Matchers{
  "A FileIO system" should {
    val fileIO = new FileIOImpl()
    "be able to save a game to a file" in {
      fileIO.save(Controller(GameImpl(8)).getGame)
      val source: BufferedSource = Source.fromFile("game.json")
      val sourceString : String = source.getLines().mkString
      val json : JsValue = Json.parse(sourceString)
      json should be(json)
    }
    "be able to load a game from a file" in {
      fileIO.load.getField.getFieldSize shouldBe 8
    }
  }
}
