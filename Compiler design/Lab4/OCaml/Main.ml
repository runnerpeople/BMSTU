open Batteries;;
open DomainTag;;

let strip_last_char str =
    if str = "" then "" else
    BatString.sub str 0 ((BatString.length str) - 1)

let compiler = new Compiler.compiler;;
let file = BatFile.open_in BatSys.argv.(1);;
let program = ref (BatIO.read_all file);;
program := strip_last_char !program;;
BatIO.close_in file;;
let scanner = new Scanner.scanner (BatUTF8.of_string_unsafe !program) compiler;;
print_string "TOKENS\n";;
(* let token = scanner#read_word scanner#cur true;;
print_string (token#to_string ^ "\n"); *)

let token = ref scanner#next_token;;
while (!token#tag != End_of_program) do
    print_string (!token#to_string ^ "\n");
    token := scanner#next_token;
done;;


if (compiler#errors) then begin
    print_string "MESSAGES\n";
    compiler#output_messages
end;;

