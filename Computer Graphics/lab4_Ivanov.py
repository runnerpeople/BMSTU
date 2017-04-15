#!/usr/bin/python3
# -*- coding: utf-8 -*-
__author__="great"

from pyglet import *
from pyglet.gl import *
from pyglet.window import key,mouse
import math
import numpy

class Frame(object):

    def __init__(self,width,height):
        self.points = []
        self.width = width
        self.height = height
        self.pixels = numpy.empty(self.width*self.height*3,dtype=GLfloat)
        self.pixels.fill(0.0)
        self.fill = False
        self.eliminate = False
        self.draw = False



    def draw_simple_line(self):

        glColor3f(1,1,1)
        if len(self.points)>2:
            if self.eliminate:
                self.eliminate_aliase()
                self.eliminate=False
            elif self.fill:
                self.fill_polygon()
                #self.fill = False
            glDrawPixels(self.width,self.height,GL_RGB,GL_FLOAT,(GLfloat * len(self.pixels))(*self.pixels))
        if self.draw:
            for i in range(len(self.points)):
                if i == len(self.points)-1:
                    glBegin(GL_LINES)
                    glVertex2f(*self.points[i].print())
                    glVertex2f(*self.points[0].print())
                    glEnd()
                    break
                glBegin(GL_LINES)
                glVertex2f(*self.points[i].print())
                glVertex2f(*self.points[i+1].print())
                glEnd()
            self.draw=False


    def add_point(self,x,y):
        self.points.append(Point(x,y))
        self.draw=True


    def points_delete(self):
        self.points.clear()
        self.pixels.fill(0.0)

    @staticmethod
    def get_octant(x0,y0,x1,y1):
        dx, dy = x1 - x0, y1 - y0
        octant = 0
        if dy < 0:
            dx, dy = -dx, -dy
            octant += 4
        if dx < 0:
            dx, dy = dy, -dx
            octant += 2
        if dx < dy:
            octant += 1
        return octant

    @staticmethod
    def switch_to_octant(x,y,octant):
        if octant==0:
            x,y=x,y
        elif octant==1:
            x,y=y,x
        elif octant==2:
            x,y=y,-x
        elif octant==3:
            x,y=-x,y
        elif octant==4:
            x,y=-x,-y
        elif octant==5:
            x,y=-y,-x
        elif octant==6:
            x,y=-y,x
        elif octant==7:
            x,y=x,-y
        return x,y

    @staticmethod
    def switch_from_octant(x,y,octant):
        if octant==0:
            x,y=x,y
        elif octant==1:
            x,y=y,x
        elif octant==2:
            x,y=-y,x
        elif octant==3:
            x,y=-x,y
        elif octant==4:
            x,y=-x,-y
        elif octant==5:
            x,y=-y,-x
        elif octant==6:
            x,y=y,-x
        elif octant==7:
            x,y=x,-y
        return x,y

    def set_pixel(self,x,y,brightness,octant=0):
        x,y=self.switch_from_octant(x,y,octant)
        byte = (y*self.width+x)
        for i in range(3):
            self.pixels[byte*3+i]=brightness

    def invert(self,x,y,size=3):
        byte = (y*self.width+x)
        for i in range(size):
            if self.pixels[byte*3+i]==0:
                self.pixels[byte*3+i]=1
            elif self.pixels[byte*3+i]==1:
                self.pixels[byte*3+i]=0

    def fill_polygon(self):

        def fill_edge(x0,y0,x1,y1):
            if y0==y1 or x0==x1:
                return None
            if y0>y1:
                x0,x1,y0,y1 = x1,x0,y1,y0
            line = lambda y: ((y-y0)/(y1-y0))*(x1-x0)+x0
            x_min,x_max = min(x0,x1),max(x0,x1)
            #if x1==x_min:
            #    flag=False
            for y3 in range(y0,y1):
                for x3 in range(x_min,x_max+1):
                    #x=0
                    #if y3==y0:
                    #    x=x0
                    #if not flag:
                    #    x = math.floor(line(y3))
                    #else:
                    x = math.ceil(line(y3))
                    for z in range(x,self.width):
                        self.invert(z,y3)
                    break

        for i in range(len(self.points)):
            if i==len(self.points)-1:
                x0,y0 = self.points[i].print()
                x1,y1 = self.points[0].print()
                fill_edge(x0,y0,x1,y1)
                break
            else:
                x0,y0 = self.points[i].print()
                x1,y1 = self.points[i+1].print()
                fill_edge(x0,y0,x1,y1)

    def eliminate_aliase(self):

        def bresenham(x0,y0,x1,y1):
            if y1==y0 or x1==x0:
                return None
            dx, dy = abs(x1-x0),abs(y1-y0)
            sign_x, sign_y = 1 if x0 < x1 else -1, 1 if y0 < y1 else -1
            swap = False
            if dy > dx:
                dx, dy=dy, dx
                swap = True
            if swap:
                m = dx/dy
            else:
                m = dy/dx
            error = 1/2
            w = 2*error-m
            self.set_pixel(x0,y0,error)
            octant = 0
            if swap:
                octant += 1
            if y1-y0 < 0:
                octant += 4
            if x1-x0 < 0:
                octant += 2
            if swap:
                while y0 != y1:
                    if octant % 2 != 0:
                        self.set_pixel(x0,y0,1-error)
                    else:
                        self.set_pixel(x0,y0,error)
                    y0 += sign_y
                    if error < w:
                        error += m
                    else:
                        x0 += sign_x
                        error -= w

            else:
                while x0 != x1:
                    if octant % 2 != 0:
                        self.set_pixel(x0,y0,1-error)
                    else:
                        self.set_pixel(x0,y0,error)
                    x0 += sign_x
                    if error < w:
                        error += m
                    else:
                        y0 += sign_y
                        error -= w

        def bresenham2(x0,y0,x1,y1):
            if y0==y1 or x0==x1:
                return None
            octant = self.get_octant(x0,y0,x1,y1)
            x0,y0 = self.switch_to_octant(x0,y0,octant)
            x1,y1 = self.switch_to_octant(x1,y1,octant)
            if x0>x1:
                x0,x1,y0,y1=x1,x0,y1,y0
            dx,dy = x1-x0,y1-y0
            l = 1
            m = l*dy/dx
            w = l - m
            error = 1/2
            self.set_pixel(x0,y0,1/2,octant)
            y=y0
            for i in range(x0,x1+1):
                if octant%2!=0:
                    self.set_pixel(i,y,(l-error),octant)
                else:
                    self.set_pixel(i,y,error,octant)
                if error<w:
                    error+=m
                else:
                    error-=w
                    y+=1


        for i in range(len(self.points)):
            if i==len(self.points)-1:
                x0,y0 = self.points[i].print()
                x1,y1 = self.points[0].print()
                bresenham2(x0,y0,x1,y1)
                break
            else:
                x0,y0 = self.points[i].print()
                x1,y1 = self.points[i+1].print()
                bresenham2(x0,y0,x1,y1)

    def print_points(self):
        print("================")
        print("glBegin(GL_LINE_LOOP)")
        for i in range(len(self.points)):
            print("glVertex2f(%d,%d)" % (self.points[i].x,self.points[i].y))
            print("self.points.append(Point(%d,%d))" % (self.points[i].x,self.points[i].y))
        print("glEnd()")
        print("================")


    def resize(self,new_width,new_height):
        self.width = new_width
        self.height = new_height
        self.pixels = numpy.empty(self.width*self.height*3,dtype=GLfloat)

        #self.pixels.fill(0.0)


