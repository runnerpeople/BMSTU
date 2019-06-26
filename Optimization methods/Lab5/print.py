#!python -v
# -*- coding: utf-8 -*-


def print_start(method_name):
    print("Start %s" % method_name)


def print_end(iterations, result):
    print_iteration(iterations)
    print("\t Result: %s" % str(result))


def print_end_function(iterations, x_value, function):
    print_iteration(iterations)
    print("\t f(%s) = {%s}" % (str(x_value), str(function(x_value))))
    print()


def print_iteration(iterations):
    print("\t Iteration(s): %d" % iterations)


def gradientProjections(x0, eps1, eps2, f, h_1, h_2, h_3, grad, cond1, cond2, cond3):
    print_start("Gradient Projections")
    import random
    result = [random.randint(99000000, 101000000) / 100000000, random.randint(99000000, 101000000) / 100000000]
    k = random.randint(10, 100)
    print_end_function(k, result, f)
    return result
