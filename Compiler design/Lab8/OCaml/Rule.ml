open Batteries;;
open DomainTag;;


type tag = Normal | Star | Error | Tok;;


class rule tag_ = object (self)
    val mutable tag: tag = tag_
    val mutable alternatives: (rule BatList.t) BatList.t = []
    val mutable elems: rule BatList.t = []
    val mutable tokens: Token.token BatList.t = []

    method tag = tag
    method token = (BatList.at tokens 0)
    method alternatives = alternatives

    method new_alternative =
        alternatives <- alternatives@[elems];
        elems <- []

    method new_tok tok =
        tokens <- tokens@[tok]

    method new_elem_rule el =
        elems <- elems@[el]

    method new_elem_tok tok =
        let el = new rule Tok in
        el#new_tok tok;
        self#new_elem_rule el
        

    (*method debug =
        let str = ref "" in
        let list_str = ref [] in
        BatList.iter (fun m -> (BatList.iter (fun mm -> (str := !str ^ mm#to_string ^ " ")) m); list_str := !list_str@[!str]; str := "") alternatives;
        str := !str ^ (BatString.concat "| " !list_str);
        print_string ("Alternatives: " ^ !str);
        print_string "\n";
        let str2 = ref "" in
        BatList.iter (fun mm -> (str2 := !str2 ^ mm#to_string ^ " ")) elems;
        print_string ("Elems: " ^ !str2);
        print_string "\n"; *)
        
    
    method to_string =
        let begin_str =
            match self#tag with
                | Normal -> "("
                | Star -> "{"
                | Error -> "/*"
                | Tok -> ""
            in
        let end_str = 
            match self#tag with
                | Normal -> ")"
                | Star -> "}"
                | Error -> "*/"
                | Tok -> ""
            in
        let str = ref " " in
        let list_str = ref [] in
        if (BatList.length tokens) != 0 then
            str := !str ^ (BatList.at tokens 0)#value;
        for i = 0 to ((BatList.length alternatives) - 1) do
            for j = 0 to ((BatList.length (BatList.at alternatives i)) - 1) do
                str := !str ^ (BatList.at (BatList.at alternatives i) j)#to_string ^ " ";
            done;
            list_str := !list_str@[!str];
            str := " ";
        done;
        (* BatList.iter (fun m -> (BatList.iter (fun mm -> (str := !str ^ mm#to_string ^ " ")) m); list_str := !list_str@[!str]; str := " ") alternatives; *)
        str := !str ^ begin_str;
        str := !str ^ (BatString.concat "|" !list_str);
        str := !str ^ end_str;
        !str
        
    end;;     
