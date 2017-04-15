#!/usr/bin/env python3
# -*- coding: utf-8 -*-
__author__="great"

def count(string):
    string = string.lower()
    dict_keys = {}
    for i in range(len(string)):
        if string[i].isspace() or string[i].isdigit():
            continue
        if string[i] in dict_keys:
            dict_keys[string[i]]+=1
        elif string[i].isalpha():
            dict_keys[string[i]]=1
    return dict_keys
