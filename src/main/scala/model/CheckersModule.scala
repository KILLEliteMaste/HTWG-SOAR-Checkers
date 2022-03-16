package model

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import controller.ControllerInterface
import model.gamebase.{CellImpl, FieldImpl, FieldMatrixImpl, GameImpl}
import model.fileiocomponent.FileIO
import model.fileiocomponent.fileiojson.FileIOImpl
import net.codingwell.scalaguice.ScalaModule

class CheckersModule extends AbstractModule {
  val defaultSize: Int = 8

  override def configure(): Unit = {

    bindConstant().annotatedWith(Names.named("DefaultSize")).to(defaultSize)

    bind(classOf[model.Game]).to(classOf[GameImpl])
    bind(classOf[Field]).to(classOf[FieldImpl])
    bind(classOf[Cell]).to(classOf[CellImpl])
    bind(classOf[FieldMatrix[Option[Cell]]]).to(classOf[FieldMatrixImpl[Option[Cell]]])
    bind(classOf[ControllerInterface]).to(classOf[controller.controllerbase.Controller])

    bind(classOf[FileIO]).to(classOf[FileIOImpl])


    //bind[Game].annotatedWithName("8").toInstance(GameImpl(8))
    //bind[Game].annotatedWithName("10").toInstance(GameImpl(10))
    //bind[Game].annotatedWithName("12").toInstance(GameImpl(12))

  }
}
