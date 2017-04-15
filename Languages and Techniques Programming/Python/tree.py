#!/usr/bin/python3
# -*- coding: utf-8 -*-
import argparse
import os
import sys
__author__="great"

files = 0
dirs = 0

def new_parser():
    parser = argparse.ArgumentParser(description="My version of utilit tree")
    parser.add_argument('-d',dest="DIR", action='store_true',help="List only directories")
    parser.add_argument('-o',dest="OUT", nargs=1, help="Send output to filename",default = sys.stdout)
    parser.add_argument('path', type=str,nargs = "*", help = "Specified directory")
    return parser

def tree(path,file,flag,separator):
    global files
    global dirs
    current_dir = path
    if os.access(current_dir,os.R_OK):
        sorted_current_dir = list(filter(lambda name: True if not flag or os.path.isdir(os.path.join(current_dir,name)) else False,sorted(os.listdir(current_dir))))
    else:
        return file.write(" " + "[error opening dir]\n")
    for name in sorted_current_dir:
        if name.startswith('.'):
            continue
        if name == sorted_current_dir[len(sorted_current_dir)-1]:
            vertical_sep="    "
            horizontal_sep = "└──"
        else:
            vertical_sep="│   "
            horizontal_sep = "├──"
        file_name = separator + horizontal_sep + name
        path = os.path.join(current_dir,name)
        if os.path.isdir(path) == False and not flag:
            files+=1
            file.write(file_name + "\n")
        elif os.path.isdir(path) and os.access(path,os.R_OK):
            dirs +=1
            file.write(file_name + "\n")
            tree(path,file,flag,separator+vertical_sep)

if __name__=="__main__":
    arg_parse = new_parser()
    args = arg_parse.parse_args()
    file = args.OUT
    dir = None
    if args.OUT != sys.stdout:
        file = open(args.OUT.pop(), 'w')
    if len(args.path) == 0:
        file.write("." + "\n")
        tree(os.getcwd(),file,args.DIR,"")
    else:
        while len(args.path)>0:
            dir = args.path.pop()
            file.write(dir + "\n")
            tree(dir,file,args.DIR,"")
    if args.DIR == False:
        file.write(str(dirs) + " directories, " + str(files) + " files")
    else:
        file.write(str(dirs) + " directories")
    file.close()
