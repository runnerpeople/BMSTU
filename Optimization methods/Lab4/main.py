#!python -v
# -*- coding: utf-8 -*-
import lab4.extreme as extreme_search
import numpy as np
import random


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


xStart = [random.randint(0, 100) / 100, random.randint(0, 100) / 100]
print(xStart)
epsilon = 0.0001
betta = 1.5
curFunction = my_function
gradientFunction = gradient_function
hessianFunction = hessian_function

extreme_search.hooke_jeeves(2, xStart, 0.5, epsilon, curFunction)
extreme_search.nelder_mead(curFunction, xStart)
extreme_search.gradient_descend(xStart, epsilon, epsilon, curFunction, gradientFunction)
extreme_search.flatcher_rivz(xStart, epsilon, epsilon, curFunction, gradientFunction)
extreme_search.davidon_flatcher_powell(xStart, epsilon, epsilon, curFunction, gradientFunction)
extreme_search.levenberg_markvardt(xStart, epsilon, curFunction, gradientFunction, hessianFunction)
