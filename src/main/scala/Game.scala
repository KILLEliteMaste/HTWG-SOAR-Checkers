import aview.UserInterface
import controller.Controller
import scalafx.scene.media.AudioClip

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

case object Game {

  def main(args: Array[String]): Unit = {
    val uiTypeGui = "gui"
    val controller = Controller()

    Try(UserInterface(uiTypeGui, controller)) match {
      case Failure(v) => println("Could not start UI because: " + v.getMessage + v.printStackTrace())
      case Success(v) => println("GOOD BYE")
    }
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