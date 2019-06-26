import numpy as np
import src.householder as function
import src.util as help_function

import time

eps = 10 ** 0

def generate_hilbert(size):
    matrix = np.eye(size)
    for i in range(size):
        for j in range(size):
            matrix[i,j] = 1 / (i + j + 1)
    return matrix

def single_vector(size):
    a = np.zeros((1,size))
    for j in range(size):
        a[0,j] = 1
    return a

def generate_vector_b(size):
    return np.random.rand(1, size)

def bmatrix(a):
    if len(a.shape) > 2:
        raise ValueError('bmatrix can at most display two dimensions')
    lines = str(a).replace('[', '').replace(']', '').splitlines()
    rv = [r'\begin{bmatrix}']
    rv += ['  ' + ' & '.join(l.split()) + r'\\' for l in lines]
    rv +=  [r'\end{bmatrix}']
    return '\n'.join(rv)

def bmatrix2(a):
    text = r'$\left[\begin{array}{*{'
    text += str(len(a[0]))
    text += r'}c}'
    text += '\n'
    for x in range(len(a)):
        for y in range(len(a[x])):
            text += str(a[x][y])
            text += r' & '
        text = text[:-2]
        text += r'\\'
        text += '\n'
    text += r'\end{array}\right]$'

    return text



def main():
    SIZE = 500
    subsup = np.random.rand(SIZE - 1)
    diagonal = np.random.rand(SIZE)
    A = np.diag(subsup, 1) + np.diag(subsup, -1) + np.diag(diagonal)
    B = np.random.rand(1,SIZE)

    start_time = time.time()

    # A = np.array([[12, 6, -4], [-51, 167, 24], [4, -68, -41]], dtype=np.float32).transpose()
    # A = np.array([[1, 1, 1, 1], [-1, 4, 4, -1],[4,-2,2,0]], dtype=np.float32).transpose()
    # A = np.array([[3,0,4], [-2,3,4]],dtype=np.float32)
    # B = np.array([[3,5,4]],dtype=np.float32)
    # A = np.array([[1, 1, 1, 1], [-1, 4, 4, -1]], dtype=np.float32)
    # B = np.array([[4, -2, 2, 0]], dtype=np.float32)
    # A = np.array([[2, 1, 3], [2, 3, 1], [4, -2, 3]], dtype=np.float64)
    # B = np.array([[18, 1, 14]], dtype=np.float32)
    # AB = np.vstack([A, B]).transpose()
    # A = np.array([[2, 1, 3], [2, 3, 1], [4, -2, 3]], dtype=np.float64)
    # B = np.array([[18, 1, 14]], dtype=np.float64)
    # A = generate_hilbert(6)
    # B = single_vector(6).dot(A)
    # B = generate_vector_b(6)
    AB = np.vstack([A, B]).transpose()
    # print(A)
    print(bmatrix(A))
    print(bmatrix(B))
    Q, R = function.householder(AB)
    Q1, R1 = np.linalg.qr(AB)
    # Q, R = function.householder(A)
    # W,R = function.householder_without_q(AB)
    m = R.shape[0]
    n = R.shape[1]
    if n != m + 1:
        n_ = min(m, n-1)
        X = help_function.gauss(R[0:n_, :n_], R[:n_, n_:])
        X1 = help_function.iterative_refinement(A[0:n_, 0:n_], B[:n_, 0:n_], X, eps)

        print("====")
        print(X)
        print("====")
        print(X1)

        print("=====")
        print(help_function.check_error(A, X1, B))

    else:
        X = help_function.gauss(R[0:m, 0:n - 1], R[0:, n - 1:])
        print("ALGORITHM: ")
        X1 = help_function.iterative_refinement(A.transpose(), B, X, eps)
        # print(np.linalg.solve(A, B))

        print("====")
        print(X)
        print("====")
        print(X1)

        print("=====")
        print(help_function.check_error2(A.transpose(), X1, B, True))

        # 6.978544726215272e-16
        # 7.105427357601003e-15

    print("====")
    print(bmatrix(Q))
    # print(function.form_q(W))
    print(bmatrix(Q1))
    print("====")

    print(bmatrix(R))
    print(bmatrix(R1))
    print("====")
    # print(function.valid(function.form_q(W),R))
    print(bmatrix(function.valid(Q1, R1)))

    # print(help_function.cn_estimator(function.valid(Q1, R1)))

    # help_function.check_error(AB,function.form_q(W),R,Q1,R1)
    # help_function.check_error(AB, Q, R, Q1, R1)

    print("====")
    elapsed_time = time.time() - start_time
    print(elapsed_time)


main()
