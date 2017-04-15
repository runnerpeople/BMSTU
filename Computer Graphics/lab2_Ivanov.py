#!/usr/bin/python3
# -*- coding: utf-8 -*-
__author__="great"

from pyglet import *
from pyglet.gl import *
from pyglet.window import key
import pyglet
import math
import sys
import numpy

class Cube(object):
    def __init__(self,a,b):
        self.a = a
        self.b = b
        self.angle_x  = 0
        self.angle_y = 0
        self.angle_z = 0
        self.vertices = [(a,a,a),(b,a,a),(b,b,a),(a,b,a),(a,a,b),(b,a,b),(b,b,b),(a,b,b)]
        self.vertex_edge = [(3,2,1,0),(7,6,5,4),(1,2,6,5),(4,5,1,0),(6,7,3,2),(3,0,4,7)]
        self.color_edge = [(0,0,1),(0,1,0),(1,0,0),(1,0,1),(1,1,0),(0,1,1)]

    def draw(self,flag=True):
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT )
        glLoadIdentity()
        glEnable(GL_DEPTH_TEST)
        #glEnable(GL_CULL_FACE)
        glRotatef(self.angle_x, 1, 0, 0)
        glRotatef(self.angle_y,0,1,0)
        glRotatef(self.angle_z,0,0,1)
        for i in range(6):
            glColor3f(*self.color_edge[i])
            glBegin(GL_QUADS)
            for j in self.vertex_edge[i]:
                glVertex3f(*self.vertices[j])
            glEnd()
        #glDisable(GL_DEPTH_TEST)
        glFlush()
        #self.angle_x += 5
        #self.angle_y += 5
        #self.angle_z += 5


class Simple_tube(object):
    def __init__(self,radius_small,radius_big,height,partion=25):
        self.radius_small = radius_small
        self.radius_big = radius_big
        self.height = height
        self.partion = partion
        self.vertex_small = []
        self.vertex_high_small = []
        self.vertex_big = []
        self.vertex_high_big = []
        self.generate_vertices()

        self.angle_x  = 0
        self.angle_y = 0
        self.angle_z = 0

        self.x = 2
        self.y = 2
        self.z = 2

        self.translate_x = 0
        self.translate_y = 0
        self.translate_z = 0

    def generate_vertices(self):
        for i in range(self.partion):
            alpha = 2*i*math.pi/self.partion
            self.vertex_small.append((math.sin(alpha) * self.radius_small,0,math.cos(alpha) * self.radius_small))
            self.vertex_high_small.append((math.sin(alpha) * self.radius_small,self.height,math.cos(alpha) * self.radius_small))
        for i in range(self.partion):
            alpha = 2*i*math.pi/self.partion
            self.vertex_big.append((math.sin(alpha) * self.radius_big,0,math.cos(alpha) * self.radius_big))
            self.vertex_high_big.append((math.sin(alpha) * self.radius_big,self.height,math.cos(alpha) * self.radius_big))

    def draw(self):
       # glLoadIdentity()
        glTranslatef(0,-50,-150)
        glRotatef(self.angle_x,1,0,0)
        glRotatef(self.angle_y,0,1,0)
        glRotatef(self.angle_z,0,0,1)
        glScalef(self.x, self.y, self.z)
        glTranslatef(self.translate_x, 0,0)
        glTranslatef(0, self.translate_y,0)
        glTranslatef(0, 0,self.translate_z)
        glBegin(GL_QUADS)
        glColor3f(1,0,1)
        for i in range(len(min(self.vertex_small,self.vertex_big))):
            if i == len(self.vertex_small)-1:
                i = -1
            glVertex3f(*self.vertex_small[i])
            glVertex3f(*self.vertex_high_small[i])
            glVertex3f(*self.vertex_high_small[i+1])
            glVertex3f(*self.vertex_small[i+1])
            glVertex3f(*self.vertex_big[i])
            glVertex3f(*self.vertex_high_big[i])
            glVertex3f(*self.vertex_high_big[i+1])
            glVertex3f(*self.vertex_big[i+1])
        glEnd()
        glBegin(GL_QUADS)
        glColor3f(0,1,1)
        for i in range(len(min(self.vertex_small,self.vertex_big))):
            if i == len(self.vertex_small)-1:
                i = -1
            glVertex3f(*self.vertex_small[i])
            glVertex3f(*self.vertex_big[i])
            glVertex3f(*self.vertex_big[i+1])
            glVertex3f(*self.vertex_small[i+1])
            glVertex3f(*self.vertex_high_small[i])
            glVertex3f(*self.vertex_high_big[i])
            glVertex3f(*self.vertex_high_big[i+1])
            glVertex3f(*self.vertex_high_small[i+1])
        glEnd()
        glFlush()

    def regenerate(self,flag):
        self.vertex_small.clear()
        self.vertex_high_small.clear()
        self.vertex_big.clear()
        self.vertex_high_big.clear()
        if flag:
            self.partion+=1
        else:
            self.partion-=1
        self.generate_vertices()

