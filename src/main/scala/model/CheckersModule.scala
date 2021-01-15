package model

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import controller.ControllerInterface
import model.gamebase.{CellImpl, FieldImpl, FieldMatrixImpl, GameImpl}
import model.fileiocomponent.FileIO
import net.codingwell.scalaguice.ScalaModule

case class CheckersModule() extends AbstractModule with ScalaModule {
  val defaultSize: Int = 8

  override def configure(): Unit = {
    bindConstant().annotatedWith(Names.named("DefaultSize")).to(defaultSize)

    bind[Game].to[GameImpl]
    bind[Field].to[FieldImpl]
    bind[Cell].to[CellImpl]
    bind[FieldMatrix[Option[Cell]]].to[FieldMatrixImpl[Option[Cell]]]
    bind[ControllerInterface].to[controller.controllerbase.Controller]

    bind[Game].annotatedWithName("8").toInstance(GameImpl(8))
    bind[Game].annotatedWithName("10").toInstance(GameImpl(10))
    bind[Game].annotatedWithName("12").toInstance(GameImpl(12))

    bind[FileIO].to[model.fileiocomponent.fileiojson.FileIOImpl]
  }
}
