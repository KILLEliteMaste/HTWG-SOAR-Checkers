import aview.UserInterface
import com.google.inject.Guice
import controller.ControllerInterface
import model.CheckersModule

import scala.util.{Failure, Success, Try}

case object Game {

  def main(args: Array[String]): Unit = {
    val injector = Guice.createInjector(new CheckersModule)
    val controller = injector.getInstance(classOf[ControllerInterface])

    val uiTypeGui = if (System.getenv("CHECKERS_UI_TYPE") == null) "both" else System.getenv("CHECKERS_UI_TYPE")

    Try(UserInterface(uiTypeGui, controller)) match {
      case Failure(v) => println("Could not start UI because: " + v.getMessage + v.printStackTrace())
      case Success(v) => println("GOOD BYE")
    }
  }
}
