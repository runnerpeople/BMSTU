import numpy as np
import math


def householder_only_r(A, B):
    AB = np.vstack([A, B]).transpose()
    m = AB.shape[0]
    n = AB.shape[1]
    for i in range(0, m-1):
        Ai = AB[i:n, i:n]
        ai = Ai[:, 0]
        ai_norm = np.linalg.norm(ai)
        di = - ai_norm if Ai[0, 0] > 0 else ai_norm
        wi = Ai[0, 0] - di
        fi = math.sqrt(-2 * wi * di)
        h_ai = np.zeros(np.shape(ai)[0])
        h_ai[0] = di
        v = np.copy(ai)
        v[0] = wi
        v = v / fi
        Ai[:, 0] = h_ai

        for j in range(1, n-i):
            aj = Ai[:, j]
            fj = 2 * v.transpose().dot(aj)
            h_aj = aj - fj * v
            Ai[:, j] = h_aj
    return AB[0:m, 0:n-1], AB[0:, n-1:]


def householder_without_q(A):

    def sign(elem):
        return -1 if elem < 0 else 1

    m, n = np.shape(A)
    W = np.zeros((m, n))
    R = A.copy()

    for k in range(m-1):

        x = R[k:m, k]
        e = np.zeros(len(x))
        e[0] = 1
        alpha = sign(x[0]) * np.linalg.norm(x, 2)
        u = x - alpha * e
        v = u / np.linalg.norm(u, 2)
        R[k:m, k:n] = R[k:m, k:n] - 2 * np.outer(v, np.dot(v.transpose(), R[k:m, k:n]))
        W[k:m, k] = v
    return W, R


def form_q(W):
    m, n = np.shape(W)
    Q = np.identity(m)
    for i in range(m):
        for k in range(n-1, -1, -1):
            Q[k:m, i] = Q[k:m, i]-2*np.dot(np.outer(W[k:m, k], W[k:m, k]), Q[k:m, i])
    return Q


def valid(Q, R):
    return np.dot(Q, R)


def householder(A):
    def sign(x):
        return -1 if x < 0 else 1

    m, n = A.shape
    R = np.array(A).copy()
    P = np.eye(m)
    E = np.eye(m)
    Q = None

    for i in range(m-1):
        u = R[i:, i]-sign(R[i:, i][0])*np.linalg.norm(R[i:, i])*E[i:, i]
        u = np.array([u])
        u = u.T
        H = np.eye(m)
        H[i:, i:] = np.eye(m-i)-2*np.dot(u, u.T)/(np.linalg.norm(u)*np.linalg.norm(u.T))
        R = np.dot(H, R)
        P = np.dot(H, P)
        Q = P.T
    return Q, R
