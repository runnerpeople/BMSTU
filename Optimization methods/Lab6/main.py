from lab6.simplex import MatrixSimplex
import lab6.bb as BB
import numpy as np
import scipy.optimize


# f = lambda x: x[0] - x[1] + x[2]

# http://www.reshmat.ru/simplex.html?l1=1&l2=-1&l3=1&l4=0&maxOrMin=max&a11=-1&a12=2&a13=-1&a14=0&z1=2&b1=4&a21=3&a22=1&a23=0&a24=1&z2=2&b2=14&step=2&sizeA=4&sizeB=2#b

# cond_f1 = lambda x: -x[0] + 2 * x[1] - x[2] == 4
# cond_f2 = lambda x: 3 * x[0] + x[1] + x[3] == 14
# cond_f2 = lambda x: x[0] >= 0 and x[1] >= 0 and x[2] >= 0 and x[3] >= 0


if __name__ == "__main__":
	A = [[-1, 2, -1, 0], [3, 1, 0, 1]]
	c = [1, -1, 1, 0]
	b = [4, 14]
	simplex = MatrixSimplex(A, b, c, [1, 2])
	simplex.do_simplex()

	c = [-1, 11, -1, 5]
	# c = [-1, 1, -1, 0]
	A_eq = [[-1, 2, -1, 0], [3, 1, 0, 1]]
	b_eq = [4, 14]

	BB.calculate(A_eq, b_eq, c)