#!python
# -*- coding: utf-8 -*-
import math

def func(x):
    return math.exp(x)

def method_of_rectangles(func,a,b,n):
    h = (b-a)/n
    return h*sum(func(a+i*h+h/2) for i in range(n))

def method_of_trapezoid(func,a,b,n):
    h = (b-a)/n
    series_of_sum = sum(func(a+i*h) for i in range(1,n))
    return h * ((func(a)+func(b))/2 + series_of_sum)

def method_of_simpson(func,a,b,n):
    h = (b-a)/n
    series_of_sum  = sum(func(a+i*h-h/2) for i in range(1,n+1))
    series_of_sum1 = sum(func(a+i*h)     for i in range(1,n))
    return h/6 * (func(a)+func(b) + 4*series_of_sum + 2*series_of_sum1)

def richardson(int_h,int_h2,k):
    return (int_h - int_h2) / (2**k-1)

def calc_integral(epsilon,method,k,func,a,b):
    for eps in epsilon:
        print("Eps = " + str(eps))
        n = 1
        r = float("+inf")
        iteration = 0
        int_h = 0
        while abs(r) >= eps:
            n *= 2
            int_h2 = int_h
            int_h = method(func,a,b,n)

            r = richardson(int_h,int_h2,k)
            iteration += 1

        print(" Iterations = %d" % (iteration))
        print(" Result = " + str(int_h))
        print(" Result with Richardson: " + str(int_h + r))

if __name__ == "__main__":
    eps = [0.1 ** i for i in range(1,4)]
    print("Method of rectangles: ")
    calc_integral(eps,method_of_rectangles,2,func,0,1)
    print("Method of trapezoids: ")
    calc_integral(eps,method_of_trapezoid ,2,func,0,1)
    print("Method of Simpson: ")
    calc_integral(eps,method_of_simpson   ,4,func,0,1)



