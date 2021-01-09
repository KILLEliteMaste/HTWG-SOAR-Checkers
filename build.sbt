name := "Checkers"
organization := "de.htwg.se"
version := "0.1"
scalaVersion := "2.12.7"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.2"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.2" % "test"

// Add dependency on ScalaFX library
libraryDependencies += "org.scalafx" %% "scalafx" % "15.0.1-R20"




libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.0.0-M3"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.2"
libraryDependencies += "com.google.inject" % "guice" % "4.2.3"
libraryDependencies += "net.codingwell" %% "scala-guice" % "4.2.11"



/*
libraryDependencies += "com.google.inject" % "guice" % "4.2.3"
libraryDependencies += "net.codingwell" %% "scala-guice" % "4.2.11"
//XML
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.1.1"
//JSON
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.6"*/

// Determine OS version of JavaFX binaries
lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux") => "linux"
  case n if n.startsWith("Mac") => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}

// Add dependency on JavaFX libraries, OS dependent
lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFXModules.map(m =>
  "org.openjfx" % s"javafx-$m" % "15.0.1" classifier osName
)
//coverageExcludedPackages := ".*gui.*;.*UiFactory"
coverageExcludedPackages := ".*gui.*"
coverageExcludedFiles := ".*UiFactory.*"