from collections import defaultdict
from random import uniform
from math import sqrt
from matplotlib import pyplot as plt

import pandas as pd
import random


def point_avg(points):
    """
    Accepts a list of points, each with the same number of dimensions.
    NB. points can have more dimensions than 2

    Returns a new point which is the center of all the points.
    """
    dimensions = len(points[0])

    new_center = []

    for dimension in range(dimensions):
        dim_sum = 0  # dimension sum
        for p in points:
            dim_sum += p[dimension]

        # average of each dimension
        new_center.append(dim_sum / float(len(points)))

    return new_center


def update_centers(data_set, assignments):
    """
    Accepts a dataset and a list of assignments; the indexes
    of both lists correspond to each other.

    Compute the center for each of the assigned groups.

    Return `k` centers where `k` is the number of unique assignments.
    """
    new_means = defaultdict(list)
    centers = []
    for assignment, point in zip(assignments, data_set):
        new_means[assignment].append(point)

    for points in new_means.values():
        centers.append(point_avg(points))

    return centers


def assign_points(data_points, centers):
    """
    Given a data set and a list of points betweeen other points,
    assign each point to an index that corresponds to the index
    of the center point on it's proximity to that point.
    Return a an array of indexes of centers that correspond to
    an index in the data set; that is, if there are N points
    in `data_set` the list we return will have N elements. Also
    If there are Y points in `centers` there will be Y unique
    possible values within the returned list.
    """
    assignments = []
    for point in data_points:
        shortest = float("+inf")  # positive infinity
        shortest_index = 0
        for i in range(len(centers)):
            val = distance(point, centers[i])
            if val < shortest:
                shortest = val
                shortest_index = i
        assignments.append(shortest_index)
    return assignments


def distance(a, b):
    """
    """
    dimensions = len(a)

    _sum = 0
    for dimension in range(dimensions):
        difference_sq = (a[dimension] - b[dimension]) ** 2
        _sum += difference_sq
    return sqrt(_sum)


def generate_k(data_set, k):
    """
    Given `data_set`, which is an array of arrays,
    find the minimum and maximum for each coordinate, a range.
    Generate `k` random points between the ranges.
    Return an array of the random points within the ranges.
    """
    centers = []
    dimensions = len(data_set[0])
    min_max = defaultdict(int)

    for point in data_set:
        for i in range(dimensions):
            val = point[i]
            min_key = 'min_%d' % i
            max_key = 'max_%d' % i
            if min_key not in min_max or val < min_max[min_key]:
                min_max[min_key] = val
            if max_key not in min_max or val > min_max[max_key]:
                min_max[max_key] = val

    for _k in range(k):
        rand_point = []
        for i in range(dimensions):
            min_val = min_max['min_%d' % i]
            max_val = min_max['max_%d' % i]

            rand_point.append(uniform(min_val, max_val))

        centers.append(rand_point)

    return centers


def k_means(dataset, k):
    k_points = generate_k(dataset, k)
    assignments = assign_points(dataset, k_points)
    old_assignments = None
    while assignments != old_assignments:
        new_centers = update_centers(dataset, assignments)
        old_assignments = assignments
        assignments = assign_points(dataset, new_centers)

    result = dict()
    for assign_num, point in zip(assignments, dataset):
        if assign_num not in result:
            result[assign_num] = [[point[i]] for i in range(len(point))]
        else:
            for i in range(len(point)):
                result[assign_num][i].append(point[i])
    return result, new_centers


def visualize_clusters(clusters, information):
    """
    Visualizes the first 2 dimensions of the data as a 2-D scatter plot.
    """
    plt.figure()
    colors = ["#%06x" % random.randint(0, 0xFFFFFF) for _ in range(len(clusters))]
    for num_cluster in clusters:
        plt.plot(clusters[num_cluster][0], clusters[num_cluster][1], 'o', color=colors[num_cluster], markersize=2)

    for num_cluster in clusters:
        for i in range(0, len(clusters[num_cluster][0]), 111):

            point = (clusters[num_cluster][0][i], clusters[num_cluster][1][i])

            for j in range(len(information)):

                if point[0] == information[j][0] and point[1] == information[j][1]:
                    dx = random.randint(-1, 1)
                    dy = random.randint(-1, 1)

                    plt.annotate(information[j][2], xy=(clusters[num_cluster][0][i], clusters[num_cluster][1][i]),
                                 xytext=(clusters[num_cluster][0][i] + (dx * 40), clusters[num_cluster][1][i] + (dy * 6)))

                    break



    plt.show()


fixed_df = pd.read_csv('aids.csv', sep=',', encoding='utf8')


cases = [int(case.replace(",", "")) for case in fixed_df['Cases'].to_list()]
rates = [rate for rate in fixed_df['Rate'].to_list()]
population = [p.replace(",", "").replace('\'', "") for p in fixed_df['Population'].to_list()]


clusters, centers = k_means(list(zip(cases, rates)), 4)
visualize_clusters(clusters, list(zip(cases, rates, population)))

