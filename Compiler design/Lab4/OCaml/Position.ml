open Batteries;;


type pos = {
     text: BatUTF8.t;
     mutable line: BatInt.t;
     mutable pos: BatInt.t;
     mutable index: BatInt.t;
}

let init_pos str = {text = str; line = BatInt.one; pos = BatInt.one;index = BatInt.zero;}

class position init = object (self)
    val mutable cur : pos = init

    method get_text = cur.text
    method get_line = cur.line
    method get_pos = cur.pos
    method get_index = cur.index

    method to_string = "(" ^ (BatInt.to_string(cur.line)) ^ " ," ^ (BatInt.to_string(cur.pos)) ^ ")"

    method is_eof = cur.index == BatUTF8.length(cur.text)
    method get_code =
        match self#is_eof with
          | false ->  BatChar.code(cur.text.[cur.index])
          | true  -> -1
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
        if not self#is_eof then (
			if self#is_newline then (
				if cur.text.[cur.index] == '\r' then
					cur.index <- cur.index+1;
				cur.line <- cur.line + 1;
				cur.pos <- 1;
            )
			else (
                if not (BatUChar.is_ascii(BatUTF8.look cur.text cur.index)) then
                    cur.index <- cur.index+1;
                cur.pos <- cur.pos+1;	
            );
        ); 
        self
    end;;

