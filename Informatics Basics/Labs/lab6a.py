#!/usr/bin/python3
# -*- coding: utf-8 -*-
from math import *
import random
import itertools
__author__ = 'great'

# Gcd,lcm,prime

def gcd(a,b):
    while b>0:
        a,b = b, a%b
    return a

def gcd1(a,b):
    if b == 0:
        return a
    else:
        return(gcd1(b,a%b))

def lcm(a,b):
    return (a*b)/gcd(a,b)

def lcm1(a,b):
    return (a*b)/gcd1(a,b)

def isprime(a):
    return ((10**(a-1)) % a) == 1


# Numbers

def program():
    print("Now input numbers")
    list_numbers=[]
    while True:
        string_number = input()
        if not string_number:
            break
        else:
            list_numbers.append(float(string_number))
    print("Length of list " + str(len(list_numbers)))
    print("Sum of list " + str(sum(list_numbers)))
    if len(list_numbers)>0:
        print("Average arithmetics is " + str(sum(list_numbers)/len(list_numbers)))
        print("Max element is " + str(max(list_numbers)))
        print("Min element is " + str(min(list_numbers)))
        print("Max element of square_root is " + str(max(list_numbers,key=sqrt)))
        print("Min element of square_root is " + str(min(list_numbers,key=sqrt)))

# Count_string

def count(string):
    string = string.lower()
    dict_keys = {}
    for i in range(len(string)):
        if string[i].isspace() or string[i].isdigit():
            continue
        if string[i] in dict_keys:
            dict_keys[string[i]]+=1
        else:
            dict_keys[string[i]]=1
    for key in sorted(dict_keys):
        print(key + " - " + str(dict_keys[key]))

# Random_sample

def random_sample(xs,count):
    if count>len(set(xs)):
        return []
    else:
        list_xs = list(set(xs))
        list_output = []
        i=0
        while True:
            if i == count:
                break
            index = random.randint(0,len(list_xs)-1)
            list_output.append(list_xs[index])
            i+=1
            list_xs.remove(list_xs[index])
        return list_output

# Permutation with repitions

def permut(iter_,r):
    return [''.join(p) for p in itertools.product(iter_,repeat=r)]

def permut_no_rep(iter_,r):
    return [''.join(p) for p in itertools.permutations(iter_,r)]

def permut2(iter_,r):
    permut_list = permutation_generator(iter_,r)
    return [''.join(p) for p in permut_list]

def permutation_generator(iter_,r):
    iterations = [tuple(iter_)] * r
    result = [[]]
    for iteration in iterations:
        result = [x + [y] for x in result for y in iteration]
    for products in result:
        yield tuple(products)

print(permut_no_rep("ABCD",2))



