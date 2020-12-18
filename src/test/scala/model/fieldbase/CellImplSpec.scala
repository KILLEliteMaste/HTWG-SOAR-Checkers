package model.fieldbase

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CellImplSpec extends AnyWordSpec with Matchers {
  "A Cell" when {
    "not set to any value " should {
      val emptyCell = CellImpl(1)

      "have value 1" in {
        emptyCell.getValue shouldBe 1
      }
      "not be set" in {
        emptyCell.isSet shouldBe true
      }
      "show unplayable mark" in {
        emptyCell.toString.matches("/s{3}")
      }
    }
    "set to 1" should {
      val cell1 = CellImpl(1)
      "be WHITE" in {
        cell1.getColor shouldBe "WHITE"
      }
    }
    "set to 3" should {
      val cell3 = CellImpl(3)
      "be BLACK" in {
        cell3.getColor shouldBe "BLACK"
      }
    }
    "set to a specific value" should {
      val nonEmptyCell = CellImpl(5)
      "return that value" in {
        nonEmptyCell.getValue shouldBe 5
      }
      "be set" in {
        nonEmptyCell.isSet shouldBe true
      }
      "have no color" in {
        nonEmptyCell.getColor shouldBe "UNKNOWN"
      }
      "show number in String" in {
        nonEmptyCell.toString.matches("▐ 5 ▐")
      }
    }
  }
}
