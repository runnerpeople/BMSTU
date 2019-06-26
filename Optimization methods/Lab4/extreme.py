#!python -v
# -*- coding: utf-8 -*-
import lab4.print as print_function
from lab3.extreme import method_svann, bisection_method, fibonacci_method, golden_section_method
import numpy as np
import copy

maxIterations = 10000


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


def best_near_by(point, function, method=0):
    z = point.copy()

    for i in range(2):
        interval = method_svann(1, 0.001, lambda x: function(z))

        if method == 0:
            z[i] = bisection_method(0.01, interval, lambda x: function(z))
        elif method == 1:
            z[i] = golden_section_method(0.01, interval, lambda x: function(z))
        elif method == 2:
            z[i] = fibonacci_method(0.01, interval, lambda x: function(z))

    return z, function(z)


def find_min(start, function, method=1):
    step = 0.01
    interval = method_svann(start, step, function)

    if method == 0:
        return bisection_method(0.001, interval, function)
    elif method == 1:
        return golden_section_method(0.001, interval, function)
    elif method == 2:
        return fibonacci_method(0.001, interval, function)


def minimizing(point, grad_value, function):
    return lambda gamma: function(point - grad_value.T * gamma)


def hooke_jeeves(dim, start_point, rho, eps, f):
    print_function.print_start("Hooke Jeeves")

    new_x = start_point.copy()
    x_before = start_point.copy()

    delta = np.zeros(dim)

    for i in range(0, dim):
        if start_point[i] == 0.0:
            delta[i] = rho
        else:
            delta[i] = rho * abs(start_point[i])

    step_length = rho
    k = 0
    f_before = f(new_x)

    while k < maxIterations and eps < step_length:

        k = k + 1

        for i in range(0, dim):
            new_x[i] = x_before[i]

        new_x, new_f = best_near_by(new_x, f, 0)

        keep = True

        while new_f < f_before and keep:

            for i in range(0, dim):
                if new_x[i] <= x_before[i]:
                    delta[i] = - abs(delta[i])
                else:
                    delta[i] = abs(delta[i])
                tmp = x_before[i]
                x_before[i] = new_x[i]
                new_x[i] = new_x[i] + new_x[i] - tmp

            f_before = new_f
            new_x, new_f = best_near_by(new_x, f, 0)

            if f_before <= new_f:
                break
            keep = False

            for i in range(0, dim):
                if 0.5 * abs(delta[i]) < abs(new_x[i] - x_before[i]):
                    keep = True
                    break

        if eps <= step_length and f_before <= new_f:
            step_length = step_length * rho
            for i in range(0, dim):
                delta[i] = delta[i] * rho

    end_point = x_before.copy()

    print_function.print_end_function(k, end_point, f)
    return end_point


def nelder_mead(f, x_start, step=0.1, no_improve_thr=10e-6, no_improve_break=10, max_iter=0, alpha=1., gamma=2.,
                rho=-0.5, sigma=0.5):
    # print_function.print_start("Nelder Mead")
    # init
    dim = len(x_start)
    prev_best = f(x_start)
    no_improve = 0
    res = [[x_start, prev_best]]

    for i in range(dim):
        x = copy.copy(x_start)
        x[i] = x[i] + step
        score = f(x)
        res.append([x, score])

    # simplex iter
    k = 0
    while 1:
        # order
        res.sort(key=lambda elem: elem[1])
        best = res[0][1]

        # break after max_iter
        if max_iter and k >= max_iter:
            # print_function.print_end_function(k, res[0][0], f)
            return res[0][0]
        k += 1

        if best < prev_best - no_improve_thr:
            no_improve = 0
            prev_best = best
        else:
            no_improve += 1

        if no_improve >= no_improve_break:
            # print_function.print_end_function(k, res[0][0], f)
            return res[0][0]

        # centroid
        x0 = [0.] * dim
        for tup in res[:-1]:
            for i, c in enumerate(tup[0]):
                x0[i] += c / (len(res) - 1)

        # reflection
        xr = x0 + alpha * (np.array(x0) - res[-1][0])
        r_score = f(xr)
        if res[0][1] <= r_score < res[-2][1]:
            del res[-1]
            res.append([xr, r_score])
            continue

        # expansion
        if r_score < res[0][1]:
            xe = x0 + gamma * (np.array(x0) - res[-1][0])
            e_score = f(xe)
            if e_score < r_score:
                del res[-1]
                res.append([xe, e_score])
                continue
            else:
                del res[-1]
                res.append([xr, r_score])
                continue

        # contraction
        xc = x0 + rho * (np.array(x0) - res[-1][0])
        c_score = f(xc)
        if c_score < res[-1][1]:
            del res[-1]
            res.append([xc, c_score])
            continue

        # reduction
        x1 = res[0][0]
        n_res = []
        for tup in res:
            red_x = x1 + sigma * (tup[0] - x1)
            score = f(red_x)
            n_res.append([red_x, score])
        res = n_res


