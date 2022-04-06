package model.fileiocomponents.fileiojson

import controller.controllerbase.Controller
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import model.gamebase.GameImpl
import model.fileiocomponent.fileiojson.FileIOImpl

import scala.util.{Failure, Success}


class FileIOImplSpec extends AnyWordSpec with Matchers {
  "A FileIO system" should {
    val fileIO = FileIOImpl()
    "be able to save and load a game" in {
      fileIO.save(new GameImpl(10))
      
      fileIO.load match {
        case Success(value) => value.field.fieldSize shouldBe 10
        case Failure(exception) =>
      }
    }
  }
}
