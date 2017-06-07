open Batteries;;
open DomainTag;;
open Rule;;

class algorithm map_rule = object (self)
    val mutable map_rule: (BatUTF8.t, Rule.rule) BatHashtbl.t = map_rule
    val mutable map_first: (BatUTF8.t, BatUTF8.t BatSet.t) BatHashtbl.t = Hashtbl.create 1000;

    method f (rule : Rule.rule) =
        let set = ref BatSet.empty in
        BatList.iter (fun lst -> 
                let alt_set = ref BatSet.empty in
                let hs = ref BatSet.empty in
                let flag = ref true in
                (
                BatList.iter (fun el -> (
                    if !flag then begin
                        if el#tag == Normal then begin
                            hs := self#f el;
                            flag := true;
                        end
                        else if el#tag == Star then
                            hs := self#f el
                        else if el#tag == Tok && el#token#tag == Term then
                            hs := BatSet.add el#token#value !hs
                        else if el#tag == Tok && el#token#tag == NonTerm then
                            hs := BatHashtbl.find map_first el#token#value;
                    BatSet.iter (fun el -> alt_set := (BatSet.add el !alt_set)) !hs;
                    flag := false;
                    end                   
                    )
                )) lst;
                BatSet.iter (fun el -> set := BatSet.add el !set) !alt_set;
            ) rule#alternatives;
        !set
        
            
        

    method calc_first =
        BatHashtbl.iter (fun key value -> (BatHashtbl.add map_first key BatSet.empty;)) map_rule;
        let is_changed = ref true in
        let hs_buf = ref BatSet.empty in
        while !is_changed do
            let len_before = ref 0 in
            BatHashtbl.iter (fun key value -> (
                len_before := BatList.length (BatSet.elements (BatHashtbl.find map_first key));
                is_changed := false;
                hs_buf := self#f value;
                BatHashtbl.replace map_first key !hs_buf; 
                if !len_before != BatList.length (BatSet.elements (BatHashtbl.find map_first key)) then
                    is_changed := true;
                )) map_rule;
        done

    method print_first =
        BatHashtbl.iter (fun key value -> (Printf.printf "%s :: %s\n" key (BatString.concat " , " (BatSet.to_list value)))) map_first


    end;;
