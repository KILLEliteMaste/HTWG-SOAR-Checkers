package aview

import controller.Controller
import model.Field
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

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

  }
}