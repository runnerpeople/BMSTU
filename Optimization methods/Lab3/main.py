#!python -v
# -*- coding: utf-8 -*-
import lab3.extreme as extreme_search
from copy import deepcopy

# F(x) = 5x^6_1 - 36x^5_2 - \frac{165}{2}x_1^4 -60x^3_2 + 36, x_0 = -13
def my_function(x):
    return (5 * pow(x, 6)) - (36 * pow(x, 5)) + (165/2 * pow(x, 4)) - (60 * pow(x, 3)) + 36

# F'(x)
def my_derivative_function(x):
    return 30 * pow(x, 5) - 180 * pow(x, 4) + 330 * pow(x, 3) - 180 * pow(x, 2)


xStart = -13.0
stepSize = 1.0
epsilon = 0.01
delta = 0.01
curFunction = my_function
curDerivativeFunction = my_derivative_function

interval = extreme_search.method_svann(xStart, stepSize, curFunction)

extreme_search.bisection_method(epsilon, deepcopy(interval), curFunction)
extreme_search.golden_section_method(epsilon, deepcopy(interval), curFunction)
extreme_search.fibonacci_method(epsilon, deepcopy(interval), curFunction)

xStart = 13
stepSize = 0.01
extreme_search.quadratic_interpolation(epsilon, delta, xStart, stepSize, curFunction)
extreme_search.cubic_interpolation(epsilon, delta, xStart, stepSize, curFunction, curDerivativeFunction)
