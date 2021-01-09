package model.fieldbase

import model.Cell
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class FieldMatrixImplSpec extends AnyWordSpec with Matchers {
  "A FieldMatrixImpl" when {
    "create new FieldMatrix" should {
      val fieldmatrix = new FieldMatrixImpl[Int]()
      "new FieldMatrix" in{
        val x = fieldmatrix.createNewFieldMatrix(Vector(Vector(1,2,3),Vector(4,5,6)))
        x.getRows shouldBe (Vector(Vector(1,2,3),Vector(4,5,6)))
      }
    }
  }
}
