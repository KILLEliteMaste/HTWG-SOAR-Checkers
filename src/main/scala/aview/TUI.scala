package aview
import Field._

class TUI {

  def processInputLine(input: String, field:Field): Field = {
    val inputsplit = input.split("\\s+").toList
      inputsplit match {
        case "new" :: "field" :: Nil => new Field(10)
        case "quit" :: "game" :: Nil => field
      }
  }
}
