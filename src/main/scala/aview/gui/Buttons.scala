package aview.gui

import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, Button, ButtonType}
import scalafx.scene.layout.GridPane
import util.Position

case class Buttons(gui: Gui) {
  def getUndoButton: Button = {
    val undoButton = new Button("Undo Move") {
      styleClass = List("controlGridButton")
    }
    undoButton.setMaxSize(Double.MaxValue, Double.MaxValue)
    undoButton.onAction = _ => {
      if (gui.processInputLine("undo", gui.controller).equals("Cannot undo")) {
        new Alert(AlertType.Error) {
          initOwner(gui.stage)
          title = "Error"
          headerText = "Cannot undo"
          contentText = "You need to do a move first to undo operation."
        }.showAndWait()
      }
    }
    undoButton
  }

  def getRedoButton: Button = {
    val redoButton = new Button("Redo Move") {
      styleClass = List("controlGridButton")
    }
    redoButton.setMaxSize(Double.MaxValue, Double.MaxValue)
    redoButton.onAction = _ => {
      if (gui.controller.redo().equals("Cannot redo")) {
        new Alert(AlertType.Error) {
          initOwner(gui.stage)
          title = "Error"
          headerText = "Cannot redo"
          contentText = "You need to do an undo operation first, to do an redo operation."
        }.showAndWait()
      }
    }
    redoButton
  }

  def getLoadButton: Button = {
    val loadButton = new Button("Load Game") {
      styleClass = List("controlGridButton")
    }
    loadButton.setMaxSize(Double.MaxValue, Double.MaxValue)
    loadButton.onAction = _ => {

      gui.processInputLine("load", gui.controller)
      gui.update()
    }
    loadButton
  }

  def getSaveButton: Button = {
    val saveButton = new Button("Save Game") {
      styleClass = List("controlGridButton")
    }
    saveButton.setMaxSize(Double.MaxValue, Double.MaxValue)
    saveButton.onAction = _ => {
      gui.processInputLine("save", gui.controller)
      gui.update()
    }
    saveButton
  }

  def getMoveButton: Button = {
    val moveButton = new Button("Move") {
      styleClass = List("controlGridButton")
    }
    moveButton.setMaxSize(Double.MaxValue, Double.MaxValue)
    moveButton.onAction = _ => {
      val str:StringBuilder = StringBuilder()
      
      gui.moveList foreach (f(str,_))

      gui.moveList.clear()
      val retVal = gui.processInputLine("move " + str, gui.controller)
      if (retVal.contains("MOVE FROM")) gui.audio.play()
    }
    moveButton
  }

  def f(sb:StringBuilder, pos:Position): Unit = sb.append(pos.x + " " + pos.y + " ")

  def getNewGameButton: Button = {
    val newGameButton = new Button("Restart Game") {
      styleClass = List("controlGridButton")
    }
    newGameButton.setMaxSize(Double.MaxValue, Double.MaxValue)
    newGameButton.onAction = _ => {
      //DO NOT CHANGE VARIABLE LETTER CASE
      val ButtonFieldEight = new ButtonType("8")
      val ButtonFieldTen = new ButtonType("10")
      val ButtonFieldTwelve = new ButtonType("12")
      val alert = new Alert(AlertType.Confirmation) {
        initOwner(gui.stage)
        title = "Field size choose dialog"
        headerText = "Please select a field size:"
        buttonTypes = Seq(ButtonFieldEight, ButtonFieldTen, ButtonFieldTwelve, ButtonType.Cancel)
      }
      alert.showAndWait() match {
        case Some(ButtonFieldEight) => gui.controller.createNewField(8)
        case Some(ButtonFieldTen) => gui.controller.createNewField(10)
        case Some(ButtonFieldTwelve) => gui.controller.createNewField(12)
        case _ => println("User canceled or closed the dialog")
      }
      gui.resizeGameGrid()
    }
    newGameButton
  }

  def gameFieldButton(x: Int, y: Int): Button = {
    val gameFieldButton = new Button {
      styleClass = List("gameFieldButton", gui.getTileCss(x, y))
      onMouseClicked = _ => {
        if (styleClass.contains("gameFieldButtonSelected")) {
          gui.moveList -= Position(GridPane.getRowIndex(this), GridPane.getColumnIndex(this))
          styleClass.remove("gameFieldButtonSelected")
        } else {
          styleClass.add("gameFieldButtonSelected")
          gui.moveList += Position(x, y)
        }
      }
    }
    gameFieldButton
  }
}
