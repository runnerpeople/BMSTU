#!/usr/bin/python3
# -*- coding: utf-8 -*-
import csv
import sys
import os
__author__="great"

def mime_type_linux(filepath):
    buf = "file -b --mime-type " + filepath
    line = os.popen(buf,"r")
    while True:
        line_str = line.readline()
        if not line_str:
            break
        print(line_str,end="")


def mime_type(filepath):
    try:
        file = open(filepath,mode="rb")
        text=True
        text=analyze_binary_type(file,text)
        if text==True:
            analyze_text_type(file,filepath)
        file.close()
    except IOError:
        print("Отказано в доступе: " + filepath)

def analyze_binary_type(file,flag):
    if flag==True:
        flag=is_png(file,flag)
    if flag==True:
        flag=is_jpeg(file,flag)
    if flag==True:
        flag=is_gif(file,flag)
    if flag==True:
        flag=is_pdf(file,flag)
    if flag==True:
        flag=is_zip(file,flag)
    return flag


def analyze_text_type(file,filepath):
    flag = True
    flag=is_html(file,flag)
    if flag==True:
        flag=is_xml(file,flag)
    if flag==True:
        flag=is_csv(filepath,flag)
    if flag==True:
        flag=is_txt(filepath,flag)
    if flag==True:
        print("Тип файла не определен")

def is_png(file,flag):
    str = file.read(8)
    file.seek(0)
    if int.from_bytes(str,byteorder="big") == 0x89504E470D0A1A0A:
        print("image/png")
        flag=False
        return flag
    else:
        return flag

def is_jpeg(file,flag):
    str = file.read(4)
    file.seek(0)
    if int.from_bytes(str,byteorder="big") == 0xFFD8FFE0:
        print("image/jpeg")
        flag=False
        return flag
    else:
        return flag

def is_gif(file,flag):
    str = file.read(6)
    file.seek(0)
    if int.from_bytes(str,byteorder="big") == 0x474946383761 or int.from_bytes(str,byteorder="big") == 0x474946383961:
        print("image/gif")
        flag=False
        return flag
    else:
        return flag

def is_pdf(file,flag):
    str = file.read(4)
    file.seek(0)
    if int.from_bytes(str,byteorder="big") == 0x25504446:
        print("application/pdf")
        flag=False
        return flag
    else:
        return flag

def is_zip(file,flag):
    str = file.read(4)
    file.seek(0)
    if int.from_bytes(str,byteorder="big") == 0x504B0304:
        print("application/zip")
        flag=False
        return flag
    else:
        return flag

def is_txt(filepath,flag):
    try:
        file = open(filepath,"rb+")
        file.readline()
        file.close()
        file_buf = open(filepath,"r+")
        file_buf.readline()
        file_buf.close()
        flag=False;
        print("text/plain")
        return flag
    except:
        return flag

def is_html(file,flag):
    file_str = b""
    i=0
    try:
        for x in file.readlines():
            if i == 4: # may be various number
                break;
            file_str += x
            i+=1
        file.seek(0)
        if file_str.find(b"<!DOCTYPE HTML>") != -1 or file_str.find(b"<html>") != -1:
            print("text/html")
            flag=False
        return flag
    except:
        return flag


def is_xml(file,flag):
    try:
        file_str=file.read(5)
        file.seek(0)
        if file_str.find(b"<?xml") != -1:
            print("text/xml")
            flag=False
        return flag
    except:
        return flag

def is_csv(filepath,flags):
    try:
        file = open(filepath)
        csv_iter = csv.reader(file)
        flag=True
        for line in csv_iter:
            if type(line)==list and len(line)>1:
                break
            else:
                flag = False
                pass
        file.close()
        if flag == True:
            print ("text/csv")
            flags=False
        return flags
    except:
        return flag
