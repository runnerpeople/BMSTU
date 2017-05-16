
class Pos private ( val prog : String , val offs : Int , val line : Int , val col : Int ) {
    def this (prog : String) = this (prog , 0, 1, 1)
    def ch = if (offs == prog.length) -1 else prog(offs).toInt
    def inc = ch match {
        case '\n' => new Pos(prog,offs +1,line +1,1)
        case -1 => this
        case _ => new Pos(prog,offs +1,line,col +1)
    }
    override def toString = "(" + line + "," + col + ")"
}

object DomainTags extends Enumeration {
    type Tag = Value
    val WHITESPACE , IDENT , NUMBER , END_OF_PROGRAM, KEYWORD, STRING, COMMENT, UNKNOWN = Value
}

import DomainTags._

class Scanner {
  def scan (start : Pos, errors: List[String]): (Tag,Pos,List[String]) =
      sys.error("syntax error at " + start)
}


class Token ( val start : Pos , scanner : Scanner, val errors: List[String]) {

   def this(start : Pos, scanner : Scanner) = this(start,scanner,List())

   val (tag, follow,list_error) = now()

   def now(): (Tag,Pos,List[String]) = {
     start.ch match {
       case -1 => (END_OF_PROGRAM, start, errors)
       case _ => scanner.scan(start, errors)
     }
   }

    def image = start.prog.substring(start.offs,follow.offs)
    def next = new Token(follow,scanner,list_error)
}

trait Whitespaces extends Scanner {
   private def missWhitespace (pos : Pos): Pos = pos.ch match {
      case ' ' | '\t' | '\n'  => missWhitespace (pos.inc)
      case _                  => pos
   }

   override def scan (start : Pos, errors: List[String]) = {
      val follow = missWhitespace ( start )
      if (start != follow) (WHITESPACE,follow,errors)
      else super.scan(start,errors)
   }
}

/* trait Idents extends Scanner {
    private def scanIdent(pos : Pos,flag: Boolean) : (Pos,Boolean) = {
        (pos,flag) match {
          case (c,d) if c.ch >= '0' && c.ch <= '9'                                               => scanIdent(pos.inc, d)
          case (c,d) if Character.toLowerCase(c.ch) >= 'a' && Character.toLowerCase(c.ch) <= 'z' => scanIdent(pos.inc, true)
          case (c,d)                                                                             => (c,d)
        }
    }

    override def scan(start: Pos, errors: List[String]) = {
        val follow = scanIdent(start,false)
        if (follow._1 == start) super.scan(start,errors)
        else {
          val image = start.prog.substring(start.offs, follow._1.offs).toLowerCase()
          if (follow._2 && (" \t\n" contains follow._1.ch) || (follow._1.ch == -1)) {
            if (image.equals("while") || image.equals("unless") || image.equals("for"))
              (KEYWORD, follow._1, errors)
            else
              (IDENT, follow._1, errors)
          }
          else {
            if (image.equals("while") || image.equals("unless") || image.equals("for")) {
              val add_error = "error in idents at " + start :: errors
              (KEYWORD, follow._1, add_error)
            }
            else {
              val add_error = "error in keywords at " + start :: errors
              (IDENT, follow._1, add_error)
            }
          }
        }
    }
}

trait Numbers extends Scanner {
    private def scanNumber(pos : Pos,flag: Boolean) : (Pos,Boolean) = {
      (pos,flag) match {
          case (c,d) if c.ch >= '0' && c.ch <= '9'                                               => scanNumber(pos.inc, true)
          case (c,d) if Character.toLowerCase(c.ch) >= 'a' && Character.toLowerCase(c.ch) <= 'z' => (c,false)
          case (c,d)                                                                             => (c,d)
      }
    }

    override def scan(start: Pos,errors:List[String]) = {
        val follow = scanNumber(start,false)
        if (follow._1 == start || !follow._2) super.scan(start,errors)
        else {
          if (follow._2 && (" \t\n" contains follow._1.ch) || (follow._1.ch == -1)) (NUMBER, follow._1, errors)
          else {
            val add_error = "error in number at " + start :: errors
            (NUMBER, follow._1, add_error)
          }
        }
    }
}

trait Strings extends Scanner {
    private def scanString(pos : Pos,flag: Boolean) : (Pos,Boolean) = {
      (pos,flag) match {
        case (c,d) if (" \t" contains c.ch) ||  (c.ch == -1)         => (c,false)
        case (c,d) if "\n"  contains c.ch                            => (c,true)
        case (c,d)                                                   => scanString(c.inc,d)
      }
    }

    override def scan(start: Pos, errors: List[String]) = {
      val checkStr = start.ch == '%'
      if (checkStr) {
          val follow = scanString(start,false)
          if (follow._2) (STRING, follow._1,errors)
          else {
            val add_error = "syntax error in string literals at " + start :: errors
            (STRING, follow._1,add_error)
          }
      }
      else super.scan(start,errors)
    }
}

trait Unknown extends Scanner {
  override def scan(start: Pos, errors: List[String]) = {
    val add_error = "Unknown token at " + start + ": " + start.prog(start.offs) :: errors
    (UNKNOWN, start.inc,add_error)
  }
} */


trait Idents extends Scanner {
  private def scanIdent(pos : Pos,flag: Boolean) : (Pos,Boolean) = {
    (pos,flag) match {
      case (c,d) if (c.ch >= '0' && c.ch <= '9')  || (Character.toLowerCase(c.ch) >= 'a' && Character.toLowerCase(c.ch) <= 'z') => scanIdent(pos.inc, true)
      case (c,d)                                                                                                                => (c,d)
    }
  }

