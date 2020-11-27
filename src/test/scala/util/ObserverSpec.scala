package util

import controller.Controller
import model._
import util._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ObserverSpec extends AnyWordSpec with Matchers {
  "A Controller" when {
    "observed by observer" should {
      val field = new Field(8)
      val controller = new Controller(field)
      val observer = new Observer {
        var updated: Boolean = false
        override def update():Unit = updated = true
      }
      controller.add(observer)
      "notify observers after changing turns" in {
        controller.changePlayerTurn()
        observer.updated should be (true)
      }
      "remove an observer" in {
        controller.remove(observer)
        controller.subscribers should not contain(observer)
      }
    }
  }
}
