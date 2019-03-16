#!python
# -*- coding: utf-8 -*-

from bio.format import *
import argparse
import sys


class NeedlemanWunsch(object):
    def __init__(self, seq_a, seq_b, substution_matrix = None, position_dict = None, gap_penalty=-5,func_match = None):
        self.seq_a = seq_a
        self.seq_b = seq_b

        self.substution_matrix = substution_matrix
        self.position_dict = position_dict

        self.gap_penalty = gap_penalty

        self.rows = len(seq_a) + 1
        self.cols = len(seq_b) + 1

        self.func_match = func_match

        """Начальное значение k выбирается как максимум разности длин и числа 3. 3 -- счастливое число"""
        self.k = max(len(self.seq_a) - len(self.seq_b) + 1, 3)

        """Матрица иницализируется минимальным возможным значением."""
        self.matrix = [[float("-inf") for _ in range(self.cols)] for _ in range(self.rows)]

    def s(self, i, j):
        if self.substution_matrix is not None:
            index_i = self.position_dict[self.seq_a[i - 1]]
            index_j = self.position_dict[self.seq_b[j - 1]]
            return self.substution_matrix[index_i][index_j]
        elif self.func_match is not None:
            return self.func_match(self.seq_a[i - 1],self.seq_b[j - 1])
        else:
            return 1 if self.seq_a[i-1] == self.seq_b[j-1] else -1

    def compute_matrix(self):

        self.matrix[0][0] = 0

        """Проверяем, не вышла ли наша полоса за границы матрицы"""
        if self.k > self.rows or self.k > self.cols:
            self.k = min(self.rows, self.cols)

        for i in range(self.k):
            self.matrix[i][0] = i * self.gap_penalty
        for j in range(self.k):
            self.matrix[0][j] = j * self.gap_penalty

        for i in range(1, self.k):
            for j in range(1, self.k):
                self.matrix[i][j] = max(self.matrix[i - 1][j - 1] + self.s(i, j),
                                        self.matrix[i - 1][j] + self.gap_penalty,
                                        self.matrix[i][j - 1] + self.gap_penalty)

        i = 2
        j = 2
        while i != self.rows and j != self.cols:
            indexRow = self.rows - 1
            indexCol = self.cols - 1
            if (i + self.k - 1) < self.rows:
                indexRow = i + self.k - 1
            if (j + self.k - 1) < self.cols:
                indexCol = j + self.k - 1
            self.matrix[indexRow][indexCol] = max(self.matrix[indexRow - 1][indexCol - 1] + self.s(indexRow, indexCol),
                                                  self.matrix[indexRow - 1][indexCol] + self.gap_penalty,
                                                  self.matrix[indexRow][indexCol - 1] + self.gap_penalty)
            i += 1
            j += 1

    def get_align(self):
        seq_a_align, seq_b_align = "", ""

        i = self.rows - 1
        j = self.cols - 1

        gap_counter = 0
        while i > 0 and j > 0:
            if self.matrix[i][j] == (self.matrix[i - 1][j - 1] + self.s(i, j)):
                seq_a_align = self.seq_a[i - 1] + seq_a_align
                seq_b_align = self.seq_b[j - 1] + seq_b_align
                i -= 1
                j -= 1
            elif self.matrix[i][j] == self.matrix[i - 1][j] + self.gap_penalty:
                seq_a_align = self.seq_a[i - 1] + seq_a_align
                seq_b_align = "-" + seq_b_align
                i -= 1
                gap_counter += 1
                if gap_counter >= (self.k - 1):
                    self.k *= 2
                    self.compute_matrix()
                    return "", ""
            elif self.matrix[i][j] == self.matrix[i][j - 1] + self.gap_penalty:
                seq_a_align = "-" + seq_a_align
                seq_b_align = self.seq_b[j - 1] + seq_b_align
                j -= 1
                gap_counter += 1
                if gap_counter >= (self.k - 1):
                    self.k *= 2
                    self.compute_matrix()
                    return "", ""
        while i > 0:
            seq_a_align = self.seq_a[i - 1] + seq_a_align
            seq_b_align = "-" + seq_b_align
            i -= 1
        while j > 0:
            seq_a_align = "-" + seq_a_align
            seq_b_align = self.seq_b[j - 1] + seq_b_align
            j -= 1

        return seq_a_align, seq_b_align

    @property
    def score(self):
        i = 0
        j = 0
        while i<self.rows and j<self.cols and self.matrix[i+1][j+1] != float("-inf"):
            i+=1
            j+=1
        return self.matrix[i][j]


def new_parser():
    parser = argparse.ArgumentParser(description="Description of using script")
    parser.add_argument('-m', dest="match", type=int, help="Value of match")
    parser.add_argument('-mm',dest="mismatch",type=int, help = "Value of mismatch")
    parser.add_argument('-f',dest="matrix",type=str,help="File, which contains table of match/mismatch")
    parser.add_argument('-g',dest="gap",type=int, help="Value of gap penalty")
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

            gap = -5 if args.gap is None else args.gap

            needlemanWunsch = NeedlemanWunsch(a, b,substution_matrix=matrix,position_dict=pos_dict,gap_penalty=gap)
            needlemanWunsch.compute_matrix()

            seq_a_align = ""
            seq_b_align = ""
            while (seq_a_align == "" and seq_b_align == ""):
                seq_a_align, seq_b_align = needlemanWunsch.get_align()
            output([seq_a_align,seq_b_align],needlemanWunsch.score)

        elif args.match is not None and args.mismatch is not None:
            print("Algorithm Needleman-Wunsch")
            func_match = lambda x,y: args.match if x == y else args.mismatch

            gap = -5 if args.gap is None else args.gap

            needlemanWunsch = NeedlemanWunsch(a, b, func_match=func_match,gap_penalty=gap)
            needlemanWunsch.compute_matrix()

            seq_a_align = ""
            seq_b_align = ""
            while (seq_a_align == "" and seq_b_align == ""):
                seq_a_align, seq_b_align = needlemanWunsch.get_align()

            output([seq_a_align,seq_b_align], needlemanWunsch.score)
        else:
            sys.stderr.write('You must include file_name of matrix (table of value match/mismatch) or manually input value of match and mismatch')
            sys.exit(1)
    else:
        sys.stderr.write('Usage: python3 lab2.py name_file.fasta -m value -mm value -g value -f name_file')
        sys.exit(1)
