#!python -v
# -*- coding: utf-8 -*-

from scipy.integrate import solve_ivp
import numpy as np
import math
from matplotlib import pyplot as plt

C = 0.15  # баллистический коэффициент

rho_lead = 11340  # kg/m^3 - плотность меди
rho_air = 1.29  # kg/m^3 - плотность воздуха

v0_1 = 10  # начальная скорость

diam = 0.5  # диаметр шарика
rad = diam / 2  # радиус шарика
alpha = math.pi / 4  # угол в радианах

t_0 = 0  # время начала
t_max = 100  # время конца

S = math.pi * (rad ** 2)  # площадь поперечного сечения
beta = 0.1  # C * rho_air * S / 2
V = (4 / 3) * math.pi * (rad ** 3)  # объем шара
m = rho_lead * V  # масса шара

v0 = 200  # тестирование начальной скорости

eps = 1.e-4
g = 9.8  # m/sec^2

# Начальные условия
u0 = v0 * math.cos(alpha)
w0 = v0 * math.sin(alpha)
x0 = 0
y0 = 0


# функции по модели Галилея:
def x(t):
    return v0 * math.cos(alpha) * t


def y(t):
    return v0 * math.sin(alpha) * t - g * (t ** 2) / 2


# описание системы модели Ньютона:
def right_part(t, system):
    (u, w, x, y) = system
    factor = -beta * math.sqrt(u ** 2 + w ** 2) / m
    return np.ndarray((4,), buffer=np.array([u * factor, w * factor - g, u, w]))


def init_system():
    return np.ndarray((4,), buffer=np.array([u0, w0, x0, y0]))


coords = solve_ivp(right_part, (t_0, t_max), init_system(), max_step=eps)

# удаляем все точки, где x < 0
def trim(arr):
    M = np.where(arr[1] >= 0)[-1][-1]
    return arr[:M, :M]


print(coords)
t_arr = coords['t']
coords = coords['y'][2:]

print(trim(coords))
coords = trim(coords)

xvals, yvals = coords

gal_xvals = [x(t) for t in t_arr]
gal_yvals = [y(t) for t in t_arr]
print(t_arr)
gal_coords = np.ndarray((2, len(gal_xvals)), buffer=np.array([gal_xvals, gal_yvals]))
gal_coords = trim(gal_coords)
gal_xvals, gal_yvals = gal_coords
print("Galilei: (", gal_xvals[-1], ", ", gal_yvals[-1], ")", sep='')
print("Newton : (", xvals[-1], ", ", yvals[-1], ")", sep='')

plt.plot(gal_xvals, gal_yvals, 'r-', label='Galileo model')
plt.plot(xvals, yvals, 'b', label='Newton model')
# plt.axis([0, 3, 0, 0.30])
plt.legend()
plt.ylabel('y')
plt.xlabel('x')
plt.show()
