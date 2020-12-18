package controller.command.conreteCommand

import controller.controllerbase.Controller
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import util.Position

class MoveSpec extends AnyWordSpec with Matchers {

  "MoveTest" when {
    val move = Move()
    "Origin input is valid" should {
      "4 arguments" in {
        move.isOriginInputValid(List("2", "1","3","0")) shouldBe true
      }
    }
    "Origin input is invalid" should {
      "not only numbers" in {
        move.isOriginInputValid(List("5", "Hi")) shouldBe false
      }
    }
    "Destination input is invalid" should {
      "not only numbers" in {
        move.isDestinationInputValid(List("1", "Hi")) shouldBe false
      }
      "not % 2 arguments" in {
        move.isDestinationInputValid(List("1", "2", "7")) shouldBe false
      }
    }
    "Destination input is valid" should {
      "only numbers" in {
        move.isDestinationInputValid(List("1", "2", "7", "6", "2", "3")) shouldBe true
      }
      "% 2 arguments" in {
        move.isDestinationInputValid(List("1", "2", "7", "6")) shouldBe true
      }
    }

    "ProcessInputLine" should {
      val controller = new Controller
      "be able to move" in {
        controller.createNewField()
        move.handleCommand(List("2", "1", "3", "2"), controller)
        controller.field.getFieldMatrix.cell(3, 2).get.getValue shouldBe 1
      }
      "be able to jump twice" in {
        controller.createNewField()
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
        move.handleCommand(List("5", "2", "3", "4", "1", "6"), controller)
        controller.field.getFieldMatrix.cell(1, 6).get.getValue shouldBe 3
      }
      "should not work because origin position is out of bounds" in {
        controller.createNewField()
        val ret = move.handleCommand(List("9", "9","9","9"), controller)
        ret shouldEqual "The given positions are not inside the field"
      }
      "should not work because destination cell is not empty" in {
        controller.createNewField()
        val ret = move.handleCommand(List("0", "1", "2", "1"), controller)
        ret shouldEqual "One destination is not empty to be able to move to this position"
      }
      "should not work because position not inside of field" in {
        controller.createNewField()
        val ret = move.handleCommand(List("2", "7", "3", "8"), controller)
        ret shouldEqual "The given positions are not inside the field"
      }
      "should not work because cell not belongs to player" in {
        controller.createNewField()
        val ret = move.handleCommand(List("5", "0", "4", "1"), controller)
        ret shouldEqual "Cell does not contain a stone that belongs to Player 1"
      }
      "should not work to move straight" in {
        controller.createNewField()
        move.handleCommand(List("2", "1", "3", "1"), controller)
        controller.playerState.toString shouldBe "It's Player 1 turn"
      }
    }
  }
}
