import numpy as np
from src.householder import householder
import copy


def gauss(A, B):
    m = A.shape[0]

    x = [0 for _ in range(m)]
    for k in range(m - 1, -1, -1):
        temp = 0
        for j in range(k + 1, m):
            temp += A[k, j] * x[j]
        x[k] = (B[k][0] - temp) / A[k, k]
    return x


def iterative_refinement(A, B, x1, eps, iterations=50):
    iterate = 0
    while True:
        delta_x = delta_error(A, B, x1, True)
        print("Iteration [%d]:" % iterate + str(x1))
        if np.linalg.norm(delta_x) < eps or iterate > iterations:
            break
        else:
            x1 = np.array([x1]).transpose() + delta_x
            x1 = x1.transpose().tolist()[0]
            iterate += 1

    return x1


def delta_error(A, B, x1, double_precision=False):
    m = A.shape[0]
    b1 = np.zeros(m)
    for i in range(m):
        b1[i] = np.dot(A[i, ], x1)

    chosen_type = np.longfloat if double_precision else A.dtype
    r = b1.astype(chosen_type) - B.astype(chosen_type)
    r = np.negative(r)

    delta_x = gauss(A.astype(chosen_type), np.array([r]).transpose())

    return delta_x

def check_error(X, X1):
    return norm(X1-X)/norm(X)

def condition_hager1 ( n, a ):

  import numpy as np

  i1 = -1
  c1 = 0.0
#
#  Factor the matrix.
#
  b = np.zeros ( n )
  for i in range ( 0, n ):
    b[i] = 1.0 / float ( n )

  while ( True ):

    b2 = np.linalg.solve ( a, b )

    for i in range ( 0, n ):
      b[i] = b2[i]

    c2 = 0.0
    for i in range ( 0, n ):
      c2 = c2 + abs ( b[i] )

    for i in range ( 0, n ):
      b[i] = r8_sign ( b[i] )

    b2 = np.linalg.solve ( np.transpose ( a ), b )

    for i in range ( 0, n ):
      b[i] = b2[i]

    i2 = r8vec_max_abs_index ( n, b )

    if ( 0 <= i1 ):
      if ( i1 == i2 or c2 <= c1 ):
        break

    i1 = i2
    c1 = c2

    for i in range ( 0, n ):
      b[i] = 0.0
    b[i1] = 1.0

  value = c2 * r8mat_norm_l1 ( n, n, a )

  return value

def condition_hager(n, a):

    import numpy as np

    i1 = -1.0
    p = 0.0
    #
    #  Factor the matrix.
    #
    b = np.zeros(n)
    for i in range(0, n):
        b[i] = 1.0 / float(n)

    y = np.zeros(n)

    while True:

        x = np.linalg.solve(a, b)

        if norm1(x) <= p:
            return p
        else:
            p = norm1(x,)

        for i in range(0, n):
            y[i] = r8_sign(x[i])

        z = np.linalg.solve(np.transpose(a), y)

        i2 = r8vec_max_abs_index(n, b)

        i1 = i2

        if np.abs(z) < np.transpose(z).dot(b):
            return p

        for i in range(0, n):
            b[i] = 0.0
        b[i1] = 1.0


def r8vec_max_abs_index ( n, a ):
  if ( n <= 0 ):

    max_abs_index = -1

  else:

    max_abs_index = 0

    for i in range ( 1, n ):
      if ( abs ( a[max_abs_index] ) < abs ( a[i] ) ):
        max_abs_index = i

  return max_abs_index

def r8mat_norm_l1 ( m, n, a ):
  value = 0.0

  for j in range ( 0, n ):
    row_sum = 0.0
    for i in range ( 0, m ):
      row_sum = row_sum + abs ( a[i,j] )
    value = max ( value, row_sum )

  return value

def r8_sign(x):
    if (x < 0.0):
        value = -1.0
    else:
        value = +1.0

    return value

def check_error(A, X1, B):
    m = A.shape[0]
    b1 = np.zeros(m)
    for i in range(m):
        b1[i] = np.dot(A[i,], X1)

    r = b1 - B

    R = np.eye(m)

    for i in range(m):
        R[i,i] = r[i]

    return norm(R.dot(condition_hager(m,np.linalg.inv(A)))) / norm(X1)

def check_error2(A, X1, B, easy):
    m = A.shape[0]
    b1 = np.zeros(m)
    for i in range(m):
        b1[i] = np.dot(A[i,], X1)

    r = b1 - B

    R = np.eye(m)
    for i in range(m):
        R[i,i] = r[0,i]

    print("Condition Hager(inv):" + str(condition_hager1(m,np.linalg.inv(A))))
    print("Norm(inv): " + str(norm1(np.linalg.inv(A))))

    print("Condition Hager:" + str(condition_hager1(m, A)))
    print("Norm: " + str(norm1(A)))

    return norm(np.linalg.inv(A).dot(R))/norm(X1)

def norm(A):
    return np.linalg.norm(A, ord=np.inf)


def norm1(A):
    return np.linalg.norm(A, ord=1)


def hager_estimator(A):
    size = max(A.shape[0], A.shape[1])
    x = np.arange(0, 1, 1/size)
    for i in range(size):
        x[i] = 1/size

    A_ = copy.deepcopy(A[0:size, 0:size])

    while True:

        w = np.linalg.inv(A_).dot(x)
        c = np.sign(w)
        z = np.linalg.inv(A_).T.dot(c)

        if norm(z) <= z.transpose().dot(x):
            return norm1(w)
        else:
            for j in range(0, size):
                if norm(z) == np.abs(z[j]):
                    x = np.zeros(size)
                    x[j] = 1
                    break
