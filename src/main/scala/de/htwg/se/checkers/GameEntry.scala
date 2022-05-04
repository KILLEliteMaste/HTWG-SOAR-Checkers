package de.htwg.se.checkers

import com.google.inject.Guice
import de.htwg.se.board.dbComponent.slickImpls.DaoSlickImpl
import de.htwg.se.checkers.aview
import de.htwg.se.checkers.aview.UserInterface
import de.htwg.se.checkers.controller.ControllerInterface
import de.htwg.se.checkers.CheckersModule

import scala.util.{Failure, Success, Try}

@main def main(): Unit =
  val injector = Guice.createInjector(new CheckersModule)
  val controller = injector.getInstance(classOf[ControllerInterface])

  val uiTypeGui = if System.getenv("CHECKERS_UI_TYPE") == null then "tui rui gui" else System.getenv("CHECKERS_UI_TYPE")

  Try(aview.UserInterface(uiTypeGui, controller)) match
    case Failure(v) => println("Could not start UI because: " + v.getMessage + v.printStackTrace())
    case Success(v) => println("GOOD BYE")
