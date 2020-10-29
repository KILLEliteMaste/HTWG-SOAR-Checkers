package Field

case class Field(fieldSize: Int) {

  def totalFieldSize: Int = fieldSize * fieldSize

  val matrix: FieldMatrix[Cell] = FieldMatrix(Vector(
    Vector(Cell(1), Cell(1), Cell(1), Cell(1), Cell(1), Cell(1), Cell(1), Cell(1), Cell(1), Cell(1)),
    Vector(Cell(1), Cell(1), Cell(1), Cell(1), Cell(1), Cell(1), Cell(1), Cell(1), Cell(1), Cell(1)),
    Vector(Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0)),
    Vector(Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0)),
    Vector(Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0)),
    Vector(Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0)),
    Vector(Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0)),
    Vector(Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0), Cell(0)),
    Vector(Cell(1), Cell(1), Cell(1), Cell(1), Cell(1), Cell(1), Cell(1), Cell(1), Cell(1), Cell(1)),
    Vector(Cell(1), Cell(1), Cell(1), Cell(1), Cell(1), Cell(1), Cell(1), Cell(1), Cell(1), Cell(1)),
  ))

  override def toString: String = {
    var output = ""
    for (name <- matrix.rows) {
      for (cell <- name) {
        output += cell
      }
      output += "\n"
    }

    output
  }
}

case class FieldMatrix[T](rows: Vector[Vector[T]]) {
  def this(size: Int, filling: T) = this(Vector.tabulate(size, size) { (row, col) => filling })

  val size: Int = rows.size

  def cell(row: Int, col: Int): T = rows(row)(col)

  def replaceCell(row: Int, col: Int, cell: T): FieldMatrix[T] = copy(rows.updated(row, rows(row).updated(col, cell)))

  def fill(filling: T): FieldMatrix[T] = copy(Vector.tabulate(size, size) { (row, col) => filling })

  override def toString: String = {
    rows.toString()
  }
}