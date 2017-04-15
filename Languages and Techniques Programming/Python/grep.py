#!/usr/bin/python3
# -*- coding: utf-8 -*-
import argparse
import sys
import os
import re
__author__="great"


def new_parser():
    parser = argparse.ArgumentParser(description="My version of utilit grep")
    parser.add_argument('-n',"--line-number", action='store_true',help="Print line number")
    parser.add_argument('-i',"--ignore-case", action='store_true', help="Ignore case")
    parser.add_argument('-m',"--max-count", type = int, help = "Limit finds",default = None)
    parser.add_argument('-e',"--regexp-Pattern", action='store_true',help="Use regex expressions")
    parser.add_argument("pattern", help = "Search pattern")    
    parser.add_argument("files", nargs="*", default=sys.stdin)   
    return parser

def grep(files,pattern,flag_regex,flag_ignore,flag_count,flag_number,files_name):
    if flag_ignore:
        pattern = pattern.lower()
    str_count = 0
    while flag_count == None or flag_count >0:
        str_count +=1
        scan = files.readline()
        if len(scan)==0:
            break
        if flag_ignore:
            buf = scan.lower()
        else:
            buf = scan
        if scan.find(pattern)!=-1 or flag_regex and re.search(pattern,buf) != None:
            if flag_count != None:
                flag_count-=1
            scan = scan.replace("\n","")
            if files_name:
                print(files + ":",end='')
            if flag_number:
                print(str(str_count) + ":",end='')
            print(scan)


if __name__=="__main__":
    arg_parse=new_parser()
    args=arg_parse.parse_args()
    if args.files == sys.stdin:
            grep(sys.stdin,args.pattern,args.regexp_Pattern,args.ignore_case,args.max_count,args.line_number,False)
    else:
        for file in args.files:
            if len(args.files)>1:
                print_=True
            else:
                print_=False
            if os.access(file,os.R_OK):
                files = open(file,'r')
                grep(files,args.pattern,args.regexp_Pattern,args.ignore_case,args.max_count,args.line_number,print_)
                files.close()
            else:
                sys.stderr.write("Недостаточно прав для доступа к файлу " + file + "\n")
