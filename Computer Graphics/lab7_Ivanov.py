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
import time
import os
import pickle

g = 9.8
fps_display = pyglet.clock.ClockDisplay()

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


    def __sub__(self, other):
        return Point(other.x-self.x,other.y-self.y,other.z-self.z)

    @staticmethod
    def cross(a,b):
        return Point(a.y*b.z-a.z*b.y,
                     a.z*b.x-a.x*b.z,
                     a.x*b.y-a.y*b.x)


    @staticmethod
    def find_normal(point1,point2,point3):
        vector1 = point2-point1
        vector2 = point3-point1
        vector_normal = Point.cross(vector1,vector2)
        length = math.sqrt(vector_normal.x*vector_normal.x+vector_normal.y*vector_normal.y+vector_normal.z*vector_normal.z)
        vector_normal.x /= length
        vector_normal.y /= length
        vector_normal.z /= length
        return vector_normal


class Spiral_3d(object):

    def __init__(self,radius_gyration,radius_circle,partion=25):

        # For animation #
        self.x_min = None
        self.x_max = None
        self.y_min = None
        self.y_max = None
        self.z_min = None
        self.z_max = None
        self.h = None
        # ============= #

        self.radius_gyration = radius_gyration
        self.radius_circle = radius_circle
        self.partion = partion

        # Unchangeble (private) variable
        self.__partion_MAX = 75


        # Changing size of the spiral #
        # Expansion or reducing #

        self.__gyration = 80
        self.__angle_gyrate = 4 * math.pi
        self.__step = math.pi / 20


        self.points = numpy.zeros((self.__gyration,self.__partion_MAX,4))
        self.textures_coord = numpy.zeros((self.__gyration,self.__partion_MAX,2))


        # Not used! #
        # self.normals = numpy.zeros((self.__gyration,self.__partion_MAX,4))

        self.display_list = 0
        self.second_display_list = 0


        # # Test variable
        # self.circle_x = 0
        # self.circle_y = 0
        # self.circle_z = 0

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

        self.normalize = False
        self.animation = False

        self.speed = 0
        self.dt = 0


    def generate_display_list(self,light_flag,texture_flag):
        id_ = glGenLists(1)
        self.display_list = id_
        if id_ != 0:
            glNewList(id_,GL_COMPILE)
            if texture_flag and light_flag:
                glBegin(GL_QUADS)
                for j in range(self.__gyration-1):
                    for i in range(self.partion):
                        normal1 = Point.find_normal(Point(*self.points[(j,i)][0:3]),Point(*self.points[(j,(i+1)%self.partion)][0:3]),Point(*self.points[(j+1,0)][0:3])).print()
                        normal2 = Point.find_normal(Point(*self.points[(j,(i+1)%self.partion)][0:3]),Point(*self.points[(j+1,(i+1)%self.partion)][0:3]),Point(*self.points[(j+1,i)][0:3])).print()
                        # Average normal from triangle to square normal
                        normal = numpy.mean(numpy.array([normal1,normal2]),axis=0)
                        glNormal3fv((GLfloat*3)(*normal))
                        glTexCoord2fv((GLfloat*2)(*self.textures_coord[(j,i)][0:2]))
                        glVertex3fv((GLfloat*3)(*self.points[(j,i)][0:3]))
                        glTexCoord2fv((GLfloat*2)(*self.textures_coord[(j,(i+1)%self.partion)][0:2]))
                        glVertex3fv((GLfloat*3)(*self.points[(j,(i+1)%self.partion)][0:3]))
                        glTexCoord2fv((GLfloat*2)(*self.textures_coord[(j+1,(i+1)%self.partion)][0:2]))
                        glVertex3fv((GLfloat*3)(*self.points[(j+1,(i+1)%self.partion)][0:3]))
                        glTexCoord2fv((GLfloat*2)(*self.textures_coord[(j+1,i)][0:2]))
                        glVertex3fv((GLfloat*3)(*self.points[(j+1,i)][0:3]))
                glEnd()
            elif texture_flag:
                glBegin(GL_QUADS)
                for j in range(self.__gyration-1):
                    for i in range(self.partion):
                        glTexCoord2fv((GLfloat*2)(*self.textures_coord[(j,i)][0:2]))
                        glVertex3fv((GLfloat*3)(*self.points[(j,i)][0:3]))
                        glTexCoord2fv((GLfloat*2)(*self.textures_coord[(j,(i+1)%self.partion)][0:2]))
                        glVertex3fv((GLfloat*3)(*self.points[(j,(i+1)%self.partion)][0:3]))
                        glTexCoord2fv((GLfloat*2)(*self.textures_coord[(j+1,(i+1)%self.partion)][0:2]))
                        glVertex3fv((GLfloat*3)(*self.points[(j+1,(i+1)%self.partion)][0:3]))
                        glTexCoord2fv((GLfloat*2)(*self.textures_coord[(j+1,i)][0:2]))
                        glVertex3fv((GLfloat*3)(*self.points[(j+1,i)][0:3]))
                glEnd()
            elif light_flag:
                glBegin(GL_QUADS)
                for j in range(self.__gyration-1):
                    for i in range(self.partion):
                        normal1 = Point.find_normal(Point(*self.points[(j,i)][0:3]),Point(*self.points[(j,(i+1)%self.partion)][0:3]),Point(*self.points[(j+1,0)][0:3])).print()
                        normal2 = Point.find_normal(Point(*self.points[(j,(i+1)%self.partion)][0:3]),Point(*self.points[(j+1,(i+1)%self.partion)][0:3]),Point(*self.points[(j+1,i)][0:3])).print()
                        # Average normal from triangle to square normal
                        normal = numpy.mean(numpy.array([normal1,normal2]),axis=0)
                        glNormal3fv((GLfloat*3)(*normal))
                        glVertex3fv((GLfloat*3)(*self.points[(j,i)][0:3]))
                        glVertex3fv((GLfloat*3)(*self.points[(j,(i+1)%self.partion)][0:3]))
                        glVertex3fv((GLfloat*3)(*self.points[(j+1,(i+1)%self.partion)][0:3]))
                        glVertex3fv((GLfloat*3)(*self.points[(j+1,i)][0:3]))
                glEnd()
            else:
                glBegin(GL_QUADS)
                for j in range(self.__gyration-1):
                    for i in range(self.partion):
                        glVertex3fv((GLfloat*3)(*self.points[(j,i)][0:3]))
                        glVertex3fv((GLfloat*3)(*self.points[(j,(i+1)%self.partion)][0:3]))
                        glVertex3fv((GLfloat*3)(*self.points[(j+1,(i+1)%self.partion)][0:3]))
                        glVertex3fv((GLfloat*3)(*self.points[(j+1,i)][0:3]))
                glEnd()
            glEndList()
        id1 = glGenLists(1)
        self.second_display_list = id1
        if id1 != 0:
            glBegin(GL_POLYGON)
            for i in range(self.partion):
                alpha = 2*i*math.pi/self.partion
                glTexCoord2f(0.5*math.cos(alpha)+0.5,0.5*math.sin(alpha)+0.5)
                glVertex3fv((GLfloat*3)(*self.points[(0,i)][0:3]))
            glEnd()

            glBegin(GL_POLYGON)
            for i in range(self.partion):
                alpha = 2*i*math.pi/self.partion
                glTexCoord2f(0.5*math.cos(alpha)+0.5,0.5*math.sin(alpha)+0.5)
                glVertex3fv((GLfloat*3)(*self.points[(self.__gyration-1,i)][0:3]))
            glEnd()






    def get_min_max(self,vector,flag=False):
        x,y,z=vector[(0,0)],vector[(0,1)],vector[(0,2)]
        if self.y_min is None and self.y_max is None:
            self.y_min = y
            self.y_max = y

        if self.y_min > y:
            self.y_min = y
        elif self.y_max < y:
            self.y_max = y

        if flag:
            self.h = self.y_min


    def generate_points(self):
        i = 0
        count = 0
        while i < self.__angle_gyrate:
            matrix = numpy.matrix(((math.cos(i) * (-1),math.sin(i)*(-1),0,0),
                                  (1 / (math.sqrt(self.radius_gyration ** 2 + 2.5 ** 2)) * 2.5 * math.sin(i),1 / (math.sqrt(self.radius_gyration ** 2 + 2.5 ** 2)) * (-2.5) * math.cos(i),1 / (math.sqrt(self.radius_gyration ** 2 + 2.5 ** 2))  * self.radius_gyration ,0),
                                 (1 / (math.sqrt(self.radius_gyration ** 2 + 2.5 ** 2)) * self.radius_gyration * (-1) * math.sin(i),1 / (math.sqrt(self.radius_gyration ** 2 + 2.5 ** 2)) * self.radius_gyration * math.cos(i),2.5 / (math.sqrt(self.radius_gyration ** 2 + 2.5 ** 2)),0),
                                  (self.radius_gyration * math.cos(i), self.radius_gyration * math.sin(i),2.5*i,1)))

            #matrix = numpy.matrix(((self.radius_gyration * math.cos(i) * (-1),self.radius_gyration * math.sin(i)*(-1),0,0),
            #                      (1 / (math.sqrt(1 + 2.5 ** 2)) * 2.5 * self.radius_gyration * math.sin(i),1 / (math.sqrt(1 + 2.5 ** 2)) * self.radius_gyration * math.cos(i)*(-2.5),1 / (math.sqrt(1 + 2.5 ** 2))  * 2.5 ,0),
            #                      (1 / (math.sqrt(1 + 2.5 ** 2)) * 2.5 * self.radius_gyration * math.sin(i),1 / (math.sqrt(1 + 2.5 ** 2)) * 2.5 * (-1) * math.cos(i) * self.radius_gyration,1,0),
            #                      (self.radius_gyration * math.cos(i), self.radius_gyration * math.sin(i),2.5*i,1)))

            # matrix = [
            #         [self.radius_gyration * math.cos(i) * (-1),0,self.radius_gyration * math.sin(i)*(-1),0],
            #           [1 / (math.sqrt(1 + 2.5 ** 2)) * self.radius_gyration * (-1) * math.sin(i),1 / (math.sqrt(1 + 2.5 ** 2))  * 2.5 ,1 / (math.sqrt(1 + 2.5 ** 2)) * self.radius_gyration * math.cos(i),0],
            #           [1 / (math.sqrt(1 + 2.5 ** 2)) * 2.5 * self.radius_gyration * math.sin(i),1,1 / (math.sqrt(1 + 2.5 ** 2)) * 2.5 * (-1) * math.cos(i) * self.radius_gyration,0],
            #           [self.radius_gyration * math.cos(i), i*2.5, self.radius_gyration * math.sin(i),1]
            # ]

            matrix = matrix.transpose()
            for j in range(self.partion):
                alpha = 2*j*math.pi/self.partion
                vector = numpy.array(((math.cos(alpha) * self.radius_circle, math.sin(alpha)*self.radius_circle,0,1)))
                vector = vector.transpose()
                new_vector = matrix.dot(vector)

                self.get_min_max(new_vector,flag=True)

                self.points[(count,j,0)]=new_vector[(0,0)]
                self.points[(count,j,1)]=new_vector[(0,1)]
                self.points[(count,j,2)]=new_vector[(0,2)]
                self.points[(count,j,3)]=new_vector[(0,3)]

                step = count / float(self.__gyration-1) * 8
                step2 = j / float(self.partion)

                self.textures_coord[(count,j)]=(step,step2)


                # Second method found normal #
                # From parametric circle equation
                # vector_2 = numpy.array(((math.cos(alpha) * self.radius_circle, math.sin(alpha)*self.radius_circle,0,0)))
                # vector_2 = vector_2.transpose()
                # new_vector2 = matrix.dot(vector_2)
                #
                # self.normals[(count,j,0)]=new_vector2[(0,0)]
                # self.normals[(count,j,1)]=new_vector2[(0,1)]
                # self.normals[(count,j,2)]=new_vector2[(0,2)]
                # self.normals[(count,j,3)]=new_vector2[(0,3)]





            count += 1
            i += (self.__step)

        # self.regenerate_normal()

    # def change(self,flag):
    #     if flag:
    #         self.__angle_gyrate += self.__step
    #         # self.points = numpy.vstack((self.points,))


    def regenerate_normal(self):
        i=0
        matrix = numpy.matrix(((math.cos(i) * (-1),math.sin(i)*(-1),0,0),
                                  (1 / (math.sqrt(self.radius_gyration ** 2 + 2.5 ** 2)) * 2.5 * math.sin(i),1 / (math.sqrt(self.radius_gyration ** 2 + 2.5 ** 2)) * (-2.5) * math.cos(i),1 / (math.sqrt(self.radius_gyration ** 2 + 2.5 ** 2))  * self.radius_gyration ,0),
                                 (1 / (math.sqrt(self.radius_gyration ** 2 + 2.5 ** 2)) * self.radius_gyration * (-1) * math.sin(i),1 / (math.sqrt(self.radius_gyration ** 2 + 2.5 ** 2)) * self.radius_gyration * math.cos(i),2.5 / (math.sqrt(self.radius_gyration ** 2 + 2.5 ** 2)),0),
                                  (self.radius_gyration * math.cos(i), self.radius_gyration * math.sin(i),2.5*i,1)))
        front_vector = numpy.array(((0,0,-1,0))).transpose()
        back_vector = numpy.array(((0,0,1,0))).transpose()

        new_vector_front_normal = matrix.dot(front_vector)

        i=self.__angle_gyrate-self.__step
        matrix = numpy.matrix(((math.cos(i) * (-1),math.sin(i)*(-1),0,0),
                                  (1 / (math.sqrt(self.radius_gyration ** 2 + 2.5 ** 2)) * 2.5 * math.sin(i),1 / (math.sqrt(self.radius_gyration ** 2 + 2.5 ** 2)) * (-2.5) * math.cos(i),1 / (math.sqrt(self.radius_gyration ** 2 + 2.5 ** 2))  * self.radius_gyration ,0),
                                 (1 / (math.sqrt(self.radius_gyration ** 2 + 2.5 ** 2)) * self.radius_gyration * (-1) * math.sin(i),1 / (math.sqrt(self.radius_gyration ** 2 + 2.5 ** 2)) * self.radius_gyration * math.cos(i),2.5 / (math.sqrt(self.radius_gyration ** 2 + 2.5 ** 2)),0),
                                  (self.radius_gyration * math.cos(i), self.radius_gyration * math.sin(i),2.5*i,1)))
        new_vector_back_normal =  matrix.dot(back_vector)

        for j in range(self.partion):
            self.normals[(0,j,0)]=new_vector_front_normal[(0,0)]
            self.normals[(0,j,1)]=new_vector_front_normal[(0,1)]
            self.normals[(0,j,2)]=new_vector_front_normal[(0,2)]
            self.normals[(0,j,3)]=new_vector_front_normal[(0,3)]
            self.normals[(self.__gyration-1,j,0)]=new_vector_back_normal[(0,0)]
            self.normals[(self.__gyration-1,j,1)]=new_vector_back_normal[(0,1)]
            self.normals[(self.__gyration-1,j,2)]=new_vector_back_normal[(0,2)]
            self.normals[(self.__gyration-1,j,3)]=new_vector_back_normal[(0,3)]

    def update(self,dt):
        if self.animation:
            self.dt = dt

    def animate(self,dt):

        self.x_min = self.x_max = self.y_min = self.y_max = self.z_min = self.z_max = None


        self.speed -= (g*dt)
        matrix_translate=numpy.matrix(((1,0,0,0),
                                       (0,1,0,0),
                                       (0,0,1,0),
                                       (0,self.speed+g*dt*dt/2,0,1))).transpose()
        for j in range(self.__gyration):
            for i in range(self.partion):
                point = self.points[(j,i)].transpose()
                vector = matrix_translate.dot(point)
                self.get_min_max(vector)
                self.points[(j,i)]=vector

        # -100 - value of y coordinate lower_plane
        if self.y_min < -100:
            # v = sqrt(2*g*h) when you collide plane
            self.speed = math.sqrt(2*g*(self.h+17))
            matrix_translate=numpy.matrix(((1,0,0,0),
                                       (0,1,0,0),
                                       (0,0,1,0),
                                       (0,self.speed,0,1))).transpose()
            for j in range(self.__gyration):
                for i in range(self.partion):
                    point = self.points[(j,i)].transpose()
                    vector = matrix_translate.dot(point)
                    self.get_min_max(vector)
                    self.points[(j,i)]=vector




    def draw(self,light_flag,texture_flag):

        glTranslatef(-15,80,-100)
        # glLoadIdentity()

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
        glColor3f(1,1,1)

        if self.normalize:
            glEnable(GL_NORMALIZE)

        self.generate_display_list(light_flag,texture_flag)
        glCallList(self.display_list)
        glCallList(self.second_display_list)
            # glBegin(GL_QUAD_STRIP)
            # for j in range(self.__gyration-1):
            #     for i in range(self.partion):
            #         glVertex3fv((GLfloat*3)(*self.points[(j+1,i)][0:3]))
            #         glVertex3fv((GLfloat*3)(*self.points[(j+1,(i+1)%self.partion)][0:3]))
            #         glVertex3fv((GLfloat*3)(*self.points[(j,(i+1)%self.partion)][0:3]))
            #         glVertex3fv((GLfloat*3)(*self.points[(j,i)][0:3]))
            # glEnd()





            # glTexCoord2f(0.0,0.0)
            # glTexCoord2f(*self.textures_coord[(j,i)])
            # glTexCoord2f(step,step2)
            # ====================== #
            # glTexCoord2f(0.0,1.0)
            # glTexCoord2f(*self.textures_coord[(j,i)])
            # glTexCoord2f(step,next_step2)
            # ====================== #
            # glTexCoord2f(*self.textures_coord[(j,i)])
            # glTexCoord2f(1.0,1.0)
            # glTexCoord2f(next_step,next_step2)
            # ====================== #
            # glTexCoord2f(*self.textures_coord[(j,i)])
            # glTexCoord2f(1.0,0.0)
            # glTexCoord2f(next_step,step2)



        glFlush()


    def regenerate(self,flag):
        print("PARTION")
        print(self.partion)
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


