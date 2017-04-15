#!/usr/bin/python3
# -*- coding: utf-8 -*-
__author__="great"

def memoize(func):
    class memoize:
        def __init__(self,f):
            self.f = f
            self.memo = {}
        def __call__(self, *args):
            if args not in self.memo:
                self.memo[args]=self.f(*args)
            return self.memo[args]
    return memoize(func)

def memoize1(func):
    memo = {}
    def wrap(x):
        if x not in memo:
            memo[x]=func(x)
        return memo[x]
    return wrap
    
@memoize
def trib(n):
    if n <= 1:
        return 0
    elif n == 2:
        return 1
    else:
        return trib(n-1) + trib(n-2) + trib(n-3)


