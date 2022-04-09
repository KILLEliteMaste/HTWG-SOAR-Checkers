package model

import com.google.inject.Provides
import com.google.inject.multibindings.Multibinder
import com.google.inject.AbstractModule
import com.google.inject.name.Names
import controller.ControllerInterface
import model.gamebase.{CellImpl, FieldImpl, FieldMatrixImpl, GameImpl}
import de.htwg.se.fileio.model.FileIO
import de.htwg.se.fileio.model.fileiojson.FileIOImpl
import net.codingwell.scalaguice.ScalaModule

class CheckersModule extends AbstractModule {
  val defaultSize: Int = 8

  override def configure(): Unit = {
    bindConstant().annotatedWith(Names.named("DefaultSize")).to(defaultSize)

    bind(classOf[model.Game]).toInstance(new GameImpl(8))

    bind(classOf[Cell]).toInstance(CellImpl(0))
    
    bind(classOf[FieldMatrix[Option[Cell]]]).toInstance(new FieldMatrixImpl[Option[Cell]](8, None))
    bind(classOf[ControllerInterface]).to(classOf[controller.controllerbase.Controller])
    
    bind(classOf[FileIO]).to(classOf[FileIOImpl])
  }
}
