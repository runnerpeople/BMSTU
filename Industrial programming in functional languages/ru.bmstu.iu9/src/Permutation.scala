import scala.annotation.tailrec

/**
  * Created by runnerpeople on 18.03.2017.
  */

class Permutation private(k : Int, m: Int, list_args : List[(Int,Int)]) {
    if (m > k) {
       throw new IllegalArgumentException("Number of elements must be greater than range of elements!")
    }

    val list = list_args match {
        case Nil  => List((k,m))
        case _    => list_args
    }

    def this(k : Int, m: Int) = this(k,m,Nil)
    def this(list_args: List[(Int,Int)]) = this(0,0,list_args)


    def + (p : Permutation): Permutation = {
        val flag = (list.head._1 >= p.list.head._1) && (list.head._2 < p.list.head._2)
        val whole_list_x = flag match {
            case false => (this.list ::: p.list).sortWith(Permutation.sortTupleByFirst)
            case true  => (p.list ::: this.list).sortWith(Permutation.sortTupleByFirst)
        }
        // Functions help to find tuples (t11,t12) < (t21,t22), where t11<t21 && t12<t22              //
        def inner(xs : List[(Int,Int)],elem: (Int,Int),flag : Boolean): Boolean = {
            (xs,elem,flag) match {
              case (Nil,element,result)                          => result
              case (x :: tail,element,result) if x._2 < elem._2  => inner(tail,element,result)
              case (x :: tail,element,result)                    => inner(tail,element,!result)
            }
        }
        def inner_help(xs: List[(Int,Int)],result : List[(Int,Int)]): List[(Int,Int)] = {
            val flag = false
            (xs,result) match {
                case (Nil,result_list)                             => result_list
                case (x :: tail,result_list) if inner(tail,x,flag) => inner_help(tail,x :: result_list)
                case (x :: tail,result_list)                       => inner_help(tail,result_list)
            }
        }
        // =============================================================================              //
        val elements_delete = inner_help(whole_list_x,Nil)
        val right_list = whole_list_x.filter(elem => !elements_delete.contains(elem))
        new Permutation(right_list)
    }

    def in_: (seq : List[Int]) = {
      if (seq.length != seq.distinct.length)
          throw new IllegalArgumentException("Finding permutations with repetition " +
                                             "in set of permutations with no repetition!")
      val list = this.list
      val new_list = list.filter(pair => pair._1 >= seq.max && pair._2 == seq.length)
      new_list.nonEmpty
    }


    def size: BigInt = {
      @tailrec
      def inner(xs : List[(Int,Int)], accum : BigInt): BigInt = {
        xs match {
          case x :: tail => inner(tail, accum + Permutation.count(x._1,x._2))
          case Nil => accum
        }
      }
      inner(this.list,BigInt(0))
    }

    override def toString: String = {
      def inner(list_args: List[(Int,Int)],accum: List[String]): List[String] = {
        (list_args,accum) match {
          case (Nil,result)           => result
          case (x :: tail, result)    => inner(tail, "A_{" + x._1.toString + "}^{" + x._2.toString + "}" :: result)
        }
      }
      val output = inner(this.list,Nil)
      output.mkString(" + ")
    }
}

object Permutation {
    def sortTupleByFirst(v1 : (Int,Int), v2: (Int,Int)) = {
        v1._1 <= v2._1
    }

    def count(k: Int, m: Int): BigInt = {
      if (k == m)
        factorial(k+1)
      else
        factorial(k + 1) / factorial(k + 1 - m)
    }

    def factorial(n: Int): BigInt = {
      @tailrec
      def inner(number : Int, accum : BigInt): BigInt = {
        number match {
          case 1 => accum
          case _ => inner(number-1, accum * number)
        }
      }
       inner(n,BigInt(1))
    }
  // Generated by Intellij IDEA          //
  def main(args: Array[String]): Unit = {
    val p1 = new Permutation(15,2)
    val p2 = new Permutation(9,4)
    val p3 = new Permutation(4,3)
    println(p1 + p2 + p3)
    println(p1.size)
    println(List(3,1) in_: p3)
  }
  // ================================    //Ujif kj[
}

// Run in REPL //

//val p1 = new Permutation(15,2)
//val p2 = new Permutation(9,4)
//val p3 = new Permutation(4,3)
//p1 + p2 + p3
//println(p1.size())
//println(List(3,1) in_: p3)

// ========== //