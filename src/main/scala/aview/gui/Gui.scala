package aview.gui

import aview.UI
import controller.ControllerInterface
import model.GameState
import scalafx.application.{JFXApp3, Platform}
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, Button}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{BorderPane, ColumnConstraints, GridPane, RowConstraints}
import scalafx.scene.media.AudioClip
import scalafx.scene.text.Text
import util.{Observer, Position}

import scala.collection.mutable.ListBuffer

case class Gui(controller: ControllerInterface) extends UI with Observer with JFXApp3 {
  controller.add(this)

  val audio: AudioClip = new AudioClip(getClass.getClassLoader.getResource("click.mp3").toExternalForm)
  val moveList: ListBuffer[Position] = ListBuffer()
  val gui: Gui = this
  val buttons: Buttons = Buttons(gui)

  var gameGrid: GridPane = new GridPane {
    for (_ <- 0 until controller.getGame.field.fieldSize) {
      val row = new RowConstraints() {
        percentHeight = 100.0 / controller.getGame.field.fieldSize
      }
      rowConstraints.add(row)
      val column = new ColumnConstraints() {
        percentWidth = 100.0 / controller.getGame.field.fieldSize
      }
      styleClass = List("gameGridRowColumn")
      columnConstraints.add(column)
    }
  }

  //TODO: BESSER?
  def resizeGameGrid(): Unit = {
    gameGrid.columnConstraints.clear()
    gameGrid.rowConstraints.clear()
    gameGrid.styleClass = List("gameGridRowColumn")
    for (_ <- 0 until controller.getGame.field.fieldSize) {
      val row = new RowConstraints() {

        percentHeight = 100.0 / controller.getGame.field.fieldSize
      }
      gameGrid.rowConstraints.add(row)
      val column = new ColumnConstraints() {
        percentWidth = 100.0 / controller.getGame.field.fieldSize
      }
      gameGrid.columnConstraints.add(column)
    }
  }

  val playerStateText = new Text(controller.getGame.playerState.toString)
  val statusMessageText: Text = new Text() {
    text <== returnMessage
  }

  var statusGrid: GridPane = new GridPane {
    val row: RowConstraints = new RowConstraints() {
      minHeight = 40
      styleClass = List("statusGrid")
    }
    rowConstraints.add(row)

    val colC: ColumnConstraints = new ColumnConstraints() {
      percentWidth = 20.0
    }
    val colC2: ColumnConstraints = new ColumnConstraints() {
      percentWidth = 80.0
    }
    columnConstraints = List(colC, colC2)

    add(playerStateText, 0, 0)
    add(statusMessageText, 1, 0)
  }

  var controlGrid: GridPane = new GridPane {
    val row: RowConstraints = new RowConstraints() {
      percentHeight = 100
      prefHeight = 60
    }
    rowConstraints.add(row)

    for (_ <- 0 until 6) {
      val colC = new ColumnConstraints() {
        percentWidth = 100
      }
      columnConstraints.add(colC)
    }
    add(buttons.getRedoButton, 0, 0)
    add(buttons.getUndoButton, 1, 0)
    add(buttons.getNewGameButton, 2, 0)
    add(buttons.getMoveButton, 3, 0)
    add(buttons.getLoadButton, 4, 0)
    add(buttons.getSaveButton, 5, 0)
  }

  def run(): Unit = {
      main(Array())
  }

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title.value = "Checkers"
      minWidth = 650
      minHeight = 800
      scene = new Scene {
        stylesheets = List(getClass.getResource("/stylesheet.css").toExternalForm)
        root = new BorderPane {
          top = statusGrid
          center = gameGrid
          bottom = controlGrid
        }
      }
    }
    
    //Resize only diagonal
    stage.minHeightProperty().bind(stage.widthProperty().multiply(1.2))
    stage.maxHeightProperty().bind(stage.widthProperty().multiply(1.2))
  }


  override def update(): Unit = {
    Platform.runLater(() -> {
      setBoard()
      controller.getGame.gameState match {
        case GameState.DRAW => new Alert(AlertType.Information, "Draw!").showAndWait()
        case GameState.P1_WON => new Alert(AlertType.Information, "Player 1 won the game!").showAndWait()
        case GameState.P2_WON => new Alert(AlertType.Information, "Player 2 won the game!").showAndWait()
        case _ =>
      }
      playerStateText.text = controller.getGame.playerState.toString
    })
  }

  def setBoard(): Unit = {
    gameGrid.getChildren.removeAll(gameGrid.getChildren)
    for (x <- 0 until controller.getGame.field.fieldSize; y <- 0 until controller.getGame.field.fieldSize) {
      val stone: Button = buttons.gameFieldButton(x, y)
      if (controller.getGame.field.fieldMatrix.cell(x, y).isDefined) {
        val img = controller.getGame.field.fieldMatrix.cell(x, y).get.value match {
          case 1 => new Image("/white.jpg");
          case 2 => new Image("/whiteKing.png");
          case 3 => new Image("/black.jpg");
          case 4 => new Image("/blackKing.png");
        }
        val view = new ImageView(img)
        view.setFitHeight(50)
        view.setFitWidth(50)
        view.setPreserveRatio(true)
        stone.setGraphic(view)
        stone.setMaxSize(Double.MaxValue, Double.MaxValue)
      }
      stone.setMaxSize(Double.MaxValue, Double.MaxValue)
      gameGrid.add(stone, y, x)
    }
  }

  def getTileCss(x: Int, y: Int): String = {
    if (y % 2 == 0) if (x % 2 == 0) "whiteTile" else "blackTile"
    else if (x % 2 == 0) "blackTile" else "whiteTile"
  }

  //Display the board at startup
  setBoard()
}


