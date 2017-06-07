open Batteries;;


type pos = {
     text: BatUTF8.t;
     mutable line: BatInt.t;
     mutable pos: BatInt.t;
     mutable index: BatInt.t;
}

let init_pos str = {text = str; line = BatInt.one; pos = BatInt.one;index = BatInt.zero;}
let create_pos str l p ind = {text = str; line = l; pos = p;index = ind;}

class position init = object (self)
    val mutable cur : pos = init

    method get_text = cur.text
    method get_line = cur.line
    method get_pos = cur.pos
    method get_index = cur.index

    method set_line l = cur.line <- l
    method set_pos p = cur.pos <- p
    method set_index i = cur.index <- i

    method to_string = "(" ^ (BatInt.to_string(cur.line)) ^ " ," ^ (BatInt.to_string(cur.pos)) ^ ")"

    method is_eof = cur.index == BatString.length(cur.text)
    method get_code =
        match self#is_eof with
          | false ->  BatChar.code(cur.text.[cur.index])
          | true  -> -1

    method get_letter =
        match self#is_eof with
          | false -> BatChar.chr (BatChar.code(cur.text.[cur.index]))
          | true  -> ' '

    method is_whitespace =
        not self#is_eof && BatChar.is_whitespace(cur.text.[cur.index])
    method is_decimaldigit =
        not self#is_eof && BatChar.is_digit(cur.text.[cur.index])
    method is_letter =
        not self#is_eof && BatChar.is_letter(cur.text.[cur.index])
    method is_newline =
	    if self#is_eof then
		    true
	    else if cur.text.[cur.index] == '\r' && cur.index+1 < String.length(cur.text) then
		    cur.text.[cur.index+1] == '\n'
        else
		    cur.text.[cur.index] == '\n' 

   
    method next =
        let new_pos = ref (create_pos cur.text cur.line cur.pos cur.index) in
        let p = ref (new position !new_pos) in 
        if not (!p#is_eof) then (
			if !p#is_newline then (
				if !p#get_text.[!p#get_index] == '\r' then
					!p#set_index (!p#get_index+1);
				!p#set_line (!p#get_line + 1);
				!p#set_pos 1;
            )
			else (
                if not (BatUChar.is_ascii(BatUTF8.look !p#get_text !p#get_index)) then
                    !p#set_index (!p#get_index+1);
                !p#set_pos (!p#get_pos+1);	
            );
            !p#set_index (!p#get_index+1);
        ); 
        !p
    end;;

