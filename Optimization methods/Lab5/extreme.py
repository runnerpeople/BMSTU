#!python -v
# -*- coding: utf-8 -*-
import lab5.print as print_function
from lab4.extreme import *
from lab3.extreme import method_svann, golden_section_method
import numpy as np


def method_penalty_function(x_c, r, c, eps, f, h_1, h_2, h_3):
    print_function.print_start("Penalty function")
    k = 1
    while True:
        p = lambda x: r / 2 * (h_1(x) ** 2 + h_2(x) ** 2 + h_3(x) ** 2)
        x_new = nelder_mead(lambda x: f(x) + p(x_c), x_c)
        penalty_value = p(x_new)
        if abs(penalty_value) < eps:
            print_function.print_end_function(k, x_new, f)
            return x_new
        else:
            r *= c
            x_c = x_new
            k += 1


def method_barrier_function(x_c, r, c, eps, f, h_1, h_2, h_3):
    print_function.print_start("Barrier function")
    k = 1
    while True:
        if (h_1(x_c) ** 2 + h_2(x_c) ** 2 + h_3(x_c) ** 2) != 0:
            p = lambda x, r_: -(r_ ** k) * (1 / (h_1(x) ** 2 + h_2(x) ** 2 + h_3(x) ** 2))
        else:
            p = lambda x, r_: -(r_ ** k)
        x_new = nelder_mead(lambda x: f(x) + p(x_c, r), x_c)
        penalty_value = p(x_new, r)
        if abs(penalty_value) < eps:
            print_function.print_end_function(k, x_new, f)
            return x_new
        else:
            k += 1
            x_c = x_new
            r /= c


def lagrange_functions(x_start, increase_param, r, lambdas, mu, eps, f, h_1, h_2, h_3):
    eps /= 100
    print_function.print_start("Lagrange Functions")
    k = 1
    x_c = x_start
    while True:

        def func(x):
            return f(x) + langrange_lambda + eq_penalty(x) + 1 / (2 * r) * sum(np.array(neq_penalty) - np.array(mu_squared))

        langrange_lambda = np.sum(np.matmul(np.array(lambdas), [h_1(x_start), h_2(x_start), h_3(x_start)]))
        eq_penalty = lambda x: (r / 2) * (h_1(x) ** 2 + h_2(x) ** 2 + h_3(x))
        neq_penalty = [max(0.0, mu[0] + r * pow(h_1(x_c), 2)),
                       max(0.0, mu[1] + r * pow(h_2(x_c), 2)),
                       max(0.0, mu[2] + r * pow(h_3(x_c), 2))]

        mu_squared = [mu[0] ** 2, mu[1] ** 2, mu[2] ** 2]

        x_new = nelder_mead(func, x_c)

        new_penalty = [max(0.0, mu[0] + r * pow(h_1(x_new), 2)),
                       max(0.0, mu[1] + r * pow(h_2(x_new), 2)),
                       max(0.0, mu[2] + r * pow(h_3(x_new), 2))]

        new_mu_squared = [mu[0] ** 2, mu[1] ** 2, mu[2] ** 2]

        penalty_value = r / 2 * (h_1(x_new) ** 2 + h_2(x_new) ** 2 + h_3(x_new)) * \
                        sum(np.array(new_penalty) - np.array(new_mu_squared))

        if abs(penalty_value) < eps:
            print_function.print_end_function(k, x_new, f)
            return x_new
        else:
            k += 1
            x_c = x_new
            r *= increase_param
            lambdas += np.array(np.multiply(r, [h_1(x_start), h_2(x_start), h_3(x_start)]))


def gradientProjections(x0, eps1, eps2, f, h_1, h_2, h_3, grad, cond1, cond2, cond3):
    print_function.print_start("Gradient Projections")
    max_iterations = 1000
    k = 0
    t_k_star = 3.4
    while k < max_iterations:
        if k >= max_iterations:
            print_function.print_end_function(k, x0, f)
            return x0, k
        a_k = np.array([cond1(x0), cond2(x0), cond3(x0)])
        t_k = np.multiply(-1, np.array([h_1(x0), h_2(x0), h_3(x0)])).T
        delta_2_x_k = np.matmul(np.matmul(a_k.T, np.linalg.inv(np.matmul(a_k, a_k.T))), t_k)
        norm_value = np.linalg.norm(delta_2_x_k)
        gradient_value = grad(x0)
        # if gradient_value == 0:
        #     print_function.print_end_function(k, x0, f)
        #     return x0
        V = a_k.T @ np.linalg.inv(a_k @ a_k.T)
        X = V @ a_k
        S = np.eye(X.shape[0]) - X
        delta_x_k = (-1 * S) @ gradient_value
        # if 0 > delta_x_k > eps1:
        #     active_restrictions.remove(gradient_value)
        if np.linalg.norm(delta_x_k) <= eps1 and norm_value <= eps2:
            print_function.print_end_function(k, x0, f)
            return x0, k
        elif np.linalg.norm(delta_x_k) > eps1 and norm_value <= eps2:
            delta_2_x_k = 0
            new_func = lambda t: f(x0 + t * delta_x_k)
            interval = method_svann(1.0, 0.001, new_func)
            t_k_star = golden_section_method(0.001, interval, new_func)
        elif np.linalg.norm(delta_x_k) > eps1 and norm_value > eps2:
            new_func = lambda t: f(x0 + t * delta_x_k)
            interval = method_svann(1.0, 0.001, new_func)
            t_k_star = golden_section_method(0.001, interval, new_func)
        elif np.linalg.norm(delta_x_k) <= eps1 and norm_value > eps2:
            delta_x_k = 0
        x0 = x0 + t_k_star * delta_x_k + delta_2_x_k
        k += 1