  override def scan(start: Pos, errors: List[String]) = {
    val checkIsIdent = ("stv" contains start.ch) && (start.inc.ch == '.')
    if (checkIsIdent) {
      val follow = scanIdent(start.inc.inc, false)
      if (follow._1.toString.equals(start.inc.inc.toString)) {
          val add_error = "error in ident (null) at " + start :: errors
          (IDENT,follow._1,add_error)
      }
      else {
        if (follow._2 && (" \t\n" contains follow._1.ch) || (follow._1.ch == -1)) {
           (IDENT, follow._1, errors)
        }
        else {
            val add_error = "error in ident at " + start :: errors
            (IDENT, follow._1, add_error)
        }
      }
    }
    else {
        super.scan(start, errors)
    }
  }
}

trait Numbers extends Scanner {
  private def scanNumber(pos : Pos,flag: Boolean) : (Pos,Boolean) = {
    (pos,flag) match {
      case (c,d) if c.ch >= '0' && c.ch <= '9'    => scanNumber(pos.inc, true)
      case (c,d)                                  => (c,d)
    }
  }

  override def scan(start: Pos,errors:List[String]) = {
    val follow = scanNumber(start,false)
    if (follow._1 == start || !follow._2) super.scan(start,errors)
    else {
      if (follow._2 && (" \t\n" contains follow._1.ch) || (follow._1.ch == -1)) (NUMBER, follow._1, errors)
      else {
        val add_error = "error in number at " + start :: errors
        (NUMBER, follow._1, add_error)
      }
    }
  }
}

trait Comments extends Scanner {
  private def scanComment(pos : Pos,flag: Boolean) : (Pos,Boolean) = {
    (pos,flag) match {
      case (c,d) if (c.ch == '\n') || (c.ch == -1)   => (c,d)
      case (c,d)                                     => scanComment(pos.inc,d)
    }
  }

  override def scan(start: Pos,errors:List[String]) = {
    val checkComment = (start.col == 1) && (start.ch == '*')
    if (checkComment) {
      val follow = scanComment(start.inc, false)
      (COMMENT,follow._1,errors)
    }
    else {
       super.scan(start, errors)
    }
  }
}

trait Strings extends Scanner {
  private def scanString(pos : Pos,flag: Boolean) : (Pos,Boolean) = {
    (pos,flag) match {
      case (c,d) if ("\n \t"  contains c.ch) || (c.ch == -1)                                          => (c,false)
      case (c,d) if (c.ch == '\'') && (("\n \t"  contains c.inc.ch) || (c.inc.ch == -1))              => (c.inc.inc,true)
      case (c,d) if (c.ch == '\'') && (c.inc.ch == '\'')                                              => scanString(c.inc.inc,d)
      case (c,d) if (c.ch == '\'')                                                                    => (c.inc,false)
      case (c,d)                                                                                      => scanString(c.inc,d)
    }
  }

  override def scan(start: Pos, errors: List[String]) = {
    val checkStr = start.ch == '\''
    if (checkStr) {
      val follow = scanString(start.inc,true)
      if (follow._2) (STRING, follow._1,errors)
      else {
        val add_error = "syntax error in string literals at " + start :: errors
        (STRING, follow._1,add_error)
      }
    }
    else super.scan(start,errors)
  }
}

trait KeyWords extends Scanner {

  private def getPos(pos: Pos,offs: Int): (Pos) = {
    (pos,offs) match {
      case (c,0) => c
      case (c,d) => getPos(pos.inc,d-1)
    }
  }

  override def scan(start: Pos, errors: List[String]) = {
    val checkKeyword = start.ch == '$'
    if (checkKeyword) {
      try {
        val image = start.prog.substring(start.inc.offs, getPos(start.inc, 5).offs)
        if (image.equals("ENTRY"))
           (KEYWORD, getPos(start.inc, 5), errors)
        else if (image.equals("EXTER") && getPos(start.inc,5).ch == 'N')
          (KEYWORD, getPos(start.inc, 6), errors)
        else {
          val add_error = "unknown keyword at " + start :: errors
          (KEYWORD, start.inc, add_error)
        }
      }
      catch {
        case unknown => val add_error = "unknown keyword at " + start :: errors;
                        (KEYWORD, start, add_error)
      }
    }
    else super.scan(start,errors)
  }
}

trait Unknown extends Scanner {
  override def scan(start: Pos, errors: List[String]) = {
    val add_error = "Unknown token at " + start + ": " + start.prog(start.offs) :: errors
    (UNKNOWN, start.inc,add_error)
  }
}



class Lab3 {}

object Lab3 {
  def main(args: Array[String]): Unit = {
      // From STDIN //
      // var str = scala.io.Source.stdin.getLines().mkString("\n")
      var t = new Token(
        new Pos("$ENT"),
        //new Pos(str),
        new Scanner
          with Unknown
          with Strings
          with Idents
          with Numbers
          with Comments
          with KeyWords
          with Whitespaces
      )
      while (t.tag != END_OF_PROGRAM) {
        if (t.tag != UNKNOWN) {
          println(t.tag.toString + " " + t.start + "-" + t.follow + ": " + t.image)
        }
        t = t.next
      }
      if (t.list_error.nonEmpty) {
        println("Errors:")
        t.list_error.reverse.foreach(println)
      }
  }
}
