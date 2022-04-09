package de.htwg.se.board.gamebase

import de.htwg.se.board.gamebase.{FieldImpl, FieldMatrixImpl}
import de.htwg.se.board.{Cell, Field}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class FieldImplSpec extends AnyWordSpec with Matchers {
  "A Field" when {
    "constructed shouldBe given the length of its edge as size" should {
      val smallfield = FieldImpl(8)
      "have a total field size of 8" in {
        smallfield.fieldSize shouldBe 8
      }
    }
    "filled" in {
      val field = FieldImpl(10)
      
      field.fieldMatrix.cell(0, 1).get.value shouldBe 1
      field.fieldMatrix.cell(0, 0) shouldBe None
      field.fieldMatrix.cell(1, 0).get.value shouldBe 1
      field.fieldMatrix.cell(1, 1) shouldBe None
    }
  }
  "made a string" in {
    val smallfield = new FieldImpl(10)
    smallfield.toString shouldBe "     0   1   2   3   4   5   6   7   8   9  \n0  ▐   ▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐\n1  ▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐   ▐\n2  ▐   ▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐   ▐ 1 ▐\n3  ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐\n4  ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐\n5  ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐\n6  ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐   ▐\n7  ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐   ▐\n8  ▐   ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐\n9  ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐   ▐ 3 ▐   ▐\n"
  }
  "A FieldMatrix" when {
    "created" should {
      val matrix = new FieldMatrixImpl[Option[Cell]](1, None)
      "have a size of 1" in {
        matrix.size shouldBe 1
      }
    }
  }
  "A field" when {
    "set fieldStatistics" should {
      var field:Field = FieldImpl(8)
      "set field statistics" in {
        field = field.increaseFieldStatistics(1)
        field.fieldStatistics(1) shouldBe 1
      }
    }
  }
}
