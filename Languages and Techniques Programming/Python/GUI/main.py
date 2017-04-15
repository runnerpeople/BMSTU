#!/usr/bin/python3
# -*- coding: utf-8 -*-
import tkinter
import tkinter.messagebox
from math import *
import graphics
__author__ = 'great'


def plot():
    try:
        min_=float(x_min.get())
        max_= float(x_max.get())
    except BaseException:
        tkinter.messagebox.showerror("Wrond Bounds", "Неправильный формат границ")
        return None
    if min_>=max_:
        tkinter.messagebox.showerror("Error in Bound", "Ошибка в min/max границах")
        return None
    width = cnv.winfo_width()
    height = cnv.winfo_height()
    try:
        function = lambda x : eval(expression.get())
        n = function(min_)
    except BaseException:
        tkinter.messagebox.showerror("Wrong formula", "Ошибка введения формулы")
        return None
    g = graphics.Graphic(min_,max_,width,height,function)
    cnv.delete("all")
    cnv.create_line(g.screen_x_y(min_, 0), g.screen_x_y(max_, 0))
    cnv.create_line(g.screen_x_y(0, g.y_min), g.screen_x_y(0, g.y_max))
    cnv.create_text(g.screen_x(max_) - 30, g.screen_y(0) + 10, text = "x")
    cnv.create_text(g.screen_x(0) - 15, g.screen_y(g.y_max) + 15, text = "y")
    cnv.create_text(g.screen_x(0) - 10, g.screen_y(0) + 10, text = "O")
    cnv.create_polygon(g.screen_x(0)-3, g.screen_y(g.y_max)+7,g.screen_x_y(0, g.y_max),
                         g.screen_x(0)+3, g.screen_y(g.y_max)+7,
                         g.screen_x(0), g.screen_y(g.y_max)+5)
    cnv.create_polygon(g.screen_x(max_)-20, g.screen_y(0)-3,
                         g.screen_x(max_) - 13, g.screen_y(0),
                         g.screen_x(max_)-20, g.screen_y(0)+3,
                         g.screen_x(max_) - 18, g.screen_y(0))
    g.make_dots()
    for line in g.lines:
        cnv.create_line(line, width = 2)


def resize(event):
    cnv.config(width=event.width,height=event.height)
    plot()

wnd = tkinter.Tk()
wnd.title("Plot")
wnd.geometry("800x400")

frame1 = tkinter.Frame(wnd,borderwidth=5)
frame2 = tkinter.Frame(wnd,bg="white",borderwidth=5)
frame1.pack(side="bottom",fill='x')
frame2.pack(side="bottom",ipadx=50)
frame2.bind("<Configure>", resize)
cnv = tkinter.Canvas(frame2,background="white")
cnv.pack(side='top')

formula = tkinter.StringVar()
formula.set("x**2")
x_min_ = tkinter.StringVar()
x_min_.set("-2")
x_max_ = tkinter.StringVar()
x_max_.set("3")

text_field=tkinter.Label(frame1,text="f(x)=")
expression=tkinter.Entry(frame1,textvariable=formula)
text_field1=tkinter.Label(frame1,text="x min=")
x_min=tkinter.Entry(frame1,width=5,textvariable=x_min_)
text_field2=tkinter.Label(frame1,text="x max=")
x_max=tkinter.Entry(frame1,width=5,textvariable=x_max_)
button = tkinter.Button(frame1,text="Plot!")

text_field.pack(side='left')
expression.pack(side="left")
text_field1.pack(side='left')
x_min.pack(side="left")
text_field2.pack(side='left')
x_max.pack(side="left")
button.pack(side='right')
button.bind("<Button-1>",lambda event : plot())
wnd.bind("<Return>",lambda event : plot())

plot()
wnd.mainloop()

