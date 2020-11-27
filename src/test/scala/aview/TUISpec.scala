package aview

import controller.Controller
import model.{Field, Position}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.io.{ByteArrayOutputStream, StringReader}

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
      "be able to jump twice" in {
        controller.createNewField(8)
        controller.moveFromPositionToPosition(Position(2, 5), Position(3, 4), 1, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(5, 0), Position(4, 1), 3, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(3, 4), Position(4, 3), 1, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(6, 1), Position(5, 0), 3, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(1, 6), Position(2, 5), 1, alreadyMoved = false)
        controller.changePlayerTurn()
        val in = new StringReader("3 4 1 6")
        Console.withIn(in) {
          tui.processInputLine("5 2")
          controller.field.matrix.cell(1, 6).value shouldBe 3
        }
      }
      "should not work because origin position is out of bounds" in {
        val out = new ByteArrayOutputStream()
        Console.withOut(out) {
          tui.processInputLine("9 9")
          out.toString.replaceAll("\r\n", "") shouldEqual "The given positions are not inside the field"
        }
      }
      "should not work because destination cell is not empty" in {
        controller.createNewField()
        val in = new StringReader("2 1")
        val out = new ByteArrayOutputStream()
        Console.withIn(in) {
          Console.withOut(out) {
            tui.processInputLine("0 1")
            val array = out.toString().split("\r?\n")
            array(array.size - 1) shouldEqual "One destination is not empty to be able to move to this position"
          }
        }
      }
      "should not work because position not inside of field" in {
        controller.createNewField()
        val in = new StringReader("3 8")
        val out = new ByteArrayOutputStream()
        Console.withIn(in) {
          Console.withOut(out) {
            tui.processInputLine("2 7")
            val array = out.toString().split("\r?\n")
            array(array.size - 1) shouldEqual "The given positions are not inside the field"
          }
        }
      }
      "should not work because cell not belongs to player" in {
        controller.createNewField()
        val in = new StringReader("4 1")
        val out = new ByteArrayOutputStream()
        Console.withIn(in) {
          Console.withOut(out) {
            tui.processInputLine("5 0")
            val array = out.toString().split("\r?\n")
            array(array.size - 1) shouldEqual "Cell does not contain a stone that belongs to Player 1"
          }
        }
      }
      "should not work to move straight" in {
        controller.createNewField()
        val in = new StringReader("3 1")
        val out = new ByteArrayOutputStream()
        Console.withIn(in) {
          Console.withOut(out) {
            tui.processInputLine("2 1")
            controller.player should be(1)
          }
        }
      }
    }
  }
}