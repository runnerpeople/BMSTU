open Batteries;;
open DomainTag;;
open Rule;;


class parser tokens_list = object (self)
    val mutable tokens: Token.token BatList.t = BatList.tl tokens_list
    val mutable token: Token.token = BatList.hd tokens_list
    val mutable terms: BatUTF8.t  BatList.t = []
    val mutable is_error: bool = false

    val mutable nterms_left: BatUTF8.t BatSet.t = BatSet.empty;
    val mutable nterms_right: BatUTF8.t BatSet.t = BatSet.empty;

    val mutable map_rule: (BatUTF8.t, Rule.rule) BatHashtbl.t = Hashtbl.create 1000;
    

    val mutable is_rule: bool = true

    method is_error = is_error
    method map_rule = map_rule

    method next_token =
        token <- BatList.hd tokens;
        tokens <- BatList.tl tokens

    method rules_to_string =
        BatHashtbl.iter (fun key value -> Batteries.Printf.printf "%s %s\n" key value#to_string) map_rule

    (* Rp = NonTerm | Term | "{" Rp { Rp } "}" | Alt *)
    method parse_rp (cur_rule : Rule.rule) =
        let ret_rule = ref cur_rule in
        if token#tag == NonTerm then begin
            nterms_right <- BatSet.add token#value nterms_right;
            !ret_rule#new_elem_tok token;
            self#next_token;
        end
        else if token#tag == Term then begin
            terms <- terms@[token#value];
            !ret_rule#new_elem_tok token;
            self#next_token;
        end
        else if token#tag == Special && (BatUTF8.compare token#value "{") == 0 then begin
            let sub_rule = ref (new Rule.rule Star) in
            self#next_token;
            sub_rule := self#parse_rp !sub_rule;
            while token#tag == NonTerm || token#tag == Term || (token#tag == Special && (BatUTF8.compare token#value "{") == 0) || (token#tag == Special && (BatUTF8.compare token#value "(") == 0) do
                sub_rule := self#parse_rp !sub_rule;
            done;
            if token#tag == Special && (BatUTF8.compare token#value "}") == 0 then begin
                self#next_token;
                !sub_rule#new_alternative;
                !ret_rule#new_elem_rule !sub_rule;
            end
            else begin
                is_error <- true;
                Batteries.Printf.eprintf "Error. Expected Token \"Special\" - \"}\"\n";
                ret_rule := new Rule.rule Error;
            end;
        end
        else if token#tag == Special && (BatUTF8.compare token#value "(") == 0 then begin
            ret_rule := self#parse_alt !ret_rule; 
        end;
        !ret_rule

    (* Alt = "(" Rp { Rp } { "," Rp { Rp } } ")" *)
    method parse_alt (cur_rule : Rule.rule) = 
        if token#tag == Special && (BatUTF8.compare token#value "(") == 0 then begin 
            let sub_rule = ref (new Rule.rule Error) in
            let cur_ref_rule = ref cur_rule in
            if not is_rule then
                sub_rule := new Rule.rule Normal
            else
                is_rule <- false;
            self#next_token;
            if !sub_rule#tag != Error then begin
                sub_rule := self#parse_rp !sub_rule; 
            end
            else begin
                cur_ref_rule := self#parse_rp !cur_ref_rule
            end;
            while token#tag == NonTerm || token#tag == Term || (token#tag == Special && (BatUTF8.compare token#value "{") == 0) || (token#tag == Special && (BatUTF8.compare token#value "(") == 0) do
                if !sub_rule#tag != Error then
                    sub_rule := self#parse_rp !sub_rule
                else
                    cur_ref_rule := self#parse_rp !cur_ref_rule;
            done;
            while (token#tag == Special && (BatUTF8.compare token#value ",") == 0) do
                self#next_token;          
                if !sub_rule#tag != Error then begin
                    !sub_rule#new_alternative;
                    sub_rule := self#parse_rp !sub_rule;
                end
                else begin
                    !cur_ref_rule#new_alternative;
                    cur_ref_rule := self#parse_rp !cur_ref_rule;
                end;
                while token#tag == NonTerm || token#tag == Term || (token#tag == Special && (BatUTF8.compare token#value "{") == 0) || (token#tag == Special && (BatUTF8.compare token#value "(") == 0) do
                    if !sub_rule#tag != Error then
                        sub_rule := self#parse_rp !sub_rule
                    else
                        cur_ref_rule := self#parse_rp !cur_ref_rule;
                done;
            done;
            if token#tag == Special && (BatUTF8.compare token#value ")") == 0 then begin
                self#next_token;
                if !sub_rule#tag != Error then begin
                    !sub_rule#new_alternative;
                    !cur_ref_rule#new_elem_rule !sub_rule;
                end
                else
                    !cur_ref_rule#new_alternative;
                !cur_ref_rule;
            end
            else begin
                is_error <- true;
                Batteries.Printf.eprintf "Error. Expected Token \"Special\" - \")\"\n";
                new Rule.rule Error;
            end;
        end
        else begin
            is_error <- true;
            Batteries.Printf.eprintf "Error. Expected Token \"Special\" - \"(\"\n";
            new Rule.rule Error;
        end

    (* Expr = NonTerm Alt *)
    method parse_expr =
        if token#tag == NonTerm then begin
            nterms_left <- BatSet.add token#value nterms_left;
            let cur_rule = ref (new Rule.rule Normal) in
            let nterm = token#value in
            self#next_token;
            cur_rule := self#parse_alt !cur_rule;
            BatHashtbl.add map_rule nterm !cur_rule;
            is_rule <- true;
        end
        else begin
            is_error <- true;
            Batteries.Printf.eprintf "Error. Expected Token \"NonTerm\"\n";
        end
                
    (* Exprs = Expr { Expr } *)
    method parse_exprs =
        self#parse_expr;
        while token#tag == NonTerm do
            self#parse_expr
        done
    

    method parse = 
        self#parse_exprs;
        if token#tag != End_of_program then begin
            is_error <- true;
            Batteries.Printf.eprintf "Error. Expected Token \"End_Of_Program\"\n";
        end
        else
            if BatSet.subset nterms_right nterms_left then
                Batteries.Printf.printf "OK. Program satisfy grammar\n"
            else begin
                let undefined = BatSet.diff nterms_right nterms_left in 
                BatSet.iter (fun m -> Batteries.Printf.eprintf "%s " m) undefined;
                Batteries.Printf.eprintf "- undefined\n";
                Batteries.Printf.eprintf "Error. Undefined nterms in right side rule\n";
            end

    end;;
