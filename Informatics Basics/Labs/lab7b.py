#!/usr/bin/env python3
# -*- coding: utf-8 -*-
__author__="great"
import sys
try:
    import count_letter
except ImportError:
    sys.stderr.write("Error in import count_letter")

def file_count(filepath):
    try:
        with open(filepath,mode="r") as file:
            text = file.read()
            dict_ = count_letter.count(text)
            for key in sorted(dict_):
                sys.stdout.write(key + " - " + str(dict_[key]) + "\n")
    except Exception as exception:
        sys.stderr.write(exception.args)
if len(sys.argv)>1:
    file_count(sys.argv[1])
else:
    sys.stderr.write("Null sys.argv")
