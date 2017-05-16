#!python -v
# -*- coding: utf-8 -*-

import numpy

def tridiagonalMatrixAlgorithm(d,c,a,b):
    alpha = [0]
    beta = [0]
    n = len(b)

    for i in range(1,n-1):
        if i == 1:
            alpha.append(-c[i]/d[i])
            beta.append(b[i]/d[i])
        else:
            alpha.append(-c[i]/(d[i]+alpha[i-1]*a[i-1]))
            beta.append((b[i]-a[i-1]*beta[i-1])/(d[i]+alpha[i-1]*a[i-1]))

    x = [0 for _ in range(n)]
    n -= 1
    x[n] = (b[n]-a[n-1]*beta[n-1])/(d[n]+a[n-1]*alpha[n-1])
    for i in range(n-1,0,-1):
        x[i]=x[i+1]*alpha[i]+beta[i]
    return x[1:]

def init_array(matrix,bb):
    a, b, d, c = [0], [0], [0], [0]
    d.extend([matrix[i][i] for i in range(len(matrix[0]))])
    c.extend([matrix[i][i + 1] for i in range(len(matrix[0]) - 1)])
    a.extend([matrix[i + 1][i] for i in range(len(matrix[0]) - 1)])
    b.extend(bb)
    return a,b,c,d

def valueB(matrix,x):
    b = [0 for _ in range(len(x))]
    for j in range(len(matrix)):
        b[j] = sum(matrix[j][i] * x[i] for i in range(len(matrix[j])))
    return b

def find_error(matrix,d1,d2,x2):
    r = [d2[i] - d1[i] for i in range(len(matrix))]
    matrix_rev = numpy.matrix(matrix).I
    matrix_rev = matrix_rev.tolist()

    e = [0 for _ in range(0, len(d1))]
    for j in range(0, len(matrix_rev)):
        e[j] = sum(matrix_rev[j][i] * r[i] for i in range(len(matrix_rev[j])))

    x = [x2[i] - e[i] for i in range(0, len(matrix_rev))]
    return x,e[0:4]

if __name__=="__main__":
    matrix = [[1,2,0,0],
              [2,-1,-1,0],
              [0,1,-1,1],
              [0,0,1,1]]
    D = [5,-3,3,7]
    a,b,c,d = init_array(matrix,D)
    x = tridiagonalMatrixAlgorithm(d,c,a,b)
    print(x)
    D_ = valueB(matrix,x)
    print(D_)
    x_real,error = find_error(matrix,D,D_,x)
    print(error)
    print(x_real)

