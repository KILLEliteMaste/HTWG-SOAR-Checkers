package controller

import model.{Field, Position}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class ControllerSpec extends AnyWordSpec with Matchers {
  "Controller" when {
    val controller: Controller = Controller(Field(8))
    "a new Field get created" should {
      "without int" in {
        controller.createNewField()
        controller.field.fieldSize should be(8)
      }
      "with int" in {
        controller.createNewField(12)
        controller.field.fieldSize should be(12)
      }
    }
    "changing player" should {
      "from p1 to p2" in {
        println(controller.player + "X")
        controller.changePlayerTurn()
        controller.player should be(2)

      }
      "from p2 to p1" in {
        println(controller.player + "Y")
        controller.changePlayerTurn()
        controller.player should be(1)
      }
    }
    "moving piece" should {
      "from 2 1 to 3 0" in {
        println(controller.player + "Z")
        controller.moveFromPositionToPosition(Position(2, 1), Position(3, 0), 1, alreadyMoved = false)
        controller.field.matrix.cell(3, 0).value should be(1)
      }

      "from 5 0 to 4 1" in {
        controller.changePlayerTurn()
        controller.createNewField(8)
        println(controller.field.toString)
        println(controller.player + "C")
        controller.moveFromPositionToPosition(Position(5, 0), Position(4, 1), 3, alreadyMoved = false)
        controller.field.matrix.cell(4, 1).value should be(3)
      }
    }
  }
}
