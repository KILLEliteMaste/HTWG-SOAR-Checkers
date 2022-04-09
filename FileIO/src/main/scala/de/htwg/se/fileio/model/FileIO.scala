package de.htwg.se.fileio.model

import scala.util.Try


trait FileIO:
        def load: Try[Game]
        def save(game: Game): Unit
