package de.htwg.se.fileio

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import scala.io.StdIn


case object FileIOService {

  val connectionInterface = "0.0.0.0"
  val connectionPort: Int = sys.env.getOrElse("FILE_IO_PORT", 8081).toString.toInt

  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem(Behaviors.empty, "my-system")
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.executionContext

    val route = concat(
      pathPrefix("json") {
        concat(
          get {
            val loadedGame = de.htwg.se.fileio.fileiojson.FileIOImpl().load.get
            val gameJson = de.htwg.se.fileio.fileiojson.FileIOImpl().gameToJson(loadedGame).toString
            complete(HttpEntity(ContentTypes.`application/json`, gameJson))
          },
          post {
            entity(as[String]) { game =>
              val oldGame = de.htwg.se.fileio.fileiojson.FileIOImpl().load.get

              de.htwg.se.fileio.fileiojson.FileIOImpl().save(game)

              if (de.htwg.se.fileio.fileiojson.FileIOImpl().load.isFailure) {
                de.htwg.se.fileio.fileiojson.FileIOImpl().save(oldGame)
                complete(StatusCodes.InternalServerError, "JSON does not conform a valid game")
              } else {
                complete(StatusCodes.OK, "Game saved")
              }
            }
          }
        )
      },
      pathPrefix("xml") {
        concat(
          get {
            val loadedGame = de.htwg.se.fileio.fileioxml.FileIOImpl().load.get
            val gameXML = de.htwg.se.fileio.fileioxml.FileIOImpl().gameToXml(loadedGame).toString
            complete(HttpEntity(ContentTypes.`text/xml(UTF-8)`, gameXML))
          },
          post {
            entity(as[String]) { game =>
              val oldGame = de.htwg.se.fileio.fileioxml.FileIOImpl().load.get

              de.htwg.se.fileio.fileioxml.FileIOImpl().save(game)

              if (de.htwg.se.fileio.fileioxml.FileIOImpl().load.isFailure) {
                de.htwg.se.fileio.fileioxml.FileIOImpl().save(oldGame)
                complete(StatusCodes.InternalServerError, "XML does not conform a valid game")
              } else {
                complete(StatusCodes.OK, "Game saved")
              }
            }
          }
        )
      }
    )

    val bindingFuture = Http().newServerAt(connectionInterface, connectionPort).bind(route)

    println(s"Server online at http://$connectionInterface:$connectionPort/\nPress RETURN to stop...")
  }
}