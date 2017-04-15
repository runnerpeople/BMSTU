#!/usr/bin/python3
# -*- coding: utf-8 -*-
import argparse
import sys
__author__="great"



def new_parser():
    parser = argparse.ArgumentParser(description="My version of utilit wc")
    parser.add_argument('-c',"--bytes", action='store_true',help="Print the byte counts")
    parser.add_argument('-w',"--words", action='store_true', help="Print the word counts")
    parser.add_argument('-m',"--char", action='store_true', help = "Print the character counts")
    parser.add_argument('-l',"--lines", action='store_true',help="Print the newlines counts")
    parser.add_argument("files", nargs="*", default=sys.stdin)
    return parser

def wc(files):
    file = None
    if files == sys.stdin:
        file = files
    else:
        try:
            file = open(files)
        except IOError:
            sys.stderr.write(files + ": отказано в доступе\n")
            return None
    count = [0,0,0,0]
    buffer = file.read()
    lines = buffer.count("\n")
    count[0] = lines
    words = len(buffer.split())
    count[1] = words
    bytes = len(buffer.encode("utf-8"))
    count[2] = bytes
    chars = len(buffer)
    count[3] = chars
    file.close()
    return count

if __name__=="__main__":
    arg_parse=new_parser()
    args = arg_parse.parse_args()
    total = [0,0,0,0]
    if args.files == sys.stdin:
        total = wc(args.files)
    else:
        for x in args.files:
            result = wc(x)
            if result == None:
                continue
            answer = []
            if args.bytes:
                answer.append(str(result[2]))
                total[2] += result[2]
            if args.words:
                answer.append(str(result[1]))
                total[1] += result[1]
            if args.char:
                answer.append(str(result[3]))
                total[3] += result[3]
            if args.lines:
                answer.append(str(result[0]))
                total[0] += result[0]
            if not args.bytes and not args.words and not args.char and not args.lines:
                answer = [result[0],result[1],result[2]]
                i = 0
                for element in answer:
                    total[i] += result[i]
                    i = i + 1
                i = 0
                for element in answer:
                    if i == 3:
                        break
                    print(" " + str(answer[i]),end='')
                    i = i + 1
                print(" " + x)
            else:
                i = 0
                for element in answer:
                    print(" " + str(answer[i]),end='')
                    i = i + 1
                print(" " + x)
    if args.files == sys.stdin or len(args.files) > 1:
        if args.bytes:
            print(" "+str(total[2]),end='')
        if args.words:
            print(" "+str(total[1]),end='')
        if args.char:
            print(" "+str(total[3]),end='')
        if args.lines:
            print(" "+str(total[0]),end='')
        if not args.bytes and not args.words and not args.char and not args.lines:
            i = 0
            for element in total:
                if i==3:
                    break
                print(" " + str(total[i]),end='')
                i = i + 1
        if args.files != sys.stdin:
            print(" итого")
