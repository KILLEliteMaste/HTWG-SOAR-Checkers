package controller.command

trait UndoableCommand extends Command {
  def undo(input: List[String]):String
}
