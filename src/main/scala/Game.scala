import Field._
import aview.TUI
import scala.io.StdIn._

case object Game {
  var field = new Field(10)
  val tui = new TUI
  def main(args: Array[String]): Unit = {
    var input: String = ""

    do {
      println(field.toString)
      input = readLine()
      field = tui.processInputLine(input, field)
    } while (input != "q")
    /*
    val field = Field(10)
    println(field.toString)
     */

  }
}