class Lightning(object):

    def __init__(self):

        self.flag = False

        self.color_light0 = [0.2,0.2,0.2,1]

        self.diffuse_light0 = [0.2,0.4,0.2,1.0]
        self.position_light0 = [2.0,-1.0,12.0,1.0]
        self.ambient_light0 = [0.4,0.1,0.5,1.0]
        self.specular_light0 = [0.2,0.4,0.2,1.0]

        self.spot_cutoff_light0 = 90.0
        self.spot_spot_exponent_light0 = 0.0
        self.spot_direction_light0 = [0.0,0.0,-1.0]

        self.diffuse_material = [0.7,0.7,0.7,1.0]
        self.ambient_material = [0.7,0.7,0.7,1.0]
        self.specular_material = [0.3,0.3,0.3,1.0]
        self.emission_material = [0.2,0.5,0.2,1.0]
        self.shininess_material = 0.2



    def init_lightning(self):

        glEnable(GL_LIGHTING)

        glEnable(GL_LIGHT0)
        glEnable(GL_COLOR_MATERIAL)

        glShadeModel(GL_SMOOTH)

        glLightModelfv(GL_LIGHT_MODEL_AMBIENT,(GLfloat * 4)(*self.color_light0))

        glLightfv(GL_LIGHT0,GL_POSITION,(GLfloat * 4)(*self.position_light0))
        glLightfv(GL_LIGHT0,GL_DIFFUSE,(GLfloat * 4)(*self.diffuse_light0))
        glLightfv(GL_LIGHT0,GL_AMBIENT,(GLfloat * 4)(*self.ambient_light0))
        glLightfv(GL_LIGHT0,GL_SPECULAR,(GLfloat * 4)(*self.specular_light0))

        glLightfv(GL_LIGHT0,GL_SPOT_CUTOFF,(GLfloat)(self.spot_cutoff_light0))
        glLightfv(GL_LIGHT0,GL_SPOT_EXPONENT,(GLfloat)(self.spot_spot_exponent_light0))
        glLightfv(GL_LIGHT0,GL_SPOT_DIRECTION,(GLfloat * 3)(*self.spot_direction_light0))

    def init_material(self):

        glMaterialfv(GL_FRONT,GL_DIFFUSE,(GLfloat * 4)(*self.diffuse_material))
        glMaterialfv(GL_FRONT,GL_AMBIENT,(GLfloat * 4)(*self.ambient_material))
        glMaterialfv(GL_FRONT,GL_SPECULAR,(GLfloat * 4)(*self.specular_material))
        glMaterialfv(GL_FRONT,GL_EMISSION,(GLfloat * 4)(*self.emission_material))
        glMaterialfv(GL_FRONT,GL_SHININESS,(GLfloat)(self.shininess_material))

        glColorMaterial(GL_FRONT,GL_AMBIENT)

    def change_material(self,param):
        delta = 0.05
        if param == GL_AMBIENT:
            sign = self.ambient_material[0] < 0.8 and self.ambient_material[1] < 0.8 and self.ambient_material[2] < 0.8
            self.ambient_material[0] = self.ambient_material[0]+delta if sign else self.ambient_material[0]-delta
            self.ambient_material[1] = self.ambient_material[1]+delta if sign else self.ambient_material[1]-delta
            self.ambient_material[2] = self.ambient_material[2]+delta if sign else self.ambient_material[2]-delta
            glMaterialfv(GL_FRONT,param,(GLfloat * 4)(*self.ambient_material))
        elif param == GL_DIFFUSE:
            sign = self.diffuse_material[0] < 0.8 and self.diffuse_material[1] < 0.8 and self.diffuse_material[2] < 0.8
            self.diffuse_material[0] = self.diffuse_material[0]+delta if sign else self.diffuse_material[0]-delta
            self.diffuse_material[1] = self.diffuse_material[1]+delta if sign else self.diffuse_material[1]-delta
            self.diffuse_material[2] = self.diffuse_material[2]+delta if sign else self.diffuse_material[2]-delta
            glMaterialfv(GL_FRONT,param,(GLfloat * 4)(*self.diffuse_material))
        elif param == GL_SPECULAR:
            sign = self.specular_material[0] < 0.8 and self.specular_material[1] < 0.8 and self.specular_material[2] < 0.7
            self.specular_material[0] = self.specular_material[0]+delta if sign else self.specular_material[0]-delta
            self.specular_material[1] = self.specular_material[1]+delta if sign else self.specular_material[1]-delta
            self.specular_material[2] = self.specular_material[2]+delta if sign else self.specular_material[2]-delta
            glMaterialfv(GL_FRONT,param,(GLfloat * 4)(*self.specular_material))
        elif param == GL_EMISSION:
            sign = self.emission_material[0] < 0.8 and self.emission_material[1] < 0.8 and self.emission_material[2] < 0.8
            self.emission_material[0] = self.emission_material[0]+delta if sign else self.emission_material[0]-delta
            self.emission_material[1] = self.emission_material[1]+delta if sign else self.emission_material[1]-delta
            self.emission_material[2] = self.emission_material[2]+delta if sign else self.emission_material[2]-delta
            glMaterialfv(GL_FRONT,param,(GLfloat * 4)(*self.emission_material))
        elif param == GL_SHININESS:
            sign = self.shininess_material < 0.8
            self.shininess_material = self.shininess_material+delta if sign else self.shininess_material-delta
            glMaterialfv(GL_FRONT,param,(GLfloat)(self.shininess_material))

class Texture:

    def __init__(self):
        self.textures = []
        self.image = None
        self.index_active_texture = -1
        self.flag = False


    def load_texture(self):


        if self.flag:
            glEnable(GL_TEXTURE_2D)
        else:
            glDisable(GL_TEXTURE_2D)
            return None

        if self.textures == []:
            return None

        glEnable(self.textures[self.index_active_texture].target)
        glBindTexture(self.textures[self.index_active_texture].target,self.textures[self.index_active_texture].id)
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR)

        # Repeat of texture
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)


        glTexImage2D(GL_TEXTURE_2D,0,GL_RGBA,self.image.width,self.image.height,0,
                     GL_RGBA,GL_UNSIGNED_BYTE,self.image.get_image_data().get_data("RGBA",self.image.width*len("RGBA")))


    def open_image(self,name):
        self.image = pyglet.image.load(name)
        self.textures.append(self.image.get_texture())
        self.index_active_texture = len(self.textures)-1


class Serialization:

    def __init__(self,name="save_scene.pickle"):
        self.file_name = name
        if not os.path.exists("save_scene.pickle"):
            open(self.file_name,mode='w').close()

    def load(self):
        with open(self.file_name,mode='rb') as f:
            tube.x_min = pickle.load(f)
            tube.x_max = pickle.load(f)
            tube.y_min = pickle.load(f)
            tube.y_max = pickle.load(f)
            tube.z_min = pickle.load(f)
            tube.z_max = pickle.load(f)
            tube.radius_gyration = pickle.load(f)
            tube.radius_circle = pickle.load(f)
            tube.partion = pickle.load(f)
            tube.points = pickle.load(f)
            tube.angle_x = pickle.load(f)
            tube.angle_y = pickle.load(f)
            tube.angle_z = pickle.load(f)
            tube.x = pickle.load(f)
            tube.y = pickle.load(f)
            tube.z = pickle.load(f)
            tube.translate_x = pickle.load(f)
            tube.translate_y = pickle.load(f)
            tube.translate_z = pickle.load(f)
            tube.normalize = pickle.load(f)
            tube.animation = pickle.load(f)
            tube.speed = pickle.load(f)
            tube.dt = pickle.load(f)
            light.color_light0 = pickle.load(f)
            light.diffuse_light0 = pickle.load(f)
            light.position_light0 = pickle.load(f)
            light.ambient_light0 = pickle.load(f)
            light.specular_light0 = pickle.load(f)
            light.spot_cutoff_light0 = pickle.load(f)
            light.spot_spot_exponent_light0 = pickle.load(f)
            light.spot_direction_light0 = pickle.load(f)
            light.diffuse_material = pickle.load(f)
            light.ambient_material = pickle.load(f)
            light.specular_material = pickle.load(f)
            light.emission_material = pickle.load(f)
            light.shininess_material = pickle.load(f)
            # texture.textures = pickle.load(f)
            texture.image = pickle.load(f)
            texture.index_active_texture = pickle.load(f)
            texture.flag = pickle.load(f)
            on_key_press.space = pickle.load(f)

    def save(self):
        with open(self.file_name,mode='wb') as f:
            pickle.dump(tube.x_min,f)
            pickle.dump(tube.x_max,f)
            pickle.dump(tube.y_min,f)
            pickle.dump(tube.y_max,f)
            pickle.dump(tube.z_min,f)
            pickle.dump(tube.z_max,f)
            pickle.dump(tube.radius_gyration,f)
            pickle.dump(tube.radius_circle,f)
            pickle.dump(tube.partion,f)
            pickle.dump(tube.points,f)
            pickle.dump(tube.angle_x,f)
            pickle.dump(tube.angle_y,f)
            pickle.dump(tube.angle_z,f)
            pickle.dump(tube.x,f)
            pickle.dump(tube.y,f)
            pickle.dump(tube.z,f)
            pickle.dump(tube.translate_x,f)
            pickle.dump(tube.translate_y,f)
            pickle.dump(tube.translate_z,f)
            pickle.dump(tube.normalize,f)
            pickle.dump(tube.animation,f)
            pickle.dump(tube.speed,f)
            pickle.dump(tube.dt,f)
            pickle.dump(light.color_light0,f)
            pickle.dump(light.diffuse_light0,f)
            pickle.dump(light.position_light0,f)
            pickle.dump(light.ambient_light0,f)
            pickle.dump(light.specular_light0,f)
            pickle.dump(light.spot_cutoff_light0,f)
            pickle.dump(light.spot_spot_exponent_light0,f)
            pickle.dump(light.spot_direction_light0,f)
            pickle.dump(light.diffuse_material,f)
            pickle.dump(light.ambient_material,f)
            pickle.dump(light.specular_material,f)
            pickle.dump(light.emission_material,f)
            pickle.dump(light.shininess_material,f)
            # pickle.dump(texture.textures,f)
            pickle.dump(texture.image,f)
            pickle.dump(texture.index_active_texture,f)
            pickle.dump(texture.flag,f)
            pickle.dump(on_key_press.space,f)




