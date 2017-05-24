open Batteries;;
open DomainTag;;

class token start follow valuee = object inherit Token.token Word start follow
    
    val tag: DomainTag.tag = Word
    val coords: Fragment.fragment = new Fragment.fragment start follow
    val value: BatUTF8.t = valuee

    method tag = tag
    method coords = coords

    method to_string = 
        "WORD " ^ coords#to_string ^ ": " ^ value

    end;;
