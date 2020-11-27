package model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CellSpec extends AnyWordSpec with Matchers {
  "A Cell" when {
    "not set to any value " should {
      val emptyCell = Cell(0)

      "have value 0" in {
        emptyCell.value should be(0)
      }
      "not be set" in {
        emptyCell.isSet should be(false)
      }
      "show unplayable mark" in {
        emptyCell.toString.matches("/s{3}")
      }
    }
    "set to 1" should {
      val cell1 = Cell(1)
      "be WHITE" in {
        cell1.color should be ("WHITE")
      }
    }
    "set to 3" should {
      val cell3 = Cell(3)
      "be BLACK" in {
        cell3.color should be ("BLACK")
      }
    }
    "set to a specific value" should {
      val nonEmptyCell = Cell(5)
      "return that value" in {
        nonEmptyCell.value should be(5)
      }
      "be set" in {
        nonEmptyCell.isSet should be(true)
      }
      "have no color" in {
        nonEmptyCell.color should be ("NONE")
      }
      "show number in String" in {
        nonEmptyCell.toString.matches("▐ 5 ▐")
      }
    }
  }
}
