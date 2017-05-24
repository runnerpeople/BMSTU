open Batteries;;

class compiler = object (self)
    
    val mutable messages: Message.message BatList.t = []
    val mutable nameCodes: (string,int) BatHashtbl.t = Hashtbl.create 1000;
    val mutable names: BatUTF8.t BatList.t = []

    method add_name name =
        if BatHashtbl.mem nameCodes name then begin
            BatHashtbl.find nameCodes name;
        end
        else begin
            names <- names@[name];
            BatHashtbl.add nameCodes name ((BatList.length names) - 1);
            (BatList.length names) - 1
        end

    method errors =
        not (BatList.is_empty messages)

    method get_name code =
        BatList.at names code

    method add_message is_err pos text =
        messages <- messages@[new Message.message is_err text pos]

    method output_messages =
        BatList.iter (fun m -> Printf.printf "%s: %s %s" (if m#get_error then "Error" else "Warning") m#get_position#to_string m#get_text) messages

    (* method get_scanner (program: BatUTF8.t) = 
        new Scanner.scanner program self *)

    end;;
  
