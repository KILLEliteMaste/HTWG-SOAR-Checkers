package model

import model.Color.{BLACK, WHITE, UNKNOWN}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CellSpec extends AnyWordSpec with Matchers {
  "A Cell" when {
    "not set to any value " should {
      val emptyCell = Cell(1)

      "have value 1" in {
        emptyCell.value should be(1)
      }
      "not be set" in {
        emptyCell.isSet should be(true)
      }
      "show unplayable mark" in {
        emptyCell.toString.matches("/s{3}")
      }
    }
    "set to 1" should {
      val cell1 = Cell(1)
      "be WHITE" in {
        cell1.color shouldBe WHITE
      }
    }
    "set to 3" should {
      val cell3 = Cell(3)
      "be BLACK" in {
        cell3.color shouldBe BLACK
      }
    }
    "set to a specific value" should {
      val nonEmptyCell = Cell(5)
      "return that value" in {
        nonEmptyCell.value shouldBe(5)
      }
      "be set" in {
        nonEmptyCell.isSet should be(true)
      }
      "have no color" in {
        nonEmptyCell.color shouldBe UNKNOWN
      }
      "show number in String" in {
        nonEmptyCell.toString.matches("▐ 5 ▐")
      }
    }
  }
}
