#!python
# -*- coding: utf-8 -*-

from bio.format import *
import argparse
import sys

class NeedlemanWunsch(object):

    def __init__(self,seq_a,seq_b, substution_matrix = None, position_dict = None,  gap_penalty=-5,func_match=None):
        self.seq_a = seq_a
        self.seq_b = seq_b

        self.substution_matrix = substution_matrix
        self.position_dict = position_dict

        self.gap_penalty = gap_penalty

        self.func_match = func_match

        self.rows = len(seq_a) + 1
        self.cols = len(seq_b) + 1

        self.matrix = [[0 for _ in range(self.cols)] for _ in range(self.rows)]

    @property
    def score(self):
        return self.matrix[self.rows - 1][self.cols - 1]

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

        self.matrix[0][0] = 0
        for i in range(self.rows):
            self.matrix[i][0] = i * self.gap_penalty
        for j in range(self.cols):
            self.matrix[0][j] = j * self.gap_penalty

        for i in range(1,self.rows):
            for j in range(1,self.cols):
                self.matrix[i][j] = max(self.matrix[i-1][j-1] + self.s(i,j),
                                        self.matrix[i-1][j] + self.gap_penalty,
                                        self.matrix[i][j-1] + self.gap_penalty)

    def get_align(self):
        seq_a_align,seq_b_align = "", ""

        i = self.rows - 1
        j = self.cols - 1

        while i > 0 and j > 0:
            if self.matrix[i][j] == (self.matrix[i-1][j-1] + self.s(i,j)):
                seq_a_align = self.seq_a[i-1] + seq_a_align
                seq_b_align = self.seq_b[j-1] + seq_b_align
                i -= 1
                j -= 1
            elif self.matrix[i][j] == self.matrix[i-1][j] + self.gap_penalty:
                seq_a_align = self.seq_a[i-1] + seq_a_align
                seq_b_align = "-" + seq_b_align
                i -= 1
            elif self.matrix[i][j] == self.matrix[i][j-1] + self.gap_penalty:
                seq_a_align = "-"  + seq_a_align
                seq_b_align = self.seq_b[j-1] + seq_b_align
                j -= 1
        while i > 0:
            seq_a_align = self.seq_a[i - 1] + seq_a_align
            seq_b_align = "-" + seq_b_align
            i -= 1
        while j > 0:
            seq_a_align = "-" + seq_a_align
            seq_b_align = self.seq_b[j-1] + seq_b_align
            j -= 1

        return seq_a_align,seq_b_align

