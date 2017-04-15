#!/usr/bin/python3
# -*- coding: utf-8 -*-
import argparse
import math
import sys
__author__="great"

def create_parser():
    parser = argparse.ArgumentParser(description="Square or root of number")
    parser.add_argument('-r',type = int, dest="ROOT", nargs=1, help="Root of number")
    parser.add_argument('-s',type = int, dest="SQUARE",nargs=1, help="Square of number")
    return parser

if __name__=="__main__":
    arg_parse = create_parser()
    args = arg_parse.parse_args()
    if args.ROOT != None:
        buf = args.ROOT.pop()
        answer_root = math.sqrt(buf + 0.0)
        print("Root of number " + str(buf) + " is",end=' ')
        print(answer_root)
        sys.exit(0)
    elif args.SQUARE != None:
        buf = args.SQUARE.pop()
        answer_square = buf ** 2
        print("Square of number " + str(buf) + " is",end=' ')
        print(answer_square)
        sys.exit(0)
    else:
        print("Error in parse argument")

