lazy val global = project.in(file("."))
  .settings(resolvers += "jitpack" at "https://jitpack.io")
  .settings(libraryDependencies ++= commonDependencies)
  .aggregate(fileio, board)
  .dependsOn(fileio, board)

lazy val fileio = project.in(file("FileIO"))
  .settings(libraryDependencies ++= fileioDependencies)
  .dependsOn(board)
  .settings(mainClass in Compile := Some("de.htwg.se.fileio.FileIOService"))

lazy val board = project.in(file("Board"))
  .settings(resolvers += "jitpack" at "https://jitpack.io")
  .settings(libraryDependencies ++= boardDependencies)
  //.enablePlugins(JavaAppPackaging)

lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux") => "linux"
  case n if n.startsWith("Mac") => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}

lazy val dependencies =
  new {
    val akkaVersion = "2.6.19"
    val akkaHttpVersion = "10.2.9"
    val scalacticVersion = "3.2.11"
    val scalatestVersion = "3.2.11"
    val scalaxmlVersion = "2.0.1"
    val playjsonVersion = "2.10.0-RC5"
    val guiceVersion = "5.1.0"
    val scalaguiceVersion = "5.0.2"
    val scalafxVersion = "17.0.1-R26"
    val javaFxVersion = "17.0.1"
    val slickHCPVersion = "3.3.3"
    val mariadbVersion = "3.0.4"
    val slf4jVersion = "1.7.36"
    val mongoDbVersion = "4.6.0"

    val mongoDb = ("org.mongodb.scala" %% "mongo-scala-driver" % mongoDbVersion).cross(CrossVersion.for3Use2_13)
    val slick = "com.github.slick.slick" % "slick_3" % "nafg~dottyquery-SNAPSHOT"
    val slf4j = "org.slf4j" % "slf4j-nop" % slf4jVersion
    val slickHCP = ("com.typesafe.slick" %% "slick-hikaricp" % slickHCPVersion).cross(CrossVersion.for3Use2_13)
    val mariadb = "org.mariadb.jdbc" % "mariadb-java-client" % mariadbVersion
    val akka = ("com.typesafe.akka" %% "akka-http" % akkaHttpVersion).cross(CrossVersion.for3Use2_13)
    val akkaactor = ("com.typesafe.akka" %% "akka-actor-typed" % akkaVersion).cross(CrossVersion.for3Use2_13)
    val akkastream = ("com.typesafe.akka" %% "akka-stream" % akkaVersion).cross(CrossVersion.for3Use2_13)
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
  dependencies.akka,
  dependencies.akkaactor,
  dependencies.akkastream,
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
  dependencies.akka,
  dependencies.akkastream,
  dependencies.akkaactor,
  dependencies.scalactic,
  dependencies.scalatest,
  dependencies.scalaXML,
  dependencies.playJson,
  dependencies.guice,
  dependencies.scalaGuice,
)

val boardDependencies = Seq(
  dependencies.mongoDb,
  dependencies.slick,
  dependencies.slickHCP,
  dependencies.slf4j,
  dependencies.mariadb,
  dependencies.scalactic,
  dependencies.scalatest,
  dependencies.guice,
  dependencies.scalaGuice,
)

name := "Checkers"
organization in ThisBuild := "de.htwg.se"
version := "0.1"
scalaVersion in ThisBuild := "3.1.1"
