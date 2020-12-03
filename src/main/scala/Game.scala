import aview.TUI
import controller.{Controller, UserInterface}

case object Game {

  def main(args: Array[String]): Unit = {
    val uiType = "tui"

    UserInterface(uiType, Controller())
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