def plane_draw():
    glColor3f(0,0,1)
    glBegin(GL_QUADS)
    glVertex3f(400,-100,-100)
    glVertex3f(400,-100,1000)
    glVertex3f(-400,-100,1000)
    glVertex3f(-400,-100,-100)
    glEnd()
    glFlush()


window = pyglet.window.Window(1600, 1000, resizable=True, caption='Lab 2')

pyglet.gl.glClearColor(0,0,0,0)

# сube_b = Cube(-50,50)
#tube = Simple_tube(10,20,20)
tube = Spiral_3d(10,2.5)
light = Lightning()
texture = Texture()
serialize = Serialization()

@window.event
def on_draw():
    window.clear()
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT )
    glLoadIdentity()
    # glEnable(GL_DEPTH_TEST)

    if light.flag:
        light.init_lightning()
        light.init_material()
    # glLightModelf(GL_LIGHT_MODEL_TWO_SIDE,GL_TRUE)
    # сube_b.draw()

    #dt = clock.tick()
    # fps_display.draw()
    #print(pyglet.clock.get_fps())

    tube.draw(light_flag=light.flag,texture_flag=texture.flag)
    texture.open_image(os.path.join(os.getcwd(),"OpenGL","3.bmp"))
    # plane_draw()
    if tube.animation:
        tube.animate(tube.dt)
    texture.load_texture()



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
        # elif symbol==key.M:
            # More tube #
        # elif symbol == key.L:
            # Less tube #
        elif symbol==key.N:
            tube.normalize=True
        elif symbol==key.LCTRL:
            if tube.animation:
                tube.animation = False
            else:
                tube.animation=True
        elif symbol==key._1:
            light.change_material(GL_AMBIENT)
        elif symbol==key._2:
            light.change_material(GL_DIFFUSE)
        elif symbol==key._3:
            light.change_material(GL_SPECULAR)
        elif symbol==key._4:
            light.change_material(GL_EMISSION)
        elif symbol==key._5:
            light.change_material(GL_SHININESS)
        elif symbol==key.T:
            if texture.flag:
                texture.flag=False
            else:
                texture.flag=True
        elif symbol==key.L:
            serialize.load()
        elif symbol==key.O:
            serialize.save()
        elif symbol==key.Y:
            if light.flag:
                light.flag = False
            else:
                light.flag = True


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
    a = (GLfloat * 16)()
    glGetFloatv(GL_PROJECTION_MATRIX,a)
    print_list(a)
    glMatrixMode(GL_MODELVIEW)
    glLoadIdentity()
    #gluLookAt(200,250,0,150,150,0,0,1,0)
    return pyglet.event.EVENT_HANDLED


if __name__ == '__main__':
    pyglet.clock.schedule(func=tube.update)
    pyglet.app.run()
