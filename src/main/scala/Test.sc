val vector = Vector.newBuilder[Vector[Int]]
vector.+=(Vector(1, 0, 1))
vector.+=(Vector(0, 1, 0))
val builded = vector.result()

builded.flatten.mkString(" ")