package aview
import Field._

class TUI {

  def processInputLine(input: String, field:Field): Field = {
    //input = input.split(" ").toList
      input match {
        case "n" => new Field(10)
        case "q" => field
      }
  }
}
