#!python -v
# -*- coding: utf-8 -*-
import lab3.print as print_function
import math


class Interval:

    def __init__(self, x_start, x_end):
        self.x_start = x_start
        self.x_end = x_end

    @property
    def length(self):
        return abs(self.x_end - self.x_start)

    @property
    def center(self):
        return (self.x_end + self.x_start) / 2

    def __str__(self):
        return "[" + str(self.x_start) + " , " + str(self.x_end) + "]"


def method_svann(x_start, step_size, function):
    # print_function.print_start("Svann Method")

    k = 0

    x_values = [x_start]

    fun_result_without_step_size = function(x_start - step_size)
    fun_result_on_start = function(x_start)
    fun_result_with_step_size = function(x_start + step_size)

    interval = Interval(x_start - step_size, x_start)

    if fun_result_without_step_size >= fun_result_on_start and fun_result_on_start <= fun_result_with_step_size:
        return interval
    elif fun_result_without_step_size <= fun_result_on_start and fun_result_on_start >= fun_result_with_step_size:
        raise Exception("Interval can't be found, choose another x_start (%f) variable!" % (str(x_start)))
    else:
        delta = float(0.0)
        k += 1

        if fun_result_without_step_size >= fun_result_on_start >= fun_result_with_step_size:
            delta = step_size
            interval.x_start = x_values[0]

            x_values.insert(k, x_start + step_size)

        elif fun_result_without_step_size <= fun_result_on_start <= fun_result_with_step_size:
            delta = -step_size
            interval.x_end = x_values[0]

            x_values.insert(k, x_start - step_size)

        while True:

            x_values.insert(k + 1, (x_values[k] + pow(2.0, k) * delta))

            if function(x_values[k + 1]) >= function(x_values[k]):
                if delta > 0:
                    interval.x_end = x_values[k + 1]
                elif delta < 0:
                    interval.x_start = x_values[k + 1]
            else:
                if delta > 0:
                    interval.x_start = x_values[k]
                elif delta < 0:
                    interval.x_end = x_values[k]

            if function(x_values[k + 1]) >= function(x_values[k]):
                break

            k += 1

    # print_function.print_end(k, interval)
    return interval


def bisection_method(epsilon, interval, function):
    # print_function.print_start("Bisection method")

    k = 0
    x_middle = interval.center

    while True:
        x_left_middle = interval.x_start + interval.length / 4
        x_right_middle = interval.x_end - interval.length / 4

        if function(x_left_middle) < function(x_middle):
            interval.x_end = x_middle
            x_middle = x_left_middle
        elif function(x_right_middle) < function(x_middle):
            interval.x_start = x_middle
            x_middle = x_right_middle
        else:
            interval.x_start = x_left_middle
            interval.x_end = x_right_middle

        k += 1

        if not interval.length > epsilon:
            break

    # print_function.print_end_function(k, x_middle, function)
    return x_middle


def golden_section_method(epsilon, interval, function):
    # print_function.print_start("Golden Section method")

    k = 0

    phi = (1 + math.sqrt(5.0)) / 2

    while interval.length > epsilon:
        z = (interval.x_end - (interval.x_end - interval.x_start) / phi)
        y = (interval.x_start + (interval.x_end - interval.x_start) / phi)
        if function(y) <= function(z):
            interval.x_start = z
        else:
            interval.x_end = y

        k += 1

    # print_function.print_end_function(k, interval.center, function)
    return interval.center


