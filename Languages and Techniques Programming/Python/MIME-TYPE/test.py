#!/usr/bin/python3
# -*- coding: utf-8 -*-
import os
import mime

if __name__=="__main__":
    dir = sorted(os.listdir(os.getcwd()))
    files = list(filter(lambda name: True if not os.path.isdir(os.path.join(os.getcwd(),name)) else False,dir))
    for file in files:
        path = os.path.join(os.getcwd(),file)
        print (file,end="\n")
        mime.mime_type(path)
