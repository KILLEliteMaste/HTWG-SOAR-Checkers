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
      val smallfield = Field(2)

      smallfield.matrix.cell(0, 1).toString should be("▐ 1 ▐")
      smallfield.matrix.cell(0, 0).toString should be("▐   ▐")
      smallfield.matrix.cell(1, 0).toString should be("▐ 1 ▐")
      smallfield.matrix.cell(1, 1).toString should be("▐   ▐")
    }
  }
  "made a string" in {
    val smallfield = Field(2)

    smallfield.toString should be("▐   ▐ 1 ▐\n▐ 1 ▐   ▐\n")
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
    }
  }
  "A big field " when {
    "built" should {
      val bigField= Field(7)
      val fieldMatrix = new FieldMatrix[Cell](7, Cell(0))
      "have Cell(0)" in {
        fieldMatrix.cell(4, 0).toString should be("▐   ▐")
      }
    }
  }
}