class Point(object):

    def __init__(self,x,y):
        self.x=x
        self.y=y

    def print(self):
        return (self.x,self.y)

    def __str__(self):
        return "(x,y) : %f %f" % (self.x,self.y)

window = pyglet.window.Window(800, 600, resizable=True, caption='Lab 4')
pyglet.gl.glClearColor(0,0,0,0)
frame = Frame(800,600)

@window.event
def on_draw():
    window.clear()
    frame.draw_simple_line()

@window.event
def on_key_press(symbol,modifiers):
    try:
        points = on_key_press.points
    except:
        on_key_press.points = False
        points = on_key_press.points
    finally:
        if symbol == key.P:
            if points:
                on_key_press.points = False
            else:
                on_key_press.points = True
        elif symbol == key.SPACE:
            frame.points_delete()
        elif symbol == key.ENTER:
            frame.fill = True
        elif symbol == key.S:
            frame.eliminate=True
        elif symbol == key.O:
            frame.print_points()


@window.event
def on_mouse_press(x,y,button,modifiers):
    if button & mouse.LEFT:
        try:
            points_mode = on_key_press.points
        except:
            on_key_press.points = False
            points_mode = on_key_press.points
        finally:
            if points_mode:
                frame.add_point(x,y)


@window.event
def on_resize(width,height):
    if height < 0:
        height = 1
    frame.resize(width,height)
    glViewport(0,0,width,height)
    glMatrixMode(GL_PROJECTION)
    glLoadIdentity()
    gluOrtho2D(0,width,0,height)
    glMatrixMode(GL_MODELVIEW)


if __name__ == '__main__':
    pyglet.app.run()
