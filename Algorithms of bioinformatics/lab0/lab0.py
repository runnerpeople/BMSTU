#!/python
# -*- coding: utf-8 -*-

from bio.format import *
import sys

def insertToStr(str,pos):
    return str[:pos] + "-" + str[pos:]

def isCorner(result,i,j):
    return result[i-1][j-1] == result[i-1][j] == result[i][j-1] \
           and result[i-1][j-1] != result[i][j] and result[i-1][j] != result[i][j] and result[i][j-1] != result[i][j]

def countMax(a,b,result,i_max,j_max):
    for i in range(1,j_max):
        for j in range(1,i_max):
            if a[j-1] == b[i-1]:
                result[i][j] = result[i-1][j-1]+1
            else:
                result[i][j] = max(result[i-1][j],result[i][j-1])

def maxSeq(result,i_max,j_max,str_min):
    last_value = result[i_max-1][j_max-1]
    if last_value == 0:
        return ""
    j = j_max - 1
    i = i_max - 1
    while result[i-1][j-1]==last_value:
        i-=1
        j-=1
    str = str_min[i-1]
    i-=1
    j-=1
    while last_value != 1:
        if result[i][j]<last_value and (isCorner(result,i,j)):
            str= str + str_min[i-1]
            last_value = result[i][j]
            j-=1
            i-=1
        else:
            if result[i-1][j]==result[i][j]:
                i-=1
            elif result[i][j-1]==result[i][j]:
                j-=1
    return str[::-1]

def setSkip(a,b,answer):
    a_index = len(a)-1
    b_index = len(b)-1

    i = len(answer)-1
    while i >= 0:
        if b[b_index] != a[a_index] and b[b_index] != '-' and a[a_index] != '-' and a[a_index] == answer[i]:
            a = insertToStr(a,a_index+1)
            a_index = len(a) - 1
            b_index = len(b) - 1
            i = len(answer) - 1
        elif b[b_index] != a[a_index] and b[b_index] != '-' and a[a_index] != '-' and b[b_index] == answer[i]:
            b = insertToStr(b, b_index+1)
            a_index = len(a) - 1
            b_index = len(b) - 1
            i = len(answer) - 1
        elif b[b_index] == '-' or a[a_index] == '-':
            b_index -= 1
            a_index -= 1
        elif a[a_index] != answer[i] and b[b_index] != answer[i]:
            a_count = 0
            b_count = 0

            while a[a_index] != answer[i]:
                a_index -= 1
                a_count += 1

            while b[b_index] != answer[i]:
                b_index -= 1
                b_count += 1

            a_count_s = a_count
            while a_count > 0:
                b = insertToStr(b,b_index+1)
                a_count -= 1
            while b_count > 0:
                a = insertToStr(a,a_index+a_count_s+1)
                b_count -= 1

        else:
            i -= 1
            b_index -= 1
            a_index -= 1

    a_count = 0
    b_count = 0

    while a_index >= 0:
        a_index -= 1
        a_count += 1

    while b_index >= 0:
        b_index -= 1
        b_count += 1

    a_count_s = a_count
    while a_count > 0:
        b = insertToStr(b, b_index + 1)
        a_count -= 1
    while b_count > 0:
        a = insertToStr(a, a_index + a_count_s + 1)
        b_count -= 1

    return a,b

def align(filename):

    seq = read_file(filename)

    a = seq[0].seq
    b = seq[1].seq

    max_str = a if len(a) > len(b) else b
    min_str = b if len(a) > len(b) else a

    max_index = len(max_str) + 1
    min_index = len(min_str) + 1

    result = [[0 for x in range(max_index)] for y in range(min_index)]
    countMax(max_str, min_str, result, max_index, min_index)

    answer = maxSeq(result,min_index,max_index,min_str)
    a,b = setSkip(a,b,answer)

    output([a,b])

if __name__ == "__main__":
    if len(sys.argv) == 2:
        align(sys.argv[1])
    else:
        sys.stderr.write('Usage: python lab0.py file_name.fasta')
        sys.exit(1)