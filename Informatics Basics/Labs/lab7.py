#!/usr/bin/env python3
# -*- coding: utf-8 -*-
__author__="great"

class StackException(Exception):
    def __init__(self,msg):
        self.msg=msg
    def __str__(self):
        return repr(self.msg)

class QueueException(Exception):
    def __init__(self,msg):
        self.msg=msg
    def __str__(self):
        return repr(self.msg)

class Stack:
    def __init__(self):
        self.data = []

    def isEmpty(self):
        return self.data == []

    def pop(self):
        if self.isEmpty():
            raise StackException("Null stack")
        return self.data.pop()

    def push(self,x):
        self.data.append(x)

    def size(self):
        return len(self.data)

class Queue:
    def __init__(self):
        self.data = []

    def isEmpty(self):
        return self.data == []

    def pop(self):
        if len(self.data) == 0:
            raise QueueException("Null queue")
        return self.data.pop(0)

    def push(self,x):
        self.data.append(x)

    def size(self):
        return len(self.data)

def rpn(string):
    list_ = string.split(" ")
    stack = Stack()
    for i in range(len(list_)):
        if (list_[i].isdigit()):
            stack.push(list_[i])
        elif list_[i]=="_":
            val = int(stack.pop())
            stack.push(-val)
        elif list_[i] in ["+","-","/","*","^"]:
            if list_[i]=="^":
                a,b=stack.pop(),stack.pop()
                stack.push(eval(str(a) + " ** " + str(b)))
            else:
                stack.push(eval(str(stack.pop()) + " " + list_[i] + " " + str(stack.pop())))
    return float(stack.pop())


class Iv:
    def __init__(self,a,b=0):
        self.a=a
        if b==0:
            self.b=self.a
        else:
            self.b=b

    def __add__(self, other):
        total_a = self.a + other.a
        total_b = self.b + other.b
        return Iv(total_a,total_b)

    def __sub__(self, other):
        total_a = self.a - other.b
        total_b = self.b - other.a
        return Iv(total_a,total_b)

    def __mul__(self, other):
        total_a = min(self.a*other.a,self.a*other.b,self.b*other.a,self.b*other.b)
        total_b = max(self.a*other.a,self.a*other.b,self.b*other.a,self.b*other.b)
        return Iv(total_a,total_b)

    def __truediv__(self, other):
        if 0 in [self.a,self.b,other.a,other.b]:
            raise ZeroDivisionError("Division by zero")
        total_a = min(self.a/other.a,self.a/other.b,self.b/other.a,self.b/other.b)
        total_b = max(self.a/other.a,self.a/other.b,self.b/other.a,self.b/other.b)
        return Iv(total_a,total_b)

    def __str__(self):
        return "[%f, %f]" % (self.a,self.b)
