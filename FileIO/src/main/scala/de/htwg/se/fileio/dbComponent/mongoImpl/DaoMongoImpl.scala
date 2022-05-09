package de.htwg.se.fileio.dbComponent.mongoImpl

import com.mongodb.DBObject
import de.htwg.se.board.Game
import de.htwg.se.board.gamebase.GameImpl
import de.htwg.se.fileio.FileIO
import de.htwg.se.fileio.dbComponent.DaoInterface
import de.htwg.se.fileio.fileiojson.FileIOImpl
import org.bson.BsonObjectId
import org.bson.types.ObjectId
import org.mongodb.scala.*
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.model.*
import org.mongodb.scala.model.Filters.*
import org.mongodb.scala.model.Updates.*
import org.mongodb.scala.model.UpdateOptions
import org.mongodb.scala.result.InsertOneResult
import play.api.libs.json.{JsNumber}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import org.mongodb.scala.model.Filters.*
import org.mongodb.scala.model.Projections.*
import scala.io.Source

import scala.util.Try

class DaoMongoImpl extends DaoInterface {

  val uri: String = s"mongodb://tb:${sys.env.getOrElse("MONGO_DB_PASSWORD", "")}@better-tickets.de:2379/trading_bot?retryWrites=true&w=majority"
  System.setProperty("org.mongodb.async.type", "netty")
  val client: MongoClient = MongoClient(uri)
  val db: MongoDatabase = client.getDatabase("trading_bot")
  val collection: MongoCollection[Document] = db.getCollection("checkers")

  val jsonFileIo = FileIOImpl()

  val gameId = sys.env.getOrElse("GAME_ID", "1").toInt

  override def load(): Try[Game] = {
    val collection = db.getCollection("checkers")
    val result = Await.result(collection.find(equal("id", gameId)).first().head(), Duration.Inf)
    jsonFileIo.loadByString(result.toJson())
  }

  override def save(game: Game): Unit = {
    val jsObject = jsonFileIo.gameToJson(game) + ("id" -> JsNumber(gameId))
    val doc = Document(BsonDocument.apply(jsObject.toString))

    collection.countDocuments(equal("id", gameId)).subscribe(new Observer[Long] {
      override def onSubscribe(subscription: Subscription): Unit = subscription.request(1)

      override def onNext(result: Long): Unit = if (result == 0) documentNotFound(doc) else documentFound(doc)

      override def onError(e: Throwable): Unit = println("Failed")

      override def onComplete(): Unit = println("Completed")
    })
  }

  def documentNotFound(doc: Document) = {
    collection.insertOne(doc).subscribe(new Observer[InsertOneResult] {

      override def onSubscribe(subscription: Subscription): Unit = subscription.request(1)

      override def onNext(result: InsertOneResult): Unit = println(s"onNext $result")

      override def onError(e: Throwable): Unit = println("New Insert Failed")

      override def onComplete(): Unit = println("New Insert Complete")
    })
  }

  def documentFound(doc: Document) = {
    collection
      .findOneAndReplace(equal("id", gameId), doc)
      .subscribe(new Observer[Any] {

        override def onSubscribe(subscription: Subscription): Unit = subscription.request(1)

        override def onNext(result: Any): Unit = println(s"onNext $result")

        override def onError(e: Throwable): Unit = println("Failed")

        override def onComplete(): Unit = println("Completed")
      })
  }
}
