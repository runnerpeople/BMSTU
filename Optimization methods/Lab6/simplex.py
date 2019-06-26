import numpy as np


class MatrixSimplex:
    def __init__(self, A, b, c, B_idx):
        self.A = np.matrix(A)
        self.b = np.matrix(b)
        self.c = np.matrix(c)

        self.B_idx = B_idx
        self.N_idx = [i for i in range(self.A.shape[1]) if i not in self.B_idx]

    def get_B(self):
        return self.A[:, self.B_idx]

    def get_Cb(self):
        return self.c[:, self.B_idx]

    def get_Cn(self):
        return self.c[:, self.N_idx]

    def get_N(self):
        return self.A[:, self.N_idx]

    def computeZ(self, InvB, N):
        # z = Cb*InvB*b - (Cb*InvB*N - Cn)*Xn
        Cb = self.get_Cb()
        Cb_InvB = np.dot(Cb, InvB)

        Cb_InvB_b = np.dot(Cb_InvB, self.b.T)

        Cb_InvB_N = np.dot(Cb_InvB, N)
        bracket_term = np.subtract(Cb_InvB_N, self.get_Cn())
        return Cb_InvB_b, -1 * bracket_term

    def computeXb(self, InvB, N):
        # Xb = InvB*b - InvB*N*Xn

        InvB_b = np.dot(InvB, self.b.T)
        """
        InvB_N = np.dot(InvB, self.N)
        print InvB_N
        InvB_N_Xn = np.dot(InvB_N, self.Xn.T)
        print InvB_N_Xn
        Xb = np.subtract(InvB_b, InvB_N_Xn)
        print "Xb is: "
        print Xb
        """
        return InvB_b

    def is_optimal(self, z):
        for x in np.nditer(z):
            if x > 0:
                return False
        return True

    def compute_entering_variable(self, z):

        max_ascent = 0
        index = -1
        it = np.nditer(z.T, flags=['f_index'])
        while not it.finished:
            if it[0] > max_ascent:
                max_ascent = it[0]
                index = it.index
            it.iternext()
        if index is not -1:  # else returns None
            return self.N_idx[index]

    def min_ratio_test(self, Xb, ev):
        min = 999999999
        index = -1
        idx = 0
        for x, y in np.nditer([Xb, ev]):
            if y <= 0:
                idx += 1
                continue
            ratio = x / y
            # if 0 < ratio < min:
            if ratio < min:
                min = ratio
                index = idx
            idx += 1
        if index is not -1:
            return self.B_idx[index]

    def replace(self, ev_idx, dv_idx):
        for i in range(len(self.B_idx)):
            if self.B_idx[i] == dv_idx:
                self.B_idx[i] = ev_idx
                break

        # for i in range(len(self.N_idx)):
        #     if self.N_idx[i] == ev_idx:
        #         self.N_idx[i] = dv_idx
        #         break

        self.N_idx = [i for i in range(self.A.shape[1]) if i not in self.B_idx]

    def calculate_value(self, z, Xb):
        print("z = ", z[0, 0])
        for i in range(self.c.shape[1]):
            index = 0
            in_basis = False
            for j in self.B_idx:
                if i == j:
                    print("x[{}] = {}".format(j, Xb[index, 0]))
                    in_basis = True
                index += 1
            if not in_basis:
                print("x[{}] = {}".format(i, 0))
        print("")
        return

    def do_simplex(self):
        print("\n######## Start Simplex ###########\n")
        while True:
            InvB = np.linalg.inv(self.get_B())
            N = self.get_N()

            z, z_var = self.computeZ(InvB, N)

            Xb = self.computeXb(InvB, N)

            if self.is_optimal(z_var):
                print("Optimal solution found..")
                self.calculate_value(z, Xb)
                return z

            ev_idx = self.compute_entering_variable(z_var)
            if ev_idx is None:
                print("Invalid")
                return 0

            ev = self.A[:, ev_idx]
            dv_idx = self.min_ratio_test(Xb, ev)

            if dv_idx is None:
                print("Invalid")
                return 0

            self.replace(ev_idx, dv_idx)
