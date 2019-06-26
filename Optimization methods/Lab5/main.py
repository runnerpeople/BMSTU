#!python -v
# -*- coding: utf-8 -*-
from lab5.extreme import *
import numpy as np
import random
from lab5.print import gradientProjections


# 30*(x_1^2 - x_2)^2 + 2*(x_1 - 1)^2 + 80
# min(F(x,y)) = 80  at  (x_0, x_1) = (1, 1)
def my_function(x):
    return 30 * pow((pow(x[0], 2) - x[1]), 2) + 2 * pow((x[0] - 1), 2) + 80


# grad(F(x,y)) = [4*(30*(x_1^3 - 30*x_1*x_2+x_1 - 1),-60(x_1^2 - x_2)]
def gradient_function(x):
    return np.array([(4 * (30 * pow(x[0], 3) - 30 * x[0] * x[1] + x[0] - 1)), (-60 * (pow(x[0], 2) - x[1]))])


def hessian_function(x):
    return np.array([[240 * x[0] ** 2 + 120 * (x[0] ** 2 - x[1]) + 4, -120 * x[0]],
                     [-120 * x[0], 60]])


def condFunction1(x):
    return x[0] ** 2 + x[1] ** 2 - 5.0


def condFunction2(x):
    return - x[0]


def condFunction3(x):
    return - x[1]


def derivativeCondFunction(x):
    return np.array([(4 * (30 * pow(x[0], 3) - 30 * x[0] * x[1] + x[0] - 1)), (-60 * (pow(x[0], 2) - x[1]))])


def derivativeCondFunction1(x):
    return 2 * x[0], 2 * x[1]


def derivativeCondFunction2(x):
    return -1.0, 0.0


def derivativeCondFunction3(x):
    return 0.0, -1.0


xStart = [random.randint(0, 1000) / 1000, random.randint(0, 1000) / 1000]
print(xStart)
epsilon = 0.0001
lambda_ = 1.0
betta = 1.5
curFunction = my_function
gradientFunction = gradient_function
hessianFunction = hessian_function

method_penalty_function(xStart, 2.0, 0.8, epsilon, curFunction,
                                       condFunction1, condFunction2, condFunction3)
method_barrier_function(xStart, epsilon, 2.0, 0.8, curFunction,
                                       condFunction1, condFunction2, condFunction3)
lagrange_functions(xStart, 0.5, 3.0, [0.01, 0.01, 0.01], [0.01, 0.01, 0.01], epsilon, curFunction,
                                  condFunction1, condFunction2, condFunction3)
gradientProjections(xStart, -0.1, 0.1, curFunction, condFunction1, condFunction2, condFunction3,
                                   gradientFunction, derivativeCondFunction1, derivativeCondFunction2, derivativeCondFunction3)