class Hirschberg(object):

    def __init__(self,seq_a,seq_b, substution_matrix = None, position_dict = None,  gap_penalty=-5,func_match=None):
        self.seq_a = seq_a
        self.seq_b = seq_b

        self.substution_matrix = substution_matrix
        self.position_dict = position_dict

        self.func_match = func_match

        self.gap_penalty = gap_penalty

    def s(self,i,j):
        if self.substution_matrix is not None:
            index_i = self.position_dict[i]
            index_j = self.position_dict[j]
            return self.substution_matrix[index_i][index_j]
        elif self.func_match is not None:
            return self.func_match(i,j)
        else:
            return 1 if i == j else -1

    # Returns last row of matrix filled by Needleman-Wunsch algorythm
    def last_row(self,seq_a,seq_b):

        len_a = len(seq_a)
        len_b = len(seq_b)

        pre_row = [0] * (len_b + 1)
        cur_row = [0] * (len_b + 1)

        for j in range(1, len_b + 1):
            pre_row[j] = pre_row[j - 1] + self.gap_penalty

        for i in range(1, len_a + 1):
            cur_row[0] = self.gap_penalty + pre_row[0]
            for j in range(1, len_b + 1):
                score_sub = pre_row[j - 1] + self.s(seq_a[i - 1], seq_b[j - 1])
                score_del = pre_row[j] + self.gap_penalty
                score_ins = cur_row[j - 1] + self.gap_penalty
                cur_row[j] = max(score_sub, score_del, score_ins)

            pre_row = cur_row
            cur_row = [0] * (len_b + 1)

        return pre_row

    # Recursively divides sequence until small sequence with less length where finding best alignment is trivial task.
    def align_rec(self,seq_a,seq_b):
        aligned_a, aligned_b = "", ""

        len_a = len(seq_a)
        len_b = len(seq_b)

        if len_a == 0:
            for i in range(len_b):
                aligned_a = "-" + aligned_a
                aligned_b = seq_b[i] + aligned_b
        elif len_b == 0:
            for i in range(len_a):
                aligned_a = seq_a[i] + aligned_a
                aligned_b = "-" + aligned_b

        elif len(seq_a) == 1 or len(seq_b) == 1:
            self.needleman = NeedlemanWunsch(seq_a,seq_b,self.substution_matrix,self.position_dict,self.gap_penalty,self.func_match)
            self.needleman.compute_matrix()
            aligned_a, aligned_b = self.needleman.get_align()

        else:

            # Divide and Conquer

            mid_a = int(len_a / 2)

            rowleft = self.last_row(seq_a[:mid_a], seq_b)
            rowright = self.last_row(seq_a[mid_a:][::-1], seq_b[::-1])

            rowright.reverse()

            # Getting maximum
            row = [l + r for l, r in zip(rowleft, rowright)]
            maxidx, maxval = max(enumerate(row), key=lambda a: a[1])

            mid_b = maxidx

            # Recursive calls

            aligned_a_left, aligned_b_left = self.align_rec(seq_a[:mid_a], seq_b[:mid_b])
            aligned_a_right, aligned_b_right = self.align_rec(seq_a[mid_a:], seq_b[mid_b:])
            aligned_a = aligned_a_left + aligned_a_right
            aligned_b = aligned_b_left + aligned_b_right

        return aligned_a, aligned_b

    def align(self,seq_a,seq_b):
        return self.align_rec(seq_a, seq_b)

class SmithWaterman(object):

    def __init__(self,seq_a,seq_b, substution_matrix = None, position_dict = None,  gap_penalty=-5,func_match=None):
        self.seq_a = seq_a
        self.seq_b = seq_b

        self.substution_matrix = substution_matrix
        self.position_dict = position_dict

        self.gap_penalty = gap_penalty

        self.func_match = func_match

        self.rows = len(seq_a) + 1
        self.cols = len(seq_b) + 1

        self.max_x = 0
        self.max_y = 0
        self.max_value = 0

        self.matrix = [[0 for _ in range(self.cols)] for _ in range(self.rows)]

    @property
    def score(self):
        return self.matrix[self.rows-1][self.cols-1]

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

        self.matrix[0][0] = 0
        for i in range(self.rows):
            self.matrix[i][0] = 0
        for j in range(self.cols):
            self.matrix[0][j] = 0

        for i in range(1,self.rows):
            for j in range(1,self.cols):
                self.matrix[i][j] = max(0,
                                        self.matrix[i-1][j-1] + self.s(i,j),
                                        self.matrix[i-1][j] + self.gap_penalty,
                                        self.matrix[i][j-1] + self.gap_penalty)
                if self.matrix[i][j] >= self.max_value:
                    self.max_value = self.matrix[i][j]
                    self.max_x = i
                    self.max_y = j

    def get_align(self):
        seq_a_align,seq_b_align = "", ""

        i = self.max_x
        j = self.max_y

        while self.matrix[i][j] != 0:
            if i > 0 and j > 0:
                if self.matrix[i][j] == (self.matrix[i-1][j-1] + self.s(i,j)):
                    seq_a_align = self.seq_a[i-1] + seq_a_align
                    seq_b_align = self.seq_b[j-1] + seq_b_align
                    i -= 1
                    j -= 1
                    continue
            if i > 0:
                if self.matrix[i][j] == self.matrix[i-1][j] + self.gap_penalty:
                    seq_a_align = self.seq_a[i-1] + seq_a_align
                    seq_b_align = "-" + seq_b_align
                    i -= 1
                    continue
            if j > 0:
                if self.matrix[i][j] == self.matrix[i][j-1] + self.gap_penalty:
                    seq_a_align = "-"  + seq_a_align
                    seq_b_align = self.seq_b[j-1] + seq_b_align
                    j -= 1
                    continue

        return seq_a_align,seq_b_align


