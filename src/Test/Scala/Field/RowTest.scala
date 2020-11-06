package Field

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class RowTest extends AnyWordSpec with Matchers {
  "A Row" when {
    "it consists of 10 cells with all types of stones" should {
      var row = Row[Cell](Vector[Cell](Cell(0), Cell(1), Cell(2), Cell(3), Cell(4), Cell(1), Cell(0), Cell(1), Cell(0), Cell(1)))

      "has the size of 10" in {
        println(row.size shouldBe 10)
      }
      "has a cell with a white men" in {
        println(row.cell(1).value shouldBe 1)
      }
      "has a cell with a white king" in {
        println(row.cell(2).value shouldBe 2)
      }
      "has a cell with a black men" in {
        println(row.cell(3).value shouldBe 3)
      }
      "has a cell with a black king" in {
        println(row.cell(4).value shouldBe 4)
      }
      "should look like this" in {
        row.toString shouldBe "▐   ▐▐ 1 ▐▐ 2 ▐▐ 3 ▐▐ 4 ▐▐ 1 ▐▐   ▐▐ 1 ▐▐   ▐▐ 1 ▐"
      }
      "a cell can be replaced with another cell" in {
        row = row.replaceCell(9, Cell(4))
        row.cell(9).value shouldBe 4
      }
      "a row can be filled with a type of 1  cells" in {
        row = row.fill(Cell(2))
        for (elem <- row.spots) {
          elem.value shouldBe 2
        }
      }
    }
    "a new uniform row is needed" should {
      "create one consisting of 1 cell type" in {
        val row = new Row[Cell](10, Cell(0))
        for (elem <- row.spots) {
          elem.value shouldBe 0
        }
      }
    }
  }
}
