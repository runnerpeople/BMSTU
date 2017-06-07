open Batteries;;
open DomainTag;;

class token start follow valuee = object inherit Token.token NonTerm start follow
    
    val tag: DomainTag.tag = NonTerm
    val coords: Fragment.fragment = new Fragment.fragment start follow
    val value: BatUTF8.t = valuee

    method tag = tag
    method coords = coords
    method value = value

    method to_string = 
        "NonTerm " ^ coords#to_string ^ ": " ^ value

    end;;
