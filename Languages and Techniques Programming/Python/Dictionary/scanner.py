#!/usr/bin/python3
# -*- coding: utf-8 -*-
__author__="great"

class Enum(set):
    def __getattr__(self, item):
        if item in self:
            return item
        raise AttributeError

class Tokens:
    def __init__(self,lexem,num,row,col):
        self.__lexem=lexem
        self.__num=num
        self.__row=row
        self.__col=col
        self.__word()

    def __word(self):
        if self.__lexem[0] in ['(','[']:
            self.__lexem=self.__lexem[1:]
        while self.__lexem[len(self.__lexem)-1] in [')',']',',','.','?','!','(','/',':',';','"']:
            self.__lexem=self.__lexem[0:-1]

    def get_lexem(self):
        return self.__lexem

    def get_num(self):
        return self.__num

    def get_row(self):
        return self.__row

    def get_col(self):
        return self.__col

def next_char(line,pos):
    if pos[0] == len(line) or (pos[2] == len(line[len(line)-1]) and pos[0] == len(line)-1):
        if pos[0]!=len(line):
            pos[0]+=1
        return pos
    elif line[pos[0]][pos[2]] == ' ' or line[pos[0]][pos[2]] == "\t":
        pos[1]+=1
        pos[2]+=1
        next_char(line,pos)
    elif line[pos[0]][pos[2]] == "\n":
        pos[0]+=1
        pos[2]=0
        next_char(line,pos)
    return pos

def no_sep(char):
    return not (char in [' ','\t','\n'])


def next_word(line,pos):
    pos=next_char(line,pos)
    word = ""
    i = pos[2]
    while pos[0] < len(line) and i < len(line[pos[0]]) and no_sep(line[pos[0]][i]):
        word+=line[pos[0]][i]
        i+=1
    buf = i
    return word,buf

def word_rec(word):
    return list(filter(lambda char: True if char.isalpha() else False,list(word)))!=[]

def dictionary_help(text):
    try:
        file = open(text,mode="r")
    except:
        print("Отказано в доступе: " + file)
        return
    lexemes = set()
    pos = [0,0,0]
    buf = []
    for line in file.readlines():
        buf.append(line)
    while pos[0]!=len(buf):
        word,i=next_word(buf,pos)
        if word != "" and word_rec(word):
            lexemes.add(Tokens(word,pos[0],pos[1]+1,pos[2]))
        if pos[0]!=len(buf):
            pos[1]+=i-pos[1]
            pos[2]+=i-pos[2]
            #print(word + " " + str(len(buf[pos[0]])))
    return lexemes

