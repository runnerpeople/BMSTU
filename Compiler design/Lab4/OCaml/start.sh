#!/bin/sh

ocamlfind ocamlc -linkpkg -thread -package batteries Position.ml Fragment.ml DomainTag.ml Token.ml WordToken.ml LabelToken.ml UnknownToken.ml CommentToken.ml Message.ml Compiler.ml Scanner.ml Main.ml -o prog.byte
./prog.byte
