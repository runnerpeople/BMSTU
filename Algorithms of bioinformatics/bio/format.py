#!/python
# -*- coding: utf-8 -*-

import os

def limit_str(string,count=80):
    result = ""
    for start in range(0, len(string), count):
        result += string[start:start + count]
        result += "\n"
    return result

class sequence(object):

    def __init__(self,description,value):
        self.description = description
        self.value = value

    def __eq__(self, other):
        return self.description == other.description and self.value == other.value

    def __ne__(self, other):
        return not (self == other)

    @property
    def seq(self):
        return self.value

    @property
    def name(self):
        return self.description

    @seq.setter
    def seq(self,seq_):
        self.value = seq_

    @name.setter
    def name(self,name_):
        self.description = name_

    def __str__(self):
        return ">" + self.description + "\n" + limit_str(self.value)


def read_file(filename):
    seq = []

    if os.path.isfile(filename):
        with open(filename,"r") as file:
            start_symbol = file.read(1)

            while start_symbol == ">":
                description = file.readline().rstrip()
                value = ""
                c = "\""
                while c and c != ">":
                    c = file.read(1)
                    start_symbol = c
                    if c != "\n" and c != ">":
                        value += c
                seq.append(sequence(description,value))
    else:
        raise FileNotFoundError("Not found \"" + filename + "\"")

    return seq

# Read matrix Matches-Mismatches from file (for example, BLOSUM62)
def load_matrix(filename):
    if os.path.isfile(filename):
        with open(filename, 'r') as file:
            rows = file.read().split("\n")

            header = rows[0].split()
            pos_dict = dict((pair[1], pair[0]) for pair in enumerate(header))

            matrix = [[0 for _ in range(len(pos_dict))] for _ in range(len(pos_dict))]

            for i, row in enumerate(rows[1:]):
                for j, value in list(enumerate(row.split()[1:])):
                    matrix[i][j] = int(value)

        return matrix, pos_dict
    else:
        raise FileNotFoundError("Not found \"" + filename + "\"")

def output(seq_list,score=None):
    if isinstance(seq_list,(list,tuple)):
        if len(seq_list) > 0 and isinstance(seq_list[0],sequence):
            for seq in seq_list:
                print(seq.seq)
                if seq_list[len(seq_list)-1] != seq:
                    print("|" * len(seq.seq))
            if score is not None:
                print("  Score=%d" % score)
        elif len(seq_list) > 0 and isinstance(seq_list[0],str):
            for seq in seq_list:
                print(seq)
                if seq_list[len(seq_list)-1] != seq:
                    print("|" * len(seq))
            if score is not None:
                print("  Score=%d" % score)
    else:
        raise Exception("Parameter of function \"output\"(...) must include list of sequences")
