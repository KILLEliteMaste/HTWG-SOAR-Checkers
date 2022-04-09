package de.htwg.se.fileio

import com.google.inject.multibindings.Multibinder
import com.google.inject.name.Names
import com.google.inject.{AbstractModule, Provides}
import de.htwg.se.fileio.fileiojson.FileIOImpl
import net.codingwell.scalaguice.ScalaModule
import de.htwg.se.board.*
import de.htwg.se.board.gamebase.*

class FileIOModule extends AbstractModule {
  val defaultSize: Int = 8

  override def configure(): Unit = {
    bindConstant().annotatedWith(Names.named("DefaultSize")).to(defaultSize)

    bind(classOf[Game]).toInstance(new GameImpl(8))

    bind(classOf[Cell]).toInstance(CellImpl(0))
    
    bind(classOf[FieldMatrix[Option[Cell]]]).toInstance(new FieldMatrixImpl[Option[Cell]](8, None))
    
    //bind(classOf[de.htwg.se.fileio.FileIO]).to(classOf[FileIOImpl])
  }
}
