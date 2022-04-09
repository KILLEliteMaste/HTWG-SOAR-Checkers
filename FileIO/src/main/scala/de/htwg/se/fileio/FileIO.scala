package de.htwg.se.fileio

import scala.util.Try
import de.htwg.se.board.Game

trait FileIO:
        def load: Try[Game]
        def save(game: Game): Unit
