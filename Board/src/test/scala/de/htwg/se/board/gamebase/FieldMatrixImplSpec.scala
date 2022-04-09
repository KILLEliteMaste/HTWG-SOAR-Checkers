package de.htwg.se.board.gamebase

import de.htwg.se.board.Cell
import de.htwg.se.board.gamebase.FieldMatrixImpl
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class FieldMatrixImplSpec extends AnyWordSpec with Matchers {
  "A FieldMatrixImpl" when {
    "create new FieldMatrix" should {
      val fieldmatrix = new FieldMatrixImpl[Int](Vector(Vector(1,2,3),Vector(4,5,6)))
      "new FieldMatrix" in{
        val x = fieldmatrix.createNewFieldMatrix(Vector(Vector(1,2,3),Vector(4,5,6)))
        x.rows shouldBe (Vector(Vector(1,2,3),Vector(4,5,6)))
      }
    }
  }
}
