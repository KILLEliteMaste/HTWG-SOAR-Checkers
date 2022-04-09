package de.htwg.se.checkers

import com.google.inject.{AbstractModule, Provides}
import com.google.inject.multibindings.Multibinder
import com.google.inject.name.Names
import de.htwg.se.board.gamebase.FieldMatrixImpl
import de.htwg.se.board
import de.htwg.se.board.{Cell, FieldMatrix, Game}
import de.htwg.se.board.gamebase.{CellImpl, FieldMatrixImpl, GameImpl}
import de.htwg.se.checkers.controller.ControllerInterface
import net.codingwell.scalaguice.ScalaModule
import de.htwg.se.board.FieldMatrix
import de.htwg.se.fileio.FileIO
import de.htwg.se.fileio.fileiojson.FileIOImpl


class CheckersModule extends AbstractModule {
  val defaultSize: Int = 8

  override def configure(): Unit = {
    bindConstant().annotatedWith(Names.named("DefaultSize")).to(defaultSize)

    bind(classOf[Game]).toInstance(new GameImpl(8))

    bind(classOf[Cell]).toInstance(CellImpl(0))
    
    bind(classOf[FieldMatrix[Option[Cell]]]).toInstance(new FieldMatrixImpl[Option[Cell]](8, None))
    bind(classOf[ControllerInterface]).to(classOf[controller.controllerbase.Controller])
    
    bind(classOf[FileIO]).to(classOf[FileIOImpl])
  }
}