def gradient_descend(x0, eps1, eps2, f, gradient):
    print_function.print_start("Gradient Descend")

    xk = x0[:]
    k = 0
    while True:
        gradient_value = gradient(xk)

        if np.linalg.norm(gradient_value) < eps1:
            print_function.print_end_function(k, xk, f)
            return xk
        if k >= maxIterations:
            print_function.print_end_function(k, xk, f)
            return xk

        t = 0.0
        min_value_func = f(xk - t * gradient_value)
        for i in np.arange(0.0, 2.0, 0.001):
            if i == 0.0:
                continue
            func_value = f(xk - i * gradient_value)
            if func_value < min_value_func:
                min_value_func = func_value
                t = i

        xk_new = xk - t * gradient_value

        if np.linalg.norm(xk_new - xk) < eps2 and np.linalg.norm(f(xk_new) - f(xk)):
            print_function.print_end_function(k - 1, xk_new, f)
            return
        else:
            k += 1
            xk = xk_new


def flatcher_rivz(x0, eps1, eps2, f, gradient):
    print_function.print_start("Flatcher-Rivz")

    xk = x0[:]
    xk_new = x0[:]
    xk_old = x0[:]

    k = 0
    d = []

    while True:
        gradient_value = gradient(xk)

        if np.linalg.norm(gradient_value) < eps1:
            print_function.print_end_function(k, xk, f)
            return

        if k >= maxIterations:
            print_function.print_end_function(k, xk, f)
            return
        if k == 0:
            d = -gradient_value
        beta = np.linalg.norm(gradient(xk_new)) / np.linalg.norm(gradient(xk_old))
        d_new = np.add(-gradient(xk_new), np.multiply(beta, d))
        t = 0.1
        min_value_func = f(xk + t * d_new)
        for i in np.arange(0.0, 1.0, 0.001):
            if i == 0.0:
                continue
            func_value = f(xk + i * d_new)
            if func_value < min_value_func:
                min_value_func = func_value
                t = i
        xk_new = xk + t * d_new
        if np.linalg.norm(xk_new - xk) < eps2 and np.linalg.norm(f(xk_new) - f(xk)):
            print_function.print_end_function(k - 1, xk_new, f)
            return
        else:
            k += 1

            xk_old = xk
            xk = xk_new
            d = d_new


def davidon_flatcher_powell(x0, eps1, eps2, f, gradient):
    print_function.print_start("Davidon-Flatcher-Powell")
    eps1 /= 100
    eps2 /= 100
    k = 0
    xk_new = copy.deepcopy(x0[:])
    xk_old = copy.deepcopy(x0[:])

    a_new = np.eye(2, 2)
    a_old = np.eye(2, 2)

    while True:
        gradient_value = gradient(xk_old)

        if np.linalg.norm(gradient_value) < eps1:
            print_function.print_end_function(k, xk_old, f)
            return xk_old

        if k >= maxIterations:
            print_function.print_end_function(k, xk_old, f)
            return xk_old
        if k != 0:
            delta_g = gradient(xk_new) - gradient_value
            delta_x = xk_new - xk_old

            num_1 = delta_x @ delta_x.T
            den_1 = delta_x.T @ delta_g

            num_2 = a_old @ delta_g @ delta_g.T * a_old
            den_2 = delta_g.T @ a_old @ delta_g
            a_c = num_1 / den_1 - num_2 / den_2
            a_old = a_new
            a_new = a_old + a_c

        minimizing_function = minimizing(xk_new, a_new @ gradient_value.T, f)

        alpha = find_min(0.0, minimizing_function)

        xk_old = xk_new
        xk_new = xk_old - alpha * a_new @ gradient_value
        if np.linalg.norm(xk_new - xk_old) < eps2 and np.linalg.norm(f(xk_new) - f(xk_old)) < eps2:
            print_function.print_end_function(k - 1, xk_new, f)
            return xk_new
        else:
            k += 1


def levenberg_markvardt(x0, eps1, f, gradient, hessian):
    print_function.print_start("Levenberg-Markvardt")
    k = 0
    xk = x0[:]
    nu_k = 10 ** 4
    while True:
        gradient_value = gradient(xk)
        if np.linalg.norm(gradient_value) < eps1:
            print_function.print_end_function(k, xk, f)
            return xk
        if k >= maxIterations:
            print_function.print_end_function(k, xk, f)
            return xk
        while True:
            hess_matrix = hessian(xk)
            temp = np.add(hess_matrix, nu_k * np.eye(2))
            temp_inv = np.linalg.inv(temp)
            d_k = - np.matmul(temp_inv, gradient_value)
            xk_new = xk + d_k
            if f(xk_new) < f(xk):
                k += 1
                nu_k = nu_k / 2
                xk = xk_new
                break
            else:
                nu_k = 2 * nu_k
        continue
