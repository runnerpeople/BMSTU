open Batteries;;
open DomainTag;;


class scanner programm compileer = object (self)
    
    val program: BatUTF8.t = programm
    val compiler: Compiler.compiler = compileer
    val mutable cur: Position.position = new Position.position (Position.init_pos programm)

    method cur = cur
  

    method read_comment (pos : Position.position) =
        let new_pos = ref (Position.create_pos pos#get_text pos#get_line pos#get_pos pos#get_index) in
        let p = ref (new Position.position !new_pos)#next#next in
        let s = ref "" in
        while (not !p#is_newline) do
            s := !s ^ BatChar.escaped (BatChar.chr !p#get_code);
            p := !p#next;
        done;
        new CommentToken.token cur !p#next !s

    method read_label (pos : Position.position) =
        let new_pos = ref (Position.create_pos pos#get_text pos#get_line pos#get_pos pos#get_index) in
        let p = ref (new Position.position !new_pos)#next in
        let s = ref "" in
        if (!p#is_decimaldigit || !p#is_letter) then (
            while (!p#is_decimaldigit || !p#is_letter) do
                s := !s ^ BatChar.escaped (BatChar.chr !p#get_code);
                p := !p#next;
            done;
            new LabelToken.token cur !p#next !s;
        )
        else (
            compiler#add_message false !p "Label must start only with numbers or letters\n";
            new UnknownToken.token Unknown cur !p#next !s;
        );

    method read_word (pos : Position.position) (flag: bool) =
        if (not flag) then (
            let new_pos = ref (Position.create_pos pos#get_text pos#get_line pos#get_pos pos#get_index) in
            let p = ref (new Position.position !new_pos)#next in
            let s = ref "" in
            s := !s ^ BatChar.escaped (BatChar.chr pos#get_code);
            while (not !p#is_eof && not !p#is_whitespace) do
                s := !s ^ BatChar.escaped (BatChar.chr !p#get_code);
                p := !p#next;
            done;
            new WordToken.token cur !p !s
        )
        else (
            let new_pos = ref (Position.create_pos pos#get_text pos#get_line pos#get_pos pos#get_index) in
            let p = ref (new Position.position !new_pos)#next in
            let s = ref "" in
            while (not !p#is_eof && (BatChar.chr !p#get_code != '\"') && (BatChar.chr !p#get_code != '\r') && (BatChar.chr !p#get_code != '\n')) do
                s := !s ^ BatChar.escaped (BatChar.chr !p#get_code);
                p := !p#next;
            done;
            if (BatChar.chr !p#get_code == '\"') then
                new WordToken.token cur !p#next !s
            else if (!p#is_eof) then begin
                compiler#add_message false !p "end of program found, \" expected\n";
                new UnknownToken.token Unknown cur !p !s
            end
            else if ((BatChar.chr !p#get_code == '\r') || (BatChar.chr !p#get_code == '\n')) then begin
                compiler#add_message false !p "Words don't contains whitespaces, \" expected\n";
                new UnknownToken.token Unknown cur !p !s
            end
            (* this isn't necessary, but compiler uses automatic type deduction, so you must have 'else' - statement *)
            else 
                new UnknownToken.token Unknown cur !p !s
        );
   
    method next_token =
        let tok = ref (new UnknownToken.token Unknown cur cur "") in
        while !tok#tag == Unknown do
            while cur#is_whitespace do
                cur <- cur#next
            done;
            if cur#is_eof then
                tok := (new UnknownToken.token End_of_program cur cur "")
            else begin
                let token =
                    match BatChar.chr cur#get_code with
                        | ':' when cur#next#get_code == BatChar.code(':') -> self#read_comment cur 
                        | ':'                                             -> self#read_label cur
                        | '\"'                                            -> self#read_word cur true
                        | _                                               -> self#read_word cur false
                    in
                    if (token#tag == Unknown) then begin
                        compiler#add_message true cur "Token: unrecognized token\n";
                        cur <- cur#next;
                    end
                    else begin
                        cur <- token#coords#following;
                        tok := (token :> Token.token);               
                    end;
            end
        done;
        !tok
    end;;