def fibonacci_method(eps, interval, function):
    # print_function.print_start("Fibonacci method")

    k = 0

    n = 3
    fib_arr = [1.0, 1.0, 2.0, 3.0]
    f1 = 2.0
    f2 = 3.0
    while fib_arr[len(fib_arr) - 1] < interval.length / eps:
        fib_arr.append(f1 + f2)
        f1 = f2
        f2 = fib_arr[len(fib_arr) - 1]
        n = n + 1

    for i in range(1, n - 3):
        y = (interval.x_start + fib_arr[n - i - 1] / fib_arr[n - i + 1] * (interval.x_end - interval.x_start))
        z = (interval.x_start + fib_arr[n - i] / fib_arr[n - i + 1] * (interval.x_end - interval.x_start))
        if function(y) <= function(z):
            interval.x_end = z
        else:
            interval.x_start = y

        k += 1

    # print_function.print_end_function(k, interval.center, function)
    return interval.center


def quadratic_interpolation(eps, delta, x_start, step_size, function):
    print_function.print_start("Quadratic Interpolation method")

    a1 = x_start
    k = 0

    while True:
        # Step 2
        a2 = a1 + step_size

        # Step 3
        f1 = function(a1)
        f2 = function(a2)

        # Step 4
        a3 = (a1 + 2 * step_size) if f1 > f2 else (a1 - 2 * step_size)

        while True:
            k += 1

            # Step 5
            f3 = function(a3)

            # Step 6
            f_min = min(f1, min(f2, f3))

            if f_min == f1:
                a_min = a1
            elif f_min == f2:
                a_min = a2
            elif f_min == f3:
                a_min = a3
            else:
                raise Exception("Cannot find f_min")

            # Step 7
            det = 2 * ((a2 - a3) * f1 + (a3 - a1) * f2 + (a1 - a2) * f3)
            if det == 0.0:
                a1 = a_min
            else:
                a = ((pow(a2, 2) - pow(a3, 2)) * f1 + (pow(a3, 2) - pow(a1, 2)) * f2 + (pow(a1, 2) - pow(a2, 2)) * f3) \
                    / det

                # Step 8
                if abs((f_min - function(a)) / function(a)) < eps and abs((a_min - a) / a) < delta:
                    print_function.print_end_function(k, a1, function)
                    return a1
                else:
                    if a1 <= a <= a3:
                        if a < a2:
                            a3 = a2
                            a2 = a
                        else:
                            a1 = a2
                            a2 = a
                    else:
                        a1 = a
                        break


def cubic_interpolation(eps, delta, x_start, step_size, function, derivative_function):
    print_function.print_start("Cubic Interpolation")

    a1 = x_start
    k = 0

    # Step 2
    df = derivative_function(x_start)

    m = 0.0
    a2 = a1
    # Step 3
    if df < 0:
        while True:
            a1 = a2
            a2 += pow(m, 2) * step_size
            m += 1.0
            if not (derivative_function(a1) * derivative_function(a2) > 0):
                break
    else:
        while True:
            a1 = a2
            a2 -= pow(m, 2) * step_size
            m += 1.0
            if not (derivative_function(a1) * derivative_function(a2) > 0):
                break

    while True:
        k += 1

        # Step 4
        f1 = function(a1)
        f2 = function(a2)
        df1 = derivative_function(a1)
        df2 = derivative_function(a2)

        # Step 5
        z = 3 * (f1 - f2) / (a2 - a1) + df1 + df2
        w = math.sqrt(pow(z, 2) - df1 * df2) if a1 < a2 else (- math.sqrt(pow(z, 2) - df1 * df2))

        mu = (df2 + w - z) / (df2 - df1 + 2*w)

        if mu < 0:
            a = a2
        elif 0.0 < mu <= 1.0:
            a = a2 - mu * (a2 - a1)
        elif mu > 1:
            a = a1
        else:
            raise Exception("Error in range")

        # Step 6
        while function(a) > function(a1) and ((a - a1) / a).absoluteValue > delta:
            a -= (a - a1) / 2

        # Step 7
        if abs(derivative_function(a)) <= eps and (abs(a - a1) / a) <= delta:
            print_function.print_end_function(k, a, function)
            return a
        else:
            if derivative_function(a) * derivative_function(a1) <= 0:
                a2 = a1
                a1 = a
            else:
                a1 = a
