lazy val global = project.in(file("."))
  .settings(libraryDependencies ++= commonDependencies)
  .settings(mainClass in Compile := Some("GameEntry"))
  .aggregate(fileio, board)
  .dependsOn(fileio, board)

lazy val fileio = project.in(file("FileIO"))
  .settings(libraryDependencies ++= fileioDependencies)
  .dependsOn(board)

lazy val board = project.in(file("Board"))
  .settings(libraryDependencies ++= boardDependencies)

lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux") => "linux"
  case n if n.startsWith("Mac") => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}

lazy val dependencies =
  new {
    val scalacticVersion = "3.2.11"
    val scalatestVersion = "3.2.11"
    val scalaxmlVersion = "2.0.1"
    val playjsonVersion = "2.10.0-RC5"
    val guiceVersion = "5.1.0"
    val scalaguiceVersion = "5.0.2"
    val scalafxVersion = "17.0.1-R26"
    val javaFxVersion = "17.0.1"

    val scalactic = "org.scalactic" %% "scalactic" % scalacticVersion
    val scalatest = "org.scalatest" %% "scalatest" % scalatestVersion % "test"
    val scalaXML = "org.scala-lang.modules" %% "scala-xml" % scalaxmlVersion
    val playJson = "com.typesafe.play" %% "play-json" % playjsonVersion
    val guice = "com.google.inject" % "guice" % guiceVersion
    val scalaGuice = ("net.codingwell" %% "scala-guice" % scalaguiceVersion).cross(CrossVersion.for3Use2_13)
    val scalafx = "org.scalafx" %% "scalafx" % scalafxVersion
    val javafxBase = "org.openjfx" % "javafx-base" % javaFxVersion classifier osName
    val javafxControls = "org.openjfx" % "javafx-controls" % javaFxVersion classifier osName
    val javafxFXML = "org.openjfx" % "javafx-fxml" % javaFxVersion classifier osName
    val javafxGraphics = "org.openjfx" % "javafx-graphics" % javaFxVersion classifier osName
    val javafxMedia = "org.openjfx" % "javafx-media" % javaFxVersion classifier osName
    val javafxSwing = "org.openjfx" % "javafx-swing" % javaFxVersion classifier osName
    val javafxWeb = "org.openjfx" % "javafx-web" % javaFxVersion classifier osName
  }

val commonDependencies = Seq(
  dependencies.scalactic,
  dependencies.scalatest,
  dependencies.scalaXML,
  dependencies.playJson,
  dependencies.guice,
  dependencies.scalaGuice,
  dependencies.scalafx,
  dependencies.javafxBase,
  dependencies.javafxControls,
  dependencies.javafxFXML,
  dependencies.javafxMedia,
  dependencies.javafxGraphics,
  dependencies.javafxSwing,
  dependencies.javafxWeb,
)

val fileioDependencies = Seq(
  dependencies.scalactic,
  dependencies.scalatest,
  dependencies.scalaXML,
  dependencies.playJson,
  dependencies.guice,
  dependencies.scalaGuice,
)

val boardDependencies = Seq(
  dependencies.scalactic,
  dependencies.scalatest,
  dependencies.guice,
  dependencies.scalaGuice,
)

name := "Checkers"
organization in ThisBuild := "de.htwg.se"
version := "0.1"
scalaVersion in ThisBuild := "3.1.1"