class Point(object):

    def __init__(self,x,y,z):
        self.x=x
        self.y=y
        self.z=z

    def edit(self,x,y,z):

        # Create new point #
        # It's not productively to construct over 1000 points #
        self.x = x
        self.y = y
        self.z = z

    def print(self):
        return (self.x,self.y,self.z)

    def __str__(self):
        return "(x,y,z) : %f %f %f" % (self.x,self.y,self.z)


class Spiral_3d(object):

    def __init__(self,radius_gyration,radius_circle,partion=25):
        self.radius_gyration = radius_gyration
        self.radius_circle = radius_circle
        self.partion = partion

        # Unchangeble (private) variable
        self.__partion_MAX = 75
        self.__gyration = 80

        # Test variable
        self.circle_x = 0
        self.circle_y = 0
        self.circle_z = 0

        self.points = [[Point(0,0,0) for x in range(self.__partion_MAX)] for x in range(self.__gyration)]

        self.generate_points()


        self.angle_x  = 0
        self.angle_y = 0
        self.angle_z = 0

        self.x = 2
        self.y = 2
        self.z = 2

        self.translate_x = 0
        self.translate_y = 0
        self.translate_z = 0


    def generate_points(self):
        i = 0
        count = 0
        while i <= 4 * math.pi:
            matrix = numpy.matrix(((math.cos(i) * (-1),math.sin(i)*(-1),0,0),
                                  (1 / (math.sqrt(self.radius_gyration ** 2 + self.radius_circle ** 2)) * self.radius_circle * math.sin(i),1 / (math.sqrt(self.radius_gyration ** 2 + self.radius_circle ** 2)) * (-self.radius_circle) * math.cos(i),1 / (math.sqrt(self.radius_gyration ** 2 + self.radius_circle ** 2))  * self.radius_gyration ,0),
                                 (1 / (math.sqrt(self.radius_gyration ** 2 + self.radius_circle ** 2)) * self.radius_gyration * (-1) * math.sin(i),1 / (math.sqrt(self.radius_gyration ** 2 + self.radius_circle ** 2)) * self.radius_gyration * math.cos(i),self.radius_circle / (math.sqrt(self.radius_gyration ** 2 + self.radius_circle ** 2)),0),
                                  (self.radius_gyration * math.cos(i), self.radius_gyration * math.sin(i),self.radius_circle*i,1)))
            #matrix = numpy.matrix(((self.radius_gyration * math.cos(i) * (-1),self.radius_gyration * math.sin(i)*(-1),0,0),
            #                      (1 / (math.sqrt(1 + self.radius_circle ** 2)) * self.radius_circle * self.radius_gyration * math.sin(i),1 / (math.sqrt(1 + self.radius_circle ** 2)) * self.radius_gyration * math.cos(i)*(-self.radius_circle),1 / (math.sqrt(1 + self.radius_circle ** 2))  * self.radius_circle ,0),
            #                      (1 / (math.sqrt(1 + self.radius_circle ** 2)) * self.radius_circle * self.radius_gyration * math.sin(i),1 / (math.sqrt(1 + self.radius_circle ** 2)) * self.radius_circle * (-1) * math.cos(i) * self.radius_gyration,1,0),
            #                      (self.radius_gyration * math.cos(i), self.radius_gyration * math.sin(i),self.radius_circle*i,1)))
           # matrix = [
           #         [self.radius_gyration * math.cos(i) * (-1),0,self.radius_gyration * math.sin(i)*(-1),0],
           #           [1 / (math.sqrt(1 + self.radius_circle ** 2)) * self.radius_gyration * (-1) * math.sin(i),1 / (math.sqrt(1 + self.radius_circle ** 2))  * self.radius_circle ,1 / (math.sqrt(1 + self.radius_circle ** 2)) * self.radius_gyration * math.cos(i),0],
           #           [1 / (math.sqrt(1 + self.radius_circle ** 2)) * self.radius_circle * self.radius_gyration * math.sin(i),1,1 / (math.sqrt(1 + self.radius_circle ** 2)) * self.radius_circle * (-1) * math.cos(i) * self.radius_gyration,0],
           #           [self.radius_gyration * math.cos(i), i*self.radius_circle, self.radius_gyration * math.sin(i),1]
           # ]
            matrix = matrix.transpose()
            # print(matrix)
            for j in range(self.partion):
                alpha = 2*j*math.pi/self.partion
                vector = numpy.array(((self.circle_x + math.cos(alpha) * self.radius_circle, self.circle_y + math.sin(alpha)*self.radius_circle,0,1)))
                vector = vector.transpose()
                # print(vector)
                new_vector = matrix.dot(vector)
                self.points[count][j].edit(new_vector[(0,0)],new_vector[(0,1)],new_vector[(0,2)])
                #print("point[%d][%d] " % (count,j) + str(self.points[count][j]))
            count += 1
            i += (math.pi/20)




    def draw(self):
        # glLoadIdentity()
        glTranslatef(0,-50,-150)
        #glColor3f(0,1,0)
        #for i in range(3):
         #   glBegin(GL_LINES)
          #  glVertex3f(0,0,0)
           # if i == 0:
           #     glVertex3f(30,0,0)
          #  elif i==1:
          #      glVertex3f(0,30,0)
          #  elif i==2:
           #     glVertex3f(0,0,30)
           # glEnd()
        glRotatef(self.angle_x,1,0,0)
        glRotatef(self.angle_y,0,1,0)
        glRotatef(self.angle_z,0,0,1)
        glScalef(self.x, self.y, self.z)
        glTranslatef(self.translate_x, 0,0)
        glTranslatef(0, self.translate_y,0)
        glTranslatef(0, 0,self.translate_z)
        glColor3f(1,0,0)
        glBegin(GL_QUADS)
        for j in range(self.__gyration-1):
            for i in range(self.partion):
                if i == self.partion-1:
                    glVertex3f(*self.points[j][i].print())
                    glVertex3f(*self.points[j][0].print())
                    glVertex3f(*self.points[j+1][0].print())
                    glVertex3f(*self.points[j+1][i].print())
                    # glEnd()
                    # glFlush()
                    # return None
                    exit(1)
                    break
                glVertex3f(*self.points[j][i].print())
                glVertex3f(*self.points[j][i+1].print())
                glVertex3f(*self.points[j+1][i+1].print())
                glVertex3f(*self.points[j+1][i].print())
                print(self.points[j][i].print())
                print(self.points[j][i+1].print())
                print(self.points[j+1][i+1].print())
                print(self.points[j+1][i].print())

        glEnd()
        glFlush()

    def regenerate(self,flag):
        if flag:
            if self.partion+1 > self.__partion_MAX:
                sys.stderr.write("Error to change partion!!!\n")
                return None
        else:
            if self.partion-1<1:
                sys.stderr.write("Error to change partion!!!\n")
                return None
        if flag:
            self.partion+=1
        else:
            self.partion-=1
        self.generate_points()




