(* print_endline "Hello world!\n" ;;
   1 + 1 ;;
   let average a b = (a +. b) /. 2.0 ;;
   average 5.0 2.0 ;; 
*)

#load "graphics.cma";;

open Graphics;;
open Printf;;

open_graph " 640x480";;
for i = 12 downto 1 do
  let radius = i * 20 in
    printf "radius is %d\n" radius;
    set_color (if (i mod 2) = 0 then red else yellow);
    fill_circle 320 240 radius
done;;
read_line ();;
