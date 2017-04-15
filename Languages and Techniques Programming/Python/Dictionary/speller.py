#!/usr/bin/python3
# -*- coding: utf-8 -*-
import argparse
import sys
import scanner
__author__="great"

def create_parser():
    parser = argparse.ArgumentParser()
    parser.add_argument('files', type=str,nargs = "+", help = "Dictionary and file")
    return parser

if __name__=="__main__":
    arg_parse = create_parser()
    args = arg_parse.parse_args()
    dict = args.files[0]
    file = args.files[1]
    lexems = scanner.dictionary_help(file)
    if lexems == None:
        print("Проверяемый текст пуст")
        sys.exit(1)
   # for word in lexems:
   #     print(word.get_lexem(),end='   ')
    words=scanner.dictionary_help(dict)
    dictionary=set()
    for word in words:
        dictionary.add(word.get_lexem())
    for word in lexems:
        if not word.get_lexem() in dictionary and not word.get_lexem().lower() in dictionary:
            print(str(word.get_num())+",  " + str(word.get_col())+"\t"+word.get_lexem())