window = pyglet.window.Window(1600, 900, resizable=True, caption='Lab 2')

pyglet.gl.glClearColor(1, 1, 1, 1)

сube_b = Cube(-50,50)
#tube = Simple_tube(10,20,20)
tube = Spiral_3d(3.0,0.8)

@window.event
def on_draw():
    window.clear()
    сube_b.draw()
    tube.draw()

@window.event
def on_key_press(symbol,modifiers):
    try:
        status = on_key_press.space
    except:
        on_key_press.space=False
        status = on_key_press.space
    finally:
        if symbol==key.ENTER:
            if status:
                glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)
                on_key_press.space = False
            else:
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
                on_key_press.space = True
        elif symbol==key.EQUAL:
            tube.regenerate(True)
        elif symbol==key.MINUS:
            tube.regenerate(False)
        elif symbol==key.MOTION_UP:
            tube.x += .01
            tube.y += .01
            tube.z += .01
        elif symbol==key.MOTION_DOWN:
            tube.x -= .01
            tube.y -= .01
            tube.z -= .01
        elif symbol==key.W:
            tube.angle_x+=.5
        elif symbol==key.S:
            tube.angle_x-=.5
        elif symbol==key.A:
            tube.angle_z-=.5
        elif symbol==key.D:
            tube.angle_z-=.5
        elif symbol==key.Z:
            tube.angle_y+=.5
        elif symbol==key.X:
            tube.angle_y-=.5
        elif symbol==key.NUM_2:
            tube.translate_y-=.5
        elif symbol==key.NUM_8:
            tube.translate_y+=.5
        elif symbol==key.NUM_7:
            tube.translate_z-=.5
        elif symbol==key.NUM_9:
            tube.translate_z+=.5



# Print matrix 4x4 for debug
def print_list(m):
    m = list(m)
    for i in range(0,16):
        if i%4==0:
            print()
        print(m[i],end=" ")
    print("\n============",end="")


@window.event
def on_resize(width,height):
    glViewport(0,0,width,height)
    glMatrixMode(GL_PROJECTION)
    glLoadIdentity()
    gluPerspective(77,float(width)/height,0.1,1000)
    glTranslatef(-40,0,-200)
    glRotatef(-11,1,0,0)
    glRotatef(225,0,1,0)
    #a = (GLfloat * 16)()
    #glGetFloatv(GL_PROJECTION_MATRIX,a)
    #print_list(a)
    glMatrixMode(GL_MODELVIEW)
    glLoadIdentity()
    #gluLookAt(200,250,0,150,150,0,0,1,0)
    return pyglet.event.EVENT_HANDLED

def update(dt):
   pass

if __name__ == '__main__':
    pyglet.clock.schedule_interval(update, 1 / 20)
    pyglet.app.run()
