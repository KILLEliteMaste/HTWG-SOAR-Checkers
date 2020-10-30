package Field

//noinspection DuplicatedCode
case class Field(fieldSize: Int) {

  def totalFieldSize: Int = fieldSize * fieldSize


  val matrix: FieldMatrix[Cell] = {
    val builder = Vector.newBuilder[Vector[Cell]]

    for {index <- 1 to fieldSize} {
      if (index < 4 || fieldSize - 3 < index) {
        val builder0 = Vector.newBuilder[Cell]
        if (index % 2 == 0) {
          // Jede 2 Reihe ab 2er Reihe
          for {index0 <- 1 to fieldSize} {
            if (index0 % 2 == 0) {
              builder0.addOne(Cell(0))
            } else {
              builder0.addOne(Cell(1))
            }
          }
        } else {
          // Jede 1 Reihe ab 1er Reihe
          for {index0 <- 1 to fieldSize} {
            if (index0 % 2 == 0) {
              builder0.addOne(Cell(1))
            } else {
              builder0.addOne(Cell(0))
            }
          }
        }
        builder.addOne(builder0.result())
      } else {
        val builder0 = Vector.newBuilder[Cell]
        for {index0 <- 1 to fieldSize} {
          builder0.addOne(Cell(0))
        }
        builder.addOne(builder0.result())
      }
    }
    FieldMatrix(builder.result())
  }

  override def toString: String = {
    var output = ""
    for (row <- matrix.rows) {
      for (cell <- row) {
        output += cell
      }
      output += "\n"
    }
    output = output.replace("▐▐","▐")
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