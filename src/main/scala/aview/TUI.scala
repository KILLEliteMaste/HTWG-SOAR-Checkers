package aview

import Field._

import scala.io.StdIn._
import scala.util.matching.Regex


class TUI {
  val numberRegex = "(\\d+)"

  def processInputLine(input: String, field: Field): Field = {
    val inputsplit = input.split("\\s+").toList
    inputsplit match {
      case "new" :: "field" :: Nil => new Field(10)
      case "quit" :: "game" :: Nil => field
      case _ => {
        if (inputsplit.size == 2) {
          if (inputsplit(0).matches(numberRegex) && inputsplit(1).matches(numberRegex)) {
            println("MOVE FROM: " + inputsplit(0) + "   " + inputsplit(1))
            val dest = scala.io.StdIn.readLine("Choose destination\n").split("\\s+").toList

            if (dest.size == 2) {
              if (dest(0).matches(numberRegex) && inputsplit(1).matches(numberRegex)) {
                val yDest = dest(0).toInt
                val xDest = dest(1).toInt
                val yOrigin = inputsplit(0).toInt
                val xOrigin = inputsplit(1).toInt

                val range = Vector.range(0, 10)
                if (range.contains(yDest) && range.contains(xDest) && range.contains(yOrigin) && range.contains(xOrigin)) {

                  field.matrix = field.matrix.replaceCell(xDest, yDest, field.matrix.cell(xOrigin, yOrigin))

                  field.matrix = field.matrix.replaceCell(xOrigin, yOrigin, Cell(0))
                  println("TO: " + dest(0) + "   " + dest(1))
                  return field
                } else {
                  println("One given number is out of range for the given field")
                }
              } else {
                println("One or more arguments do not match a number")
              }
            } else {
              println("Wrong amount of arguments")
            }

          } else {
            println("One or more arguments do not match a number")
          }
        } else {
          println("Wrong amount of arguments")
        }

        field
      }
    }
  }
}
