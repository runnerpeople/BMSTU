#!/usr/bin/python3
# -*- coding: utf-8 -*-
__author__ = 'great'
def easy_divide_and_mod(a,b):
    return [a//b,a -(a//b)*b]


def divide_and_mod(a,b):
    count = len(str(a))-len(str(a)[str(a).find("."):])
    return [round(a//b),round(a -(a//b)*b,count)]

def list_words(string):
    words = []
    buffer = ""
    for i in string:
        if not buffer and i.isspace():
            continue
        if i.isspace():
            words.append(buffer)
            buffer = ""
            continue
        buffer += i
    if buffer:
        words.append(buffer)
    return words


def my_int(string):
    number = 0
    string = string[::-1]
    j = 0
    for i in string:
        if not i.isdigit():
            return -1
        else:
            number += (ord(i)-48) * (10 ** j)
            j+=1
    return number

def delete_parathesis(string):
    while True:
        if string.find("[")==-1 or string.find("]")==-1:
            break
        else:
            delete_string=string[string.find("["):(string.find("]")+1)]
            string = string.replace(delete_string,"")
    return string


def easy_arith_expr():
    print("Введите аримфетическое выражение,")
    print("например 4+5-3-5+2 и нажмите Enter")
    expr = input("-> ")
    value = eval(expr)
    print("Значение введённого выражения: ",end="")
    return str(value)

def arith_expr():
    print("Введите аримфетическое выражение,")
    print("например 4+5-3-5+2 и нажмите Enter")
    expr = input("->")
    value = int(expr[0])
    index = 0
    for i in expr:
        if index %2 == 0:
            index+=1
            continue
        elif index == len(expr):
            break
        else:
            if expr[index] == "+":
                value += int(expr[index+1])
            else:
                value -= int(expr[index+1])
            index+=2
    print("Значение введённого выражения: ",end="")
    return str(value)

def max_two_element(list_):
    set_list = set(list_)
    max_one = max(set_list)
    set_list.remove(max_one)
    max_two = max(set_list)
    return [max_two,max_one]

def reverse_plus(list_):
    new_list = []
    for i in range(len(list_)):
        new_list.append(list_[len(list_)-i-1])
    return new_list

def remove_null(list_):
    return [x for x in list_ if x !=0]

def easy_average_geometry(list_):
    from functools import reduce
    return reduce(lambda x,y:x*y,list_) ** (1/len(list_))

def average_geometry(list_):
    value = 1
    for i in range(len(list_)):
        value *= list_[i]
    value = value ** (1/len(list_))
    return value


### Тесты ###
print(easy_divide_and_mod(13,5))
print(divide_and_mod(13,5))
print(list_words("Дана строка символов"))
print(my_int("123"))
print(my_int("ABC"))
print(delete_parathesis("[Старинные ]меры длины:дюйм[ 2,5 см],аршин[ 71 см] и верста[ 1,067 км]"))
print(easy_arith_expr())
print(arith_expr())
print(max_two_element((1,1,2,2,3,3)))
print(reverse_plus((1,2,3,4,5)))
print(remove_null((1,2,3,0,4,2,0,1,0)))
print(easy_average_geometry((1,2,3,4,5)))
print(average_geometry((1,2,3,4,5)))
