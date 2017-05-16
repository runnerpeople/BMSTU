#!python
# -*- coding: utf-8 -*-

import abc
import math

class RungeKutta(object):
    __metaclass__ = abc.ABCMeta

    def __init__(self,t0,y0):
        self.t = t0
        self.y = y0
        self.yy = [0,0]
        self.y1 = [0,0]
        self.y2 = [0,0]
        self.y3 = [0,0]
        self.y4 = [0,0]

    @abc.abstractclassmethod
    def f(self,t,y):
        pass

    def nextStep(self,dt):
        if dt < 0:
            return None

        self.y1 = self.f(self.t, self.y)
        for i in range(2):
            self.yy[i] = self.y[i] + self.y1[i] * (dt / 2.0)

        self.y2 = self.f(self.t + dt / 2.0, self.yy)
        for i in range(2):
            self.yy[i] = self.y[i] + self.y2[i] * (dt / 2.0)

        self.y3 = self.f(self.t + dt / 2.0, self.yy)
        for i in range(2):
            self.yy[i] = self.y[i] + self.y3[i] * dt

        self.y4 = self.f(self.t + dt, self.yy)
        for i in range(2):
            self.y[i] = self.y[i] + dt / 6.0 *\
                                    (self.y1[i] + 2.0 * self.y2[i] + 2.0 * self.y3[i] + self.y4[i])

        self.t += dt

class RungeKuttaImpl(RungeKutta):
    def f(self,t,y):
        fy = [0,0]
        fy[0] = y[1]
        fy[1] = math.exp(t) + y[0] - y[1]
        return fy

def func(x):
    return math.exp(x)

if __name__ == "__main__":
    eq = RungeKuttaImpl(0,[1,1])
    dt = 0.1
    while eq.t <= 1.01:
        #print("Solution y'(%.2f)=" %(eq.t) + str(eq.y[1]) + " y(%.2f)=" %(eq.t) + str(eq.y[0]) +
        #      " delta=" + str(func(eq.t)-eq.y[0]))
        print(str(eq.t) + " & " + str(eq.y[0]) + " & " + str(eq.y[1]) + " \\\\ \hline")
        eq.nextStep(dt)
