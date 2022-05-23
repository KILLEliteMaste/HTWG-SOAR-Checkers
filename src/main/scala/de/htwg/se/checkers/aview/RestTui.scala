package de.htwg.se.checkers.aview

import de.htwg.se.board.GameState
import de.htwg.se.checkers.controller.ControllerInterface
import de.htwg.se.checkers.controller.controllerbase.Controller
import de.htwg.se.checkers.util.Observer
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.*
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn.*

case class RestTui(controller: ControllerInterface) extends UI with Observer {
  controller.add(this)

  val appPort: Int = sys.env.getOrElse("APP_PORT", 8080).toString.toInt
  val connectionInterface = "0.0.0.0"



  def run(): Unit = {
    println("REST TUI STARTED")
    val config = ConfigFactory.load()
    implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-system", config)
    //println(system.settings.config.getString("akka.loglevel"))
    println(system.settings.config.getObject("akka.actor.default-dispatcher"))
    //system.logConfiguration()
    println("++++++++++++++++++++++++++")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext: ExecutionContextExecutor = system.executionContext

    val route = concat(
      pathPrefix("tui") {
        concat(
          newCommand,
          saveCommandDb,
          loadCommandDb,
          saveCommand,
          loadCommand,
          redoCommand,
          undoCommand,
          quitCommand,
          moveCommand
        )
      }
    )

    val bindingFuture = Http().newServerAt(connectionInterface, appPort).bind(route)

    println(s"Server online at http://$connectionInterface:$appPort/\nPress RETURN to stop...")
  }

  def newCommand: Route = path("new") {
    post {
      parameter("size"){
        size =>{
          complete(StatusCodes.OK, processInputLine(s"new $size", controller))
        }
      }
    }
  }

  def saveCommand: Route = path("save") {
    post {
      complete(StatusCodes.OK, processInputLine("save", controller))
    }
  }

  def loadCommand: Route = path("load") {
    post {
      complete(StatusCodes.OK, processInputLine("load", controller))
    }
  }

  def saveCommandDb: Route = path("savedb") {
    post {
      complete(StatusCodes.OK, processInputLine("savedb", controller))
    }
  }

  def loadCommandDb: Route = path("loaddb") {
    post {
      complete(StatusCodes.OK, processInputLine("loaddb", controller))
    }
  }

  def redoCommand: Route = path("redo") {
    post {
      complete(StatusCodes.OK, processInputLine("redo", controller))
    }
  }

  def undoCommand: Route = path("undo") {
    post {
      complete(StatusCodes.OK, processInputLine("undo", controller))
    }
  }

  def quitCommand: Route = path("quit") {
    post {
      complete(StatusCodes.OK, processInputLine("quit", controller))
    }
  }

  def moveCommand: Route = path("move") {
    post {
      parameter("positions"){
        positions =>{
          complete(StatusCodes.OK, processInputLine(s"move $positions", controller))
        }
      }
    }
  }

  override def update(): Unit = {
    println("REST UPDATE RECEIVED")
  }
}