def new_parser():
    parser = argparse.ArgumentParser(description="Description of using script")
    parser.add_argument('-alg',dest="algorithm",type=str,help="Compute alignment by certain algorithm")
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
        if args.algorithm not in ["sw","nw","hb"]:
            sys.stderr.write('Value of algorithm must take such values: sw(Smith-Waterman),nw(Needleman-Wunsch),hb(Hirschberg)')
            sys.exit(1)

        elif args.algorithm == "sw":

            seq = read_file(args.file)

            a = seq[0].seq
            b = seq[1].seq

            if args.matrix is not None:
                print("Algorithm Smith-Waterman")
                matrix, pos_dict = load_matrix(args.matrix)

                gap = -5 if args.gap is None else args.gap

                smithWaterman = SmithWaterman(a, b,substution_matrix=matrix,position_dict=pos_dict,gap_penalty=gap)
                smithWaterman.compute_matrix()

                output(smithWaterman.get_align(),smithWaterman.score)

            elif args.match is not None and args.mismatch is not None:
                print("Algorithm Smith-Waterman")
                func_match = lambda x,y: args.match if x == y else args.mismatch

                gap = -5 if args.gap is None else args.gap

                smithWaterman = SmithWaterman(a, b, func_match=func_match,gap_penalty=gap)
                smithWaterman.compute_matrix()

                output(smithWaterman.get_align(), smithWaterman.score)
            else:
                sys.stderr.write('You must include file_name of matrix (table of value match/mismatch) or manually input value of match and mismatch')
                sys.exit(1)

        elif args.algorithm == "nw":

            seq = read_file(args.file)

            a = seq[0].seq
            b = seq[1].seq

            if args.matrix is not None:
                print("Algorithm Needleman-Wunsch")
                matrix, pos_dict = load_matrix(args.matrix)

                gap = -5 if args.gap is None else args.gap

                needlemanWinsch = NeedlemanWunsch(a, b, substution_matrix=matrix, position_dict=pos_dict, gap_penalty=gap)
                needlemanWinsch.compute_matrix()

                output(needlemanWinsch.get_align(), needlemanWinsch.score)

            elif args.match is not None and args.mismatch is not None:
                print("Algorithm Needleman-Wunsch")
                func_match = lambda x, y: args.match if x == y else args.mismatch

                gap = -5 if args.gap is None else args.gap

                needlemanWinsch = NeedlemanWunsch(a, b, func_match=func_match, gap_penalty=gap)
                needlemanWinsch.compute_matrix()

                output(needlemanWinsch.get_align(), needlemanWinsch.score)
            else:
                sys.stderr.write(
                    'You must include file_name of matrix (table of value match/mismatch) or manually input value of match and mismatch')
                sys.exit(1)

        elif args.algorithm == "hb":

            seq = read_file(args.file)

            a = seq[0].seq
            b = seq[1].seq

            if args.matrix is not None:
                print("Algorithm Hirschberg")
                matrix, pos_dict = load_matrix(args.matrix)

                gap = -5 if args.gap is None else args.gap

                hirschberg = Hirschberg(a, b, substution_matrix=matrix, position_dict=pos_dict, gap_penalty=gap)

                output(hirschberg.align(a,b))

            elif args.match is not None and args.mismatch is not None:
                print("Algorithm Hirschberg")
                func_match = lambda x, y: args.match if x == y else args.mismatch

                gap = -5 if args.gap is None else args.gap

                hirschberg = Hirschberg(a, b, func_match=func_match, gap_penalty=gap)

                output(hirschberg.align(a,b))
            else:
                sys.stderr.write(
                    'You must include file_name of matrix (table of value match/mismatch) or manually input value of match and mismatch')
                sys.exit(1)

    else:
        sys.stderr.write('Usage: python3 lab1.py name_file.fasta -alg name_of_algorithm -m value -mm value -g value -f name_file')
        sys.exit(1)

