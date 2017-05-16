#!python
# -*- coding: utf-8 -*-

import math

def func(x):
    return 3*x ** 3 - 8*x**2 - 11*x + 10

def bisection_method(func,a,b,eps):
    if func(a)==0:
        return a
    elif func(b)==0:
        return b
    x = (a + b) / 2
    iteration = 0
    while abs(b-a)>2*eps:
        x = (a+b) / 2
        iteration += 1
        if func(a)*func(x)<0:
            b = x
        else:
            a = x
    return (iteration,(a+b)/2)

def golden_method(func,a,b,eps):
    iteration = 0
    phi = (1 + math.sqrt(5)) / 2
    while abs(b-a)>2*eps:
        iteration += 1
        x1 = b - (b - a) / phi
        x2 = a + (b - a) / phi
        if func(x1)*func(x2)<0:
            a = x1
            b = x2
        else:
            if func(a)*func(x2)<0:
                b = x2
            else:
                a = x1
    return (iteration,(a+b)/2)


if __name__ == "__main__":
    epsilon = [10 ** (-i) for i in range(1, 4)]
    real_answers = [1-math.sqrt(6),2/3,1+math.sqrt(6)]
    print("Bisection method: ")
    for eps in epsilon:
        print(" Epsilon = " + str(eps))
        roots = []
        roots.append(bisection_method(func,-2,0,eps))
        roots.append(bisection_method(func,0,2,eps))
        roots.append(bisection_method(func,2,4,eps))
        for i in range(len(roots)):
            print("  Iterations: " + str(roots[i][0]))
            print("  x = " + str(roots[i][1]))
            print("  x_real = " + str(real_answers[i]))
    print("Golden section method: ")
    for eps in epsilon:
        print(" Epsilon = " + str(eps))
        roots = []
        roots.append(golden_method(func, -2, 0, eps))
        roots.append(golden_method(func, 0, 2, eps))
        roots.append(golden_method(func, 2, 4, eps))
        for i in range(len(roots)):
            print("  Iterations: " + str(roots[i][0]))
            print("  x = " + str(roots[i][1]))
            print("  x_real = " + str(real_answers[i]))
