import aview.UserInterface
import com.google.inject.Guice
import controller.ControllerInterface
import model.CheckersModule

import scala.util.{Failure, Success, Try}

case object Game {

  def main(args: Array[String]): Unit = {


    val uiTypeGui = "both"
    val injector = Guice.createInjector(new CheckersModule)
    val controller = injector.getInstance(classOf[ControllerInterface])
    //val controller = Controller(GameImpl(8))

    Try(UserInterface(uiTypeGui, controller)) match {
      case Failure(v) => println("Could not start UI because: " + v.getMessage + v.printStackTrace())
      case Success(v) => println("GOOD BYE")
    }
  }
}
