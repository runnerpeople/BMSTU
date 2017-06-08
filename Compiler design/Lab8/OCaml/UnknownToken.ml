open Batteries;;
open DomainTag;;

class token ttag start follow valuee = object inherit Token.token ttag start follow
    
    val tag: DomainTag.tag = ttag
    val coords: Fragment.fragment = new Fragment.fragment start follow
    val value: BatUTF8.t = valuee

    method tag = tag
    method coords = coords
    method value = value

    method to_string =
        if ttag == End_of_program then
            "EOF token " ^ coords#to_string ^ ": " ^ value
        else 
            "Unknown token " ^ coords#to_string ^ ": " ^ value

    end;;