package aview

import controller.controllerbase.Controller
import model.fieldbase.FieldImpl
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.io.{ByteArrayOutputStream, StringReader}

class TuiSpec extends AnyWordSpec with Matchers {
  "Text User Input" when {
    "run" should {
      "run" in {
        val controller = new Controller(FieldImpl(8))
        val tui = Tui(controller)
        val in = new StringReader("quit")
        val out = new ByteArrayOutputStream()
        Console.withIn(in) {
          Console.withOut(out) {
            tui.run()
            val array = out.toString().split("\r?\n")
            array(array.size - 1) shouldEqual "Game ended"
          }
        }
      }
    }
  }
}