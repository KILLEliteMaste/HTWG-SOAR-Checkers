package controller

import model.Position
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class ControllerSpec extends AnyWordSpec with Matchers {
  "Controller" when {
    val controller = new Controller
    "a new Field get created" should {
      "without int" in {
        controller.createNewField(8)
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
        controller.changePlayerTurn()
        controller.playerState.toString should be("It's Player 2 turn")
      }
      "from p2 to p1" in {
        controller.changePlayerTurn()
        controller.playerState.toString should be ("It's Player 1 turn")
      }
    }
    "moving piece" should {
      "from 2 1 to 3 0" in {
        controller.createNewField(8)
        controller.moveFromPositionToPosition(Position(2, 1), Position(3, 0), 1, alreadyMoved = false)
        controller.field.matrix.cell(3, 0).value should be(1)
      }
      "from 5 0 to 4 1" in {
        controller.createNewField(8)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(5, 0), Position(4, 1), 3, alreadyMoved = false)
        controller.field.matrix.cell(4, 1).value should be(3)
      }

      "from 2 3 to 3 4" in {
        controller.createNewField(8)
        controller.moveFromPositionToPosition(Position(2, 3), Position(3, 4), 1, alreadyMoved = false)
        controller.field.matrix.cell(3, 4).value should be(1)
      }

      "double jump from upper left to under right(white)" in {
        controller.createNewField(8)
        controller.moveFromPositionToPosition(Position(2, 5), Position(3, 4), 1, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(5, 4), Position(4, 3), 3, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(2, 7), Position(3, 6), 1, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(6, 5), Position(5, 4), 3, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(1, 6), Position(2, 7), 1, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(4, 3), Position(3, 2), 3, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(2, 1), Position(4, 3), 1, alreadyMoved = false)
        controller.moveFromPositionToPosition(Position(4, 3), Position(6, 5), 1, alreadyMoved = true)
        controller.field.matrix.cell(6, 5).value should be(1)
      }

      "double jump from under right to upper left(black)" in {
        controller.createNewField(8)
        controller.moveFromPositionToPosition(Position(2, 1), Position(3, 2), 1, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(5, 6), Position(4, 7), 3, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(3, 2), Position(4, 3), 1, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(4, 7), Position(3, 6), 3, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(1, 0), Position(2, 1), 1, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(5, 4), Position(3, 2), 3, alreadyMoved = false)
        controller.moveFromPositionToPosition(Position(3, 2), Position(1, 0), 3, alreadyMoved = true)
        controller.changePlayerTurn()
        controller.field.matrix.cell(1, 0).value should be(3)
      }

      "double jump from under left to upper right(black)" in {
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
        controller.moveFromPositionToPosition(Position(5, 2), Position(3, 4), 3, alreadyMoved = false)
        controller.moveFromPositionToPosition(Position(3, 4), Position(1, 6), 3, alreadyMoved = true)
        controller.changePlayerTurn()
        controller.field.matrix.cell(1, 6).value should be(3)
      }
      "upgrade to king(white)" in {
        controller.createNewField(8)
        controller.moveFromPositionToPosition(Position(2, 5), Position(3, 4), 1, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(5, 4), Position(4, 3), 3, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(2, 7), Position(3, 6), 1, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(6, 5), Position(5, 4), 3, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(1, 6), Position(2, 7), 1, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(4, 3), Position(3, 2), 3, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(2, 1), Position(4, 3), 1, alreadyMoved = false)
        controller.moveFromPositionToPosition(Position(4, 3), Position(6, 5), 1, alreadyMoved = true)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(6, 3), Position(5, 4), 3, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(2, 3), Position(3, 2), 1, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(7, 4), Position(6, 3), 3, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(6, 5), Position(7, 4), 1, alreadyMoved = false)
        controller.field.matrix.cell(7, 4).value shouldBe 2
      }
      "upgrade to king(black)" in {
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
        controller.moveFromPositionToPosition(Position(5, 2), Position(3, 4), 3, alreadyMoved = false)
        controller.moveFromPositionToPosition(Position(3, 4), Position(1, 6), 3, alreadyMoved = true)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(1, 4), Position(2, 5), 1, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(5, 4), Position(4, 5), 3, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(0, 5), Position(1, 4), 1, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(1, 6), Position(0, 5), 3, alreadyMoved = false)
        //println("++++++++++++++++++++++++++++++++++++++++++++\n"+controller.matrixToString)
        controller.field.matrix.cell(0, 5).value shouldBe 4
      }
      //geht noch nicht
      "double jump with king from upper right to under left" in {
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
        controller.moveFromPositionToPosition(Position(5, 2), Position(3, 4), 3, alreadyMoved = false)
        controller.moveFromPositionToPosition(Position(3, 4), Position(1, 6), 3, alreadyMoved = true)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(1, 4), Position(2, 5), 1, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(5, 4), Position(4, 5), 3, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(0, 5), Position(1, 4), 1, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(1, 6), Position(0, 5), 3, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(2, 3), Position(3, 2), 1, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(4, 1), Position(3, 0), 3, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(2, 7), Position(3, 6), 1, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(0, 5), Position(2, 3), 4, alreadyMoved = false)
        controller.moveFromPositionToPosition(Position(2, 3), Position(4, 1), 4, alreadyMoved = true)
        controller.field.matrix.cell(4, 1).value should be(4)
      }
      "move more then 1 cell with king" in {
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
        controller.moveFromPositionToPosition(Position(5, 2), Position(3, 4), 3, alreadyMoved = false)
        controller.moveFromPositionToPosition(Position(3, 4), Position(1, 6), 3, alreadyMoved = true)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(1, 4), Position(2, 5), 1, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(5, 4), Position(4, 5), 3, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(0, 5), Position(1, 4), 1, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(1, 6), Position(0, 5), 3, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(2, 7), Position(3, 6), 1, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(0, 5), Position(2, 7), 4, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(2, 5), Position(3, 4), 1, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(2, 7), Position(1, 6), 4, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(3, 4), Position(4, 3), 1, alreadyMoved = false)
        controller.changePlayerTurn()
        controller.moveFromPositionToPosition(Position(1, 6), Position(5, 2), 4, alreadyMoved = false)
        //println(controller.field)
        controller.field.matrix.cell(5, 2).value should be(4)
      }
    }
    "positions in vector are" should {
      "in bounds" in {
        controller.checkIfAllPositionsAreInBounds(Vector(Position(2, 1), Position(3, 0), Position(5, 4)), controller.field) should be(true)
      }

      "not in bounds" in {
        controller.checkIfAllPositionsAreInBounds(Vector(Position(10, 1), Position(-2, 0), Position(5, 4)),
          controller.field) should be(false)
      }
    }
    "checking all cells empty" should {
      "empty" in {
        controller.createNewField()
        controller.checkIfAllCellsAreEmpty(controller.field, Vector(Position(0, 0), Position(6, 0),
          Position(3, 1))) should be(true)
      }
      "not empty" in {
        controller.createNewField()
        controller.checkIfAllCellsAreEmpty(controller.field, Vector(Position(1, 0), Position(2, 1),
          Position(3, 1))) should be(false)
      }
    }
    "checking all cells belongs to player" should {
      "belongs to player" in {
        controller.createNewField()
        controller.checkIfAllCellsBelongToPlayer(controller.field, Vector(Position(2, 1), Position(1, 4))) should be(true)
      }
      "dont belongs to player 1" in {
        controller.createNewField()
        controller.checkIfAllCellsBelongToPlayer(controller.field, Vector(Position(5, 0), Position(5, 4))) should be(false)
      }
      "dont belongs to player 2" in {
        controller.createNewField()
        controller.changePlayerTurn()
        controller.checkIfAllCellsBelongToPlayer(controller.field, Vector(Position(2, 1), Position(1, 4))) should be(false)
      }
    }
  }
}
