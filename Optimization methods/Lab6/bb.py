from scipy.optimize import linprog
import numpy as np
from math import floor, ceil
import copy

integer_var = [0, 1, 2]

dangling_nodes = []
dangling_nodes_obj = []
z_star = float('-Inf')
node_counter = 0
result_Node = None


class Node:
    def __init__(self, x_bounds=[], freeze_var_list=[], index=0, upper_or_lower=0):
        self._x_bounds = x_bounds
        self._freeze_var_list = freeze_var_list
        self._index = index
        self._upper_or_lower = upper_or_lower

    # print("create Node:", index)
    # print('')

    def freeze_lower_var(self, index, val):
        self._x_bounds[index] = (None, val)
        # self._x_bounds[index][0] = val
        # self._x_bounds[index][1] = val
        self._freeze_var_list.append(index)

    def freeze_upper_var(self, index, val):
        self._x_bounds[index] = (val, None)
        # self._x_bounds[index][0] = val
        # self._x_bounds[index][1] = val
        self._freeze_var_list.append(index)

    def freeze_var(self, index, val):
        self._x_bounds[index] = (val, val)
        # self._x_bounds[index][0] = val
        # self._x_bounds[index][1] = val
        self._freeze_var_list.append(index)

    def set_lp_res(self, res):
        self._res = res

    # s = " "
    # for l in range(len(self._res['x'])):
    # 	if l in self._freeze_var_list:
    # 		s += "[" + str(self._res['x'][l]) + "] "
    # 	elif l in integer_var:
    # 		s += "\'" + str(self._res['x'][l]) + "\' "
    # 	else:
    # 		s += str(self._res['x'][l])
    # print("x: ", res.x)

    def check_integer_var_all_solved(self, m):
        return True if m == len(self._freeze_var_list) else False


def check_all_integers(list):
    is_integer = True
    for i in range(len(list) - 1):
        if not is_int(list[i]):
            is_integer = False
            break

    return is_integer


def calculate(A, b, c):
    global node_counter

    x_bounds = [(0, None) for i in range(len(c))]

    print("\n######## Start B & B ###########\n")

    node = Node(copy.deepcopy(x_bounds), [], node_counter)

    node_counter += 1
    res = solve_LP(x_bounds, A, b, c)

    if check_all_integers(res.x):
        print(node._x_bounds)
        print_result(res)
        return res

    lower = floor(res['x'][integer_var[0]])
    upper = lower + 1

    lower_node = Node(copy.deepcopy(x_bounds), [], node_counter, 1)
    lower_node.freeze_lower_var(integer_var[0], lower)
    add_dangling_node(lower_node, A, b, c)

    node_counter += 1

    upper_node = Node(copy.deepcopy(x_bounds), [], node_counter, 2)
    upper_node.freeze_upper_var(integer_var[0], upper)
    add_dangling_node(upper_node, A, b, c)

    node_counter += 1

    while len(dangling_nodes) > 0:

        index = np.argmin(dangling_nodes_obj)

        x_b = dangling_nodes[index]._x_bounds
        frez = dangling_nodes[index]._freeze_var_list
        res = dangling_nodes[index]._res
        frez_var_index = len(frez)

        u_or_l = dangling_nodes[index]._upper_or_lower
        arbitrary_node = Node(copy.deepcopy(x_b), copy.deepcopy(frez), node_counter, copy.deepcopy(u_or_l))
        u_or_l_b = lower - 1 if (u_or_l == 1) else upper + 1
        arbitrary_node.freeze_var(integer_var[frez_var_index - 1], u_or_l_b)
        x_b_arbi = arbitrary_node._x_bounds
        if check_bounds(x_b_arbi, integer_var[frez_var_index - 1], u_or_l, x_bounds):
            add_dangling_node(arbitrary_node, A, b, c)
        else:
            print("arbitrary Node infeasibile: ", arbitrary_node._index)

        node_counter += 1

        lower = floor(res['x'][integer_var[frez_var_index]])
        upper = lower + 1

        lower_node = Node(copy.deepcopy(x_b), copy.deepcopy(frez), node_counter, 1)
        lower_node.freeze_lower_var(integer_var[frez_var_index], lower)
        add_dangling_node(lower_node, A, b, c)

        node_counter += 1

        upper_node = Node(copy.deepcopy(x_b), copy.deepcopy(frez), node_counter, 2)
        upper_node.freeze_upper_var(integer_var[frez_var_index], upper)
        add_dangling_node(upper_node, A, b, c)

        node_counter += 1

        # убрать break в записке
        # if check_all_integers():
        break

    result = get_max_integer_node()
    print(result._x_bounds)
    print_result(result._res)


def is_int(n):
    return int(n) == float(n)


def solve_LP(x_bounds, A_eq, b_eq, c):
    return linprog(c, A_eq=A_eq, b_eq=b_eq, bounds=x_bounds)


def print_result(result):
    x_list = result.x
    print("z = {}".format(result.fun))

    for i, item in enumerate(x_list):
        print("x[{}] = {}".format(i, item))


def get_max_integer_node():
    global dangling_nodes
    global dangling_nodes_obj
    global result_Node

    integer_nodes = []
    for node in dangling_nodes:
        if check_all_integers(node._res.x):
            integer_nodes.append(node)

    max_node_result = float("-INF")
    max_node = None
    for node in integer_nodes:
        if node._res.fun >= max_node_result:
            max_node = node
            max_node_result = node._res.fun

    return max_node


def add_dangling_node(node, A, b, c):
    global z_star
    global dangling_nodes
    global dangling_nodes_obj
    global result_Node

    res = solve_LP(node._x_bounds, A, b, c)
    if check_feasibility(res) and res['fun'] > z_star:
        node.set_lp_res(res)

        if check_all_integers(res.x):
            dangling_nodes_obj.append(res['fun'])
            dangling_nodes.append(node)
        elif len(dangling_nodes) == 0:
            dangling_nodes_obj.append(res['fun'])
            dangling_nodes.append(node)
        else:
            return

        # dangling_nodes_obj.append(res['fun'])
        # dangling_nodes.append(node)
        if node.check_integer_var_all_solved(len(integer_var)):
            z_star = res['fun']
            result_Node = node
        # print("-----------Temporary sol-----------")
        # print("x: ", result_Node._res['x'])
        # print("z:", result_Node._res['fun'])
        # print("-----------------------------------")
        # print('')
        # print("=> Add node to dangling list: ", node._index)
        # print("=> current dangling nodes:", dangling_nodes_obj)
        # print('')
        return True
    else:
        # print("=> Node infeasibile: ", node._index)
        # print("=> current dangling nodes:", dangling_nodes_obj)
        # print('')
        return False


def check_feasibility(res):
    if res['status'] == 0:
        return True
    elif res['status'] == 2:
        return False
    else:
        raise ("Problem Unbounded")
        exit()


def check_bounds(x_b, index, u_or_l, x_bounds):
    # global x_bounds
    if u_or_l == 1:
        if x_b[index][0] is None and x_bounds[index][0] is not None:
            return False
        elif x_b[index][0] is not None and x_bounds[index][0] is None:
            return True
        elif x_b[index][0] is not None and x_bounds[index][0] is not None:
            return False if (x_b[index][0] < x_bounds[index][0]) else True
    elif u_or_l == 2:
        if x_b[index][1] is None and x_bounds[index][1] is not None:
            return False
        elif x_b[index][1] is not None and x_bounds[index][1] is None:
            return True
        elif x_b[index][0] is not None and x_bounds[index][0] is not None:
            return False if (x_b[index][1] > x_bounds[index][1]) else True
    else:
        print("error of bounds")
        exit()
