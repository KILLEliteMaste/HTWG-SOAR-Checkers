package Field

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class FieldTest extends AnyWordSpec with Matchers {
  "A Field" when {
    "constructed should be given the length of its edge as size" should {
      val smallfield = Field(2)
      "have a total field size of 4" in {
        smallfield.totalFieldSize should be(4)
      }
    }
    "filled" in {
      val field = Field(10)

      field.matrix.cell(0, 1).value should be(0)
      field.matrix.cell(0, 0).value should be(1)
      field.matrix.cell(1, 0).value should be(0)
      field.matrix.cell(1, 1).value should be(1)
    }
  }
  "made a string" in {
    val smallfield = Field(10)
    smallfield.toString shouldBe "▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐   ▐\n▐   ▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐\n▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐   ▐\n▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐\n▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐\n▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐\n▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐\n▐   ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐\n▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐   ▐\n▐   ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐\n"
  }
  "A FieldMatrix" when {
    "created" should {
      val matrix = new FieldMatrix[Cell](1, Cell(0))
      "have a size of 1" in {
        matrix.size should be(1)
      }
      matrix.replaceCell(0, 0, Cell(1))
      "have replaceable Cells" in {
        matrix.cell(0, 0).toString should be("▐   ▐")
      }
      matrix.fill(Cell(1))
      "be fillable" in {
        matrix.cell(0, 0).toString should be("▐   ▐")
      }
      "look like" in {
        println(matrix.toString)
        matrix.toString shouldBe "Vector(Vector(▐   ▐))"
      }
    }
  }
  "A big field " when {
    "built" should {
      val bigField = Field(7)
      val fieldMatrix = new FieldMatrix[Cell](7, Cell(0))
      "have Cell(0)" in {
        fieldMatrix.cell(4, 0).toString should be("▐   ▐")
      }
    }
  }
}
