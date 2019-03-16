#!python
# -*- coding: utf-8 -*-

from bio.format import *
from lab1.lab1 import NeedlemanWunsch
import argparse
import sys
import itertools

class ModifiedNeedlemanWunsch(NeedlemanWunsch):
    def s(self,i,j):
        if self.seq_a[i - 1] == "-" or self.seq_b[j - 1] == "-":
            return self.gap_penalty
        elif self.substution_matrix is not None:
            index_i = self.position_dict[self.seq_a[i - 1]]
            index_j = self.position_dict[self.seq_b[j - 1]]
            return self.substution_matrix[index_i][index_j]
        elif self.func_match is not None:
            return self.func_match(self.seq_a[i - 1],self.seq_b[j - 1])
        else:
            return 1 if self.seq_a[i-1] == self.seq_b[j-1] else -1

def get_max_score(matrix):
    max_value,i_,j_ = 0,0,0
    for i in range(len(matrix)):
        for j in range(len(matrix[0])):
            if abs(matrix[i][j]) > max_value:
                max_value = abs(matrix[i][j])
                i_,j_ = i,j
    return (i_,j_)

def find_value_in_sublist(list,value):
    for i in range(len(list)):
        for j in range(len(list[i])):
            if list[i][j] == value:
                return list[i]
    return None

# Progressive multiple alignment #
def MultiDimensionalAlignment(seq_list,matrix,pos_dict,func_match,gap):

    len_list = len(seq_list)

    # Guide tree of score #
    matrix_ = [[0 for _ in range(len_list)] for _ in range(len_list)]
    for i in range(len_list):
        for j in range(len_list):
            if j >= i:
                continue
            else:
                a = seq_list[i].seq
                b = seq_list[j].seq
                needlemanWinsch = ModifiedNeedlemanWunsch(a, b, substution_matrix=matrix, position_dict=pos_dict,gap_penalty=gap)
                needlemanWinsch.compute_matrix()
                matrix_[i][j] = needlemanWinsch.score


    seq_list_ = list(itertools.chain.from_iterable(itertools.repeat(x, 2) for x in range(len_list)))
    append_list = []

    while len(seq_list_) > 0:
        # Compute alignment of almost the same chains (pairwaise and the same)
        i,j = get_max_score(matrix_)
        if i in seq_list_ or j in seq_list_:
            if find_value_in_sublist(append_list,i) is not None or find_value_in_sublist(append_list,j) is not None:
                if find_value_in_sublist(append_list,i) is not None:
                    result = find_value_in_sublist(append_list,i)
                    prev_value = result[0] if i != result[0] else result[1]
                    needlemanWinsch = ModifiedNeedlemanWunsch(seq_list[j].seq, seq_list[prev_value].seq,
                                                              substution_matrix=matrix,
                                                              position_dict=pos_dict,
                                                              gap_penalty=gap)
                    needlemanWinsch.compute_matrix()
                    align_a, align_b = needlemanWinsch.get_align()
                    seq_list[j].seq = align_a
                    seq_list[prev_value].seq = align_b
                    append_list.remove(result)
                if find_value_in_sublist(append_list,j) is not None:
                    result = find_value_in_sublist(append_list, j)
                    prev_value = result[0] if j != result[0] else result[1]
                    needlemanWinsch = ModifiedNeedlemanWunsch(seq_list[i].seq, seq_list[prev_value].seq,
                                                              substution_matrix=matrix,
                                                              position_dict=pos_dict,
                                                              gap_penalty=gap)
                    needlemanWinsch.compute_matrix()
                    align_a, align_b = needlemanWinsch.get_align()
                    seq_list[i].seq = align_a
                    seq_list[prev_value].seq = align_b
                    append_list.remove(result)
            needlemanWinsch = ModifiedNeedlemanWunsch(seq_list[i].seq, seq_list[j].seq, substution_matrix=matrix,
                                                          position_dict=pos_dict,
                                                          gap_penalty=gap)
            needlemanWinsch.compute_matrix()
            align_a, align_b = needlemanWinsch.get_align()
            seq_list[i].seq = align_a
            seq_list[j].seq = align_b
            if i in seq_list_:
                seq_list_.remove(i)
            if j in seq_list_:
                seq_list_.remove(j)
            append_list.append([i,j])
        matrix_[i][j] = 0

    return seq_list

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

        seq_list = read_file(args.file)

        if args.matrix is not None or (args.match is not None and args.mismatch is not None):
            print("Algorithm Multi-Dimensional Alignment")
            gap = -5 if args.gap is None else args.gap
            if args.matrix is not None:
                matrix, pos_dict = load_matrix(args.matrix)
                aligned = MultiDimensionalAlignment(seq_list,matrix,pos_dict,None,gap)
                output(aligned)
            else:
                func_match = lambda x, y: args.match if x == y else args.mismatch
                aligned = MultiDimensionalAlignment(seq_list,None,None,func_match,gap)
                output(aligned)
        else:
            sys.stderr.write('You must include file_name of matrix (table of value match/mismatch) or manually input value of match and mismatch')
            sys.exit(1)
    else:
        sys.stderr.write('Usage: python3 lab4.py name_file.fasta -m value -mm value -g value -f name_file')
        sys.exit(1)