package aview
import controller.Controller

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.io.{ByteArrayOutputStream, StringReader}

class TUISpec extends AnyWordSpec with Matchers {
  "Text User Input" when {
    "run" should {
      "run" in {
        val controller = new Controller
        val tui = TUI(controller)
        val in = new StringReader("q")
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