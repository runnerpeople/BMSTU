/**
  * Created by runnerpeople on 11.03.2017.
  */

object Test {

  val zipP: (List[Int], List[Int], (Int, Int) => Boolean) => List[(Int, Int)] = {
    case (Nil, Nil, pred) => Nil
    // If different size of lists //
    case (Nil, y :: ys, pred) => zipP(Nil, ys, pred)
    case (x :: xs, Nil, pred) => zipP(xs, Nil, pred)
    // ========================== //
    case (x :: xs, y :: ys, pred) if pred(x, y) => (x, y) :: zipP(xs, ys, pred)
    case (x :: xs, y :: ys, pred) => zipP(xs, ys, pred)
  }

  def main(args: Array[String]): Unit = {
    val A = List(1, 3, 5, 7, 9)
    val B = List(2, 4, 6, 8, 10)
    val result = zipP(A, B, _ + _ % 2 != 0)
    println(result)
  }
}


