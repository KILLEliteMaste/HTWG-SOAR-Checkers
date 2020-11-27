package aview

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, File, FileOutputStream, StringReader}
import java.nio.charset.{CharsetDecoder, StandardCharsets}

import controller.Controller
import model.Field
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.io.StdIn

class TUISpec extends AnyWordSpec with Matchers {
  "Text User Input" when {
    val controller: Controller = Controller(Field(8))
    val tui = new TUI(controller)
    "Origin input is valid" should {
      "2 arguments" in {
        tui.isOriginInputValid(List("1", "2")) should be(true)
      }
      "only numbers" in {
        tui.isOriginInputValid(List("6", "4")) should be(true)
      }

    }
    "Origin input is invalid" should {
      "> 2 arguments" in {
        tui.isOriginInputValid(List("1", "2", "5", "2")) should be(false)
      }
      "not only numbers" in {
        tui.isOriginInputValid(List("5", "Hi")) should be(false)
      }
    }

    "Destination input is invalid" should {
      "not only numbers" in {
        tui.isDestinationInputValid(List("1", "Hi")) should be(false)
      }
      "not % 2 arguments" in {
        tui.isDestinationInputValid(List("1", "2", "7")) should be(false)
      }
    }
    "Destination input is valid" should {
      "only numbers" in {
        tui.isDestinationInputValid(List("1", "2", "7", "6", "2", "3")) should be(true)
      }
      "% 2 arguments" in {
        tui.isDestinationInputValid(List("1", "2", "7", "6")) should be(true)
      }
    }
    "ProcessInputLine" should {
      "be able to move" in {
        val in = new StringReader("3 2")
        Console.withIn(in) {
          tui.processInputLine("2 1")
          controller.field.matrix.cell(3, 2).value shouldBe 1
        }
      }
      "should not work because origin position is out of bounds" in {
        val out = new ByteArrayOutputStream()
        Console.withOut(out) {
          tui.processInputLine("9 9")
          out.toString.replaceAll("[^A-z ]", "") shouldBe ("The given positions are not inside the field")
        }
      }
    }
  }
}