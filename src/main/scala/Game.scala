import Field.{Cell, Field}

case object Game {
  def main(args: Array[String]): Unit = {
    val field = Field(10)
    println(field.toString)

  }
}
