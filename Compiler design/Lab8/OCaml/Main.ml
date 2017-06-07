open Batteries;;
open DomainTag;;

let file = BatFile.open_in BatSys.argv.(1);;
let program = ref (BatIO.read_all file);;
BatIO.close_in file;;
let lex = new Lexer.lexer (BatUTF8.of_string_unsafe !program);;

let token = ref lex#next_token;;
let list_tokens = ref [];;
list_tokens := !list_tokens@[!token];;
(* print_string (!token#to_string ^ "\n"); *)
while (!token#tag != End_of_program) do
    token := lex#next_token;
    list_tokens := !list_tokens@[!token];
    (* print_string (!token#to_string ^ "\n"); *)
done;; 
let parser = new Parser.parser !list_tokens;;
parser#parse;;
if not parser#is_error then begin
    parser#rules_to_string;
    let alg = new Algorithm.algorithm parser#map_rule in
    alg#calc_first;
    alg#print_first;
end;;
