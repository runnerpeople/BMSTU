#!/usr/bin/python3
# -*- coding: utf-8 -*-
import tkinter
import tkinter.font
__author__ = 'great'


def bttn_clicked():
    print("Button clucked")

def test1():
    wnd = tkinter.Tk()
    wnd.title("Example")
    bttn = tkinter.Button(wnd,text="Click me",command=bttn_clicked())
    bttn.pack()
    wnd.mainloop()

test1()
