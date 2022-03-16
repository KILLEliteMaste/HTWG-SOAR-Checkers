import aview.UserInterface
import com.google.inject.Guice
import controller.ControllerInterface
import model.CheckersModule

import scala.util.{Failure, Success, Try}

@main def main(): Unit =
  val injector = Guice.createInjector(new CheckersModule)
  val controller = injector.getInstance(classOf[ControllerInterface])

  val uiTypeGui = if System.getenv("CHECKERS_UI_TYPE") == null then "both" else System.getenv("CHECKERS_UI_TYPE")

  Try(UserInterface(uiTypeGui, controller)) match
    case Failure(v) => println("Could not start UI because: " + v.getMessage + v.printStackTrace())
    case Success(v) => println("GOOD BYE")
