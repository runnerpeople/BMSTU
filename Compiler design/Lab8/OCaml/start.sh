#!/bin/sh

ocamlfind ocamlc -linkpkg -thread -package batteries Position.ml Fragment.ml DomainTag.ml Token.ml NonTermToken.ml TermToken.ml SpecialToken.ml UnknownToken.ml Lexer.ml Rule.ml Parser.ml Algorithm.ml Main.ml -o prog.byte
./prog.byte test4.txt
