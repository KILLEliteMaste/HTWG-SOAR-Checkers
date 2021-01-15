package model.gamebase

import model.Cell
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class FieldImplSpec extends AnyWordSpec with Matchers {
  "A Field" when {
    "constructed shouldBe given the length of its edge as size" should {
      val smallfield = FieldImpl(4)
      "have a total field size of 4" in {
        smallfield.getFieldSize shouldBe 4
      }
    }
    "filled" in {
      val field = FieldImpl(10)
      field.matrix.cell(0, 1).get.getValue shouldBe 1
      field.matrix.cell(0, 0) shouldBe None
      field.matrix.cell(1, 0).get.getValue shouldBe 1
      field.matrix.cell(1, 1) shouldBe None
    }
  }
  "made a string" in {
    val smallfield = FieldImpl(10)
    smallfield.toString shouldBe "     0   1   2   3   4   5   6   7   8   9\n0  ▐   ▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐\n1  ▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐   ▐\n2  ▐   ▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐\n3  ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐\n4  ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐\n5  ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐\n6  ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐\n7  ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐   ▐\n8  ▐   ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐\n9  ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐   ▐\n"
  }
  "A FieldMatrix" when {
    "created" should {
      val matrix = new FieldMatrixImpl[Option[Cell]](1, None)
      "have a size of 1" in {
        matrix.getSize shouldBe 1
      }
      /*matrix.replaceCell(0, 0, Some(Cell(1)))
      "have replaceable Cells" in {
        matrix.cell(0, 0).toString shouldBe("▐   ▐")
      }
      matrix.fill(Some(Cell(1)))
      "be fillable" in {
        matrix.cell(0, 0).toString shouldBe("▐   ▐")
      }
      "look like" in {
        println(matrix.toString)
        matrix.toString shouldBe "Vector(Vector(▐   ▐))"
      }*/
    }
  }
  "A big field " when {
    "built" should {
      val bigField = FieldImpl(7)
      val fieldMatrix = new FieldMatrixImpl[Option[Cell]](7, None)
      "have Cell(0)" in {
        fieldMatrix.cell(4, 0) shouldBe None
      }
    }
  }
  "A field" when {
    "have a total field size" should {
      val field = FieldImpl(8)
      "return" in {
        field.getTotalFieldSize shouldBe 64
      }
    }
    "set fieldStatistics" should {
      val field = FieldImpl(8)
      "set field statistics" in {
        field.setFieldStatistics(1, 1)
        field.getFieldStatistics(1) shouldBe 1
      }
    }
  }
}
