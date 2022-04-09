package util

import de.htwg.se.board.gamebase.GameImpl
import de.htwg.se.checkers.controller.controllerbase.Controller
import de.htwg.se.checkers.util.Observer
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ObserverSpec extends AnyWordSpec with Matchers {
  "A Controller" when {
    "observed by observer" should {
      val controller = Controller(new GameImpl(8))
      val observer: Observer = new Observer {
        var updated: Boolean = false

        override def update(): Unit = updated = true
      }
      controller.add(observer)
      "notify observers after changing turns" in {
        controller.changePlayerTurn()
        observer.update() shouldBe ()
      }
      "remove an observer" in {
        controller.remove(observer)
        controller.subscribers should not contain observer
      }
    }
  }
}
