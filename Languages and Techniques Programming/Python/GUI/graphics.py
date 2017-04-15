#!/usr/bin/python3
# -*- coding: utf-8 -*-
import tkinter
import math
__author__ = 'great'

class Graphic:

    def __init__(self, x_min, x_max, width, height, func):
        self.x_min = x_min
        self.x_max = x_max
        self.width = width
        self.height = height
        self.f = func
        self.kx = width/(x_max-x_min)
        self.arg_list = []
        x = self.x_min
        i = 1
        delta = 1/self.kx
        while x <= self.x_max:
            self.arg_list.append(x)
            x = self.x_min + delta * i
            i += 1
        self.dictionary={}
        for item in self.arg_list:
            self.dictionary[item]=self.safe_func(item)
        for key,value in self.dictionary.items():
            if self.dictionary[key]==None:
                self.dictionary.pop(key)
        self.y_min = min(self.dictionary.items(),key=lambda x:x[1])[1]
        self.y_max = max(self.dictionary.items(),key=lambda x:x[1])[1]
        if self.y_max != self.y_min:
            self.ky = (height-30)/((self.y_max - self.y_min))
        else:
            self.ky = (height-30)/1

    def safe_func(self, x):
        try :
            return self.f(x)
        except:
            return None

    def screen_x(self, x):
        return self.kx * (x - self.x_min)

    def screen_y(self, y):
        return self.height - self.ky * (y - self.y_min) - 21

    def screen_x_y(self, x, y):
        return self.screen_x(x), self.screen_y(y)

    def make_dots(self):
        self.lines = []
        buf_dict={}
        for key,value in self.dictionary.items():
            buf_dict[self.screen_x(key)]=self.screen_y(value)
        keylist = sorted(buf_dict.keys())
        i = 0
        for key in keylist:
            if i == len(keylist)-1:
                break
            keyf = keylist[i]
            keys = keylist[i+1]
            list_ = [keyf,buf_dict[keyf],keys,buf_dict[keys]]
            self.lines.append(list_)
            i+=1
