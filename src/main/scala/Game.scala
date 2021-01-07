import aview.UserInterface
import controller.controllerbase
import controller.controllerbase.Controller
import model.fieldbase.FieldImpl

import scala.util.{Failure, Success, Try}

case object Game {

  def main(args: Array[String]): Unit = {
    val uiTypeGui = "both"
    val controller = Controller(FieldImpl(8))

    Try(UserInterface(uiTypeGui, controller)) match {
      case Failure(v) => println("Could not start UI because: " + v.getMessage + v.printStackTrace())
      case Success(v) => println("GOOD BYE")
    }
  }
}
