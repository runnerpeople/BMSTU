open Batteries;;
open DomainTag;;


class scanner programm compileer = object (self)
    
    val program: BatUTF8.t = programm
    val compiler: Compiler.compiler = compileer
    val mutable cur: Position.position = new Position.position (Position.init_pos programm)
  

    method read_comment (pos : Position.position) =
        let p = ref pos#next#next in
        let s = ref "" in
        while (not !p#is_newline) do
            s := !s ^ BatChar.escaped (BatChar.chr !p#get_code);
            p := !p#next;
        done;
        new CommentToken.token cur !p#next !s

    method read_label (pos : Position.position) =
        let p = ref pos#next in
        let s = ref "" in
        if (!p#is_decimaldigit || !p#is_letter) then (
            while (!p#is_decimaldigit || !p#is_letter) do
                s := !s ^ BatChar.escaped (BatChar.chr !p#get_code);
                p := !p#next;
            done;
            new LabelToken.token cur !p#next !s
        )
        else (
            compiler#add_message false !p "Label must start only with numbers or letters";
            new UnknownToken.token Unknown cur !p#next !s;
        );

    method read_word (pos : Position.position) (flag: bool) =
        if (flag) then (
            let p = ref pos#next in
            let s = ref "" in
            s := !s ^ BatChar.escaped (BatChar.chr !p#get_code);
            while (not !p#is_eof && not !p#is_whitespace) do
                s := !s ^ BatChar.escaped (BatChar.chr !p#get_code);
                p := !p#next;
            done;
            new WordToken.token cur !p#next !s
        )
        else (
            let p = ref pos#next in
            let s = ref "" in
            while (not !p#is_eof && (BatChar.chr !p#get_code == '\"') && (BatChar.chr !p#get_code == '\r') && (BatChar.chr !p#get_code == '\n')) do
                s := !s ^ BatChar.escaped (BatChar.chr !p#get_code);
                p := !p#next;
            done;
            if (BatChar.chr !p#get_code == '\"') then
                new WordToken.token cur !p#next !s
            else if ((BatChar.chr !p#get_code == '\r') || (BatChar.chr !p#get_code == '\n')) then begin
                compiler#add_message false !p "Words don't contains whitespaces, \" expected";
                new UnknownToken.token Unknown cur !p !s
            end
            else begin
                compiler#add_message false !p "end of program found, \" expected";
                new UnknownToken.token Unknown cur !p !s
            end
        );
   
    method next_token =
        let tok = ref (new UnknownToken.token Unknown cur cur "") in
        while !tok#tag != End_of_program do
            while not cur#is_eof do
                while cur#is_whitespace do
                    cur <- cur#next
                done;
                let token =
                    match BatChar.chr cur#get_code with
                        | ':' when cur#next#get_code == BatChar.code(':') -> self#read_comment cur 
                        | ':'                                             -> self#read_label cur
                        | '\"'                                            -> self#read_word cur true
                        | _                                               -> self#read_word cur false
                in
                if (token#tag == Unknown) then begin
                    compiler#add_message true cur "Token: unrecognized token";
                    cur <- cur#next;
                end
                else begin
                    cur <- token#coords#following;
                    tok := (token :> Token.token);                
                end 
            done;
            if cur#is_eof then
                tok := (new UnknownToken.token End_of_program cur cur "");
        done;
        !tok
    end;;


