open Batteries;;
open DomainTag;;

class lexer programm = object (self)
    val mutable program: BatUTF8.t = programm
    val mutable cur: Position.position = new Position.position (Position.init_pos programm)

    method cur = cur

    method read_nonterm (pos : Position.position) =
        let new_pos = ref (Position.create_pos pos#get_text pos#get_line pos#get_pos pos#get_index) in
        let p = ref (new Position.position !new_pos) in
        let s = ref "" in
        while (!p#is_letter) do
            s := !s ^ BatChar.escaped !p#get_letter;
            p := !p#next;
        done;
        new NonTermToken.token cur !p !s

    method read_term (pos : Position.position) =
        let new_pos = ref (Position.create_pos pos#get_text pos#get_line pos#get_pos pos#get_index) in
        let p = ref (new Position.position !new_pos) in
        let s = ref "" in
        s := !s ^ BatChar.escaped !p#get_letter;
        p := !p#next;
        while (not !p#is_eof && !p#get_letter != '\"' && !p#get_letter != '\r' && !p#get_letter != '\n') do
            s := !s ^ BatChar.escaped !p#get_letter;
            p := !p#next;
        done;
        if !p#get_letter == '\"' then begin
            s := !s ^ BatChar.escaped !p#get_letter;
            new TermToken.token cur !p#next !s
        end
        else if (!p#is_eof) then begin
            Batteries.Printf.eprintf "end of program found, \" expected\n";
            new UnknownToken.token Unknown cur !p !s
        end
        else if !p#get_letter == '\r' || !p#get_letter == '\n'then begin
            Batteries.Printf.eprintf "Words don't contains whitespaces, \" expected\n";
            new UnknownToken.token Unknown cur !p !s
        end
        (* this isn't necessary, but compiler uses automatic type deduction, so you must have 'else' - statement *)
        else 
            new UnknownToken.token Unknown cur !p !s

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
                    match cur#get_letter with
                        | '\"'                                            -> self#read_term cur 
                        | 'a'..'z' | 'A'..'Z'                             -> self#read_nonterm cur
                        | '(' | ')' | ',' | '{' | '}'                     -> new SpecialToken.token cur cur#next (BatChar.escaped cur#get_letter)
                        | _                                               -> new UnknownToken.token Unknown cur cur ""
                    in
                    if (token#tag == Unknown) then begin
                        Batteries.Printf.eprintf "Token: unrecognized token\n";
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
    
