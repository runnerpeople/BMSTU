#!python
# -*- coding: utf-8 -*-

from bio.format import *
import argparse
import sys


class NeedlemanWunsch(object):

    def __init__(self,seq_a,seq_b, substution_matrix = None, position_dict = None, func_match=None,
                 gap_penalty=-10,gap_extend_penalty=-2):
        self.seq_a = seq_a
        self.seq_b = seq_b

        self.substution_matrix = substution_matrix
        self.position_dict = position_dict

        self.func_match = func_match

        self.gap_penalty = gap_penalty
        self.gap_extend_penaly = gap_extend_penalty

        self.min_value = -float("inf")

        self.rows = len(seq_a) + 1
        self.cols = len(seq_b) + 1


        # Algorithm is based on three matrices X (gap in X), Y (gap in Y), M (match between X and Y) #
        self.matrix_x = [[0 for _ in range(self.cols)] for _ in range(self.rows)]
        self.matrix_y = [[0 for _ in range(self.cols)] for _ in range(self.rows)]
        self.matrix_m = [[0 for _ in range(self.cols)] for _ in range(self.rows)]


    def s(self,i,j):
        if self.substution_matrix is not None:
            index_i = self.position_dict[self.seq_a[i - 1]]
            index_j = self.position_dict[self.seq_b[j - 1]]
            return self.substution_matrix[index_i][index_j]
        elif self.func_match is not None:
            return self.func_match(self.seq_a[i - 1],self.seq_b[j - 1])
        else:
            return 1 if self.seq_a[i-1] == self.seq_b[j-1] else -1


    def compute_matrix(self):

        for i in range(self.rows):
            for j in range(self.cols):
                if i > 0 and j == 0:
                    self.matrix_x[i][j]=self.min_value
                else:
                    if j > 0:
                        self.matrix_x[i][j] = self.gap_penalty + self.gap_extend_penaly * j

        for i in range(self.rows):
            for j in range(self.cols):
                if j > 0 and i == 0:
                    self.matrix_y[i][j]=self.min_value
                else:
                    if i > 0:
                        self.matrix_y[i][j] = self.gap_penalty + self.gap_extend_penaly * i

        for i in range(self.rows):
            self.matrix_m[i][0] = self.min_value
        for j in range(self.cols):
            self.matrix_m[0][j] = self.min_value
        self.matrix_m[0][0] = 0

        for i in range(1,self.rows):
            for j in range(1,self.cols):
                self.matrix_x[i][j] = max(self.gap_penalty + self.gap_extend_penaly + self.matrix_m[i][j-1],
                                          self.gap_extend_penaly + self.matrix_x[i][j-1],
                                          self.gap_penalty + self.gap_extend_penaly + self.matrix_y[i][j-1])
                self.matrix_y[i][j] = max(self.gap_penalty + self.gap_extend_penaly + self.matrix_m[i-1][j],
                                          self.gap_penalty + self.gap_extend_penaly + self.matrix_x[i-1][j],
                                          self.gap_extend_penaly + self.matrix_y[i-1][j])
                self.matrix_m[i][j] = max(self.s(i,j) + self.matrix_m[i-1][j-1],
                                          self.matrix_x[i][j],
                                          self.matrix_y[i][j])

    def get_align(self):
        seq_a_align,seq_b_align = "", ""

        i = self.rows - 1
        j = self.cols - 1

        while i > 0 and j > 0:
            if self.matrix_m[i][j] == (self.matrix_m[i-1][j-1] + self.s(i,j)):
                seq_a_align = self.seq_a[i-1] + seq_a_align
                seq_b_align = self.seq_b[j-1] + seq_b_align
                i -= 1
                j -= 1
            elif self.matrix_m[i][j] == self.matrix_x[i][j]:
                seq_a_align = "-" + seq_a_align
                seq_b_align = self.seq_b[j - 1] + seq_b_align
                j -= 1
            elif self.matrix_m[i][j] == self.matrix_y[i][j]:
                seq_a_align = self.seq_a[i - 1] + seq_a_align
                seq_b_align = "-" + seq_b_align
                i -= 1

        return seq_a_align,seq_b_align

    @property
    def score(self):
        return self.matrix_m[self.rows - 1][self.cols - 1]


def new_parser():
    parser = argparse.ArgumentParser(description="Description of using script")
    parser.add_argument('-m', dest="match", type=int, help="Value of match")
    parser.add_argument('-mm',dest="mismatch",type=int, help = "Value of mismatch")
    parser.add_argument('-f',dest="matrix",type=str,help="File, which contains table of match/mismatch")
    parser.add_argument('-gp',dest="gap_penalty",type=int, help="Value of gap penalty")
    parser.add_argument('-ge', dest="gap_extend", type=int, help="Value of gap extend")
    parser.add_argument("file",type=str,help="Name of fasta file, which contains sequences")
    return parser


if __name__ == "__main__":
    arg_parse = new_parser()
    args = arg_parse.parse_args()

    if args.file is not None:

        seq = read_file(args.file)

        a = seq[0].seq
        b = seq[1].seq

        if args.matrix is not None:
            print("Algorithm Needleman-Wunsch")
            matrix, pos_dict = load_matrix(args.matrix)

            gap_penalty = -10 if args.gap_penalty is None else args.gap_penalty
            gap_extend = -2 if args.gap_extend is None else args.gap_extend

            needlemanWunsch = NeedlemanWunsch(a, b,substution_matrix=matrix,position_dict=pos_dict,gap_penalty=gap_penalty,gap_extend_penalty=gap_extend)
            needlemanWunsch.compute_matrix()

            output([needlemanWunsch.get_align()],needlemanWunsch.score)

        elif args.match is not None and args.mismatch is not None:
            print("Algorithm Needleman-Wunsch")
            func_match = lambda x,y: args.match if x == y else args.mismatch

            gap_penalty = -10 if args.gap_penalty is None else args.gap_penalty
            gap_extend = -2 if args.gap_extend is None else args.gap_extend

            needlemanWunsch = NeedlemanWunsch(a, b, func_match=func_match,gap_penalty=gap_penalty,gap_extend_penalty=gap_extend)
            needlemanWunsch.compute_matrix()

            output([needlemanWunsch.get_align()], needlemanWunsch.score)
        else:
            sys.stderr.write('You must include file_name of matrix (table of value match/mismatch) or manually input value of match and mismatch')
            sys.exit(1)
    else:
        sys.stderr.write('Usage: python3 lab3.py name_file.fasta -m value -mm value -gp value -ge value -f name_file')
        sys.exit(1)
