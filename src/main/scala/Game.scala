import aview.TUI
import controller.Controller
import model.Field

import scala.io.StdIn._

case object Game {
  val controller: Controller = Controller(Field(8))
  val tui = new TUI(controller)
  controller.notifyObservers()

  def main(args: Array[String]): Unit = {
    var input: String = ""

    //Syntax for moving:
    //X Y -> e.g. 5 6
    //X Y X Y X Y X Y -> To XY then to XY...
    println("It's player 1 turn (white stone)")
    do {
      input = readLine()
      tui.processInputLine(input)
    } while (input != "q" || input != "quit")
    println("Game ended")
  }
}

/*
Schmeiß 2 steine gleichzeitig
2 1
3 2

5 6
4 7

3 2
4 3

4 7
3 6

1 0
2 1

5 4
3 2 1 0

 */