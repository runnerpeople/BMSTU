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
