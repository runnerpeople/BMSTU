#!/usr/bin/python3
# -*- coding: utf-8 -*-
__author__="great"

from enum import Enum
from pyglet import *
from pyglet.gl import *
from pyglet.window import key,mouse
import math
import llist as linked_list
from enum import Enum

global eps
# global center_mode
eps=1e-10
# center_mode = False

window = pyglet.window.Window(800, 600, resizable=False, caption='Lab 5')

# Extension double-linked list #
def find(dllist,value):
    if isinstance(dllist,linked_list.dllist):
        for i in range(len(dllist)):
            if dllist[i]==value:
                return dllist[i]
        return None
    else:
        raise TypeError("First argument must have type 'llist.dllist'")

def find_node(dllist,value):
    if isinstance(dllist,linked_list.dllist):
        for i in range(len(dllist)):
            if dllist[i]==value:
                return dllist.nodeat(i)
        return None
    else:
        raise TypeError("First argument must have type 'llist.dllist'")

def bool_find(dllist,value):
    return not (isinstance(find(dllist,value),type(None)))

def contains(dllist,node):
    if isinstance(dllist,linked_list.dllist) and isinstance(node,linked_list.dllistnode):
        for i in range(len(dllist)):
            if dllist[i]==node.value:
                return dllist.nodeat(i)
        return None
    elif isinstance(node,linked_list.dllistnode):
        raise TypeError("First argument must have type 'llist.dllist'")
    else:
        raise ValueError("Second argument must have type 'llist.dllistnode'")

def bool_contains(dllist,node):
    return contains(dllist,node)!=None

def next(dllist,node):
    if isinstance(dllist,linked_list.dllist) and isinstance(node,linked_list.dllistnode):
        if node.next != None:
            return node.next
        else:
            return dllist.first
    elif isinstance(node,linked_list.dllistnode):
        raise TypeError("First argument must have type 'llist.dllist'")
    else:
        raise ValueError("Second argument must have type 'llist.dllistnode'")

def prev(dllist,node):
    if isinstance(dllist,linked_list.dllist) and isinstance(node,linked_list.dllistnode):
        if node.prev != None:
            return node.prev
        else:
            return dllist.last
    elif isinstance(node,linked_list.dllistnode):
        raise TypeError("First argument must have type 'llist.dllist'")
    else:
        raise ValueError("Second argument must have type 'llist.dllistnode'")

def deepcopy(dllist):
    copy_dllist = linked_list.dllist()
    this = dllist.first
    while this != None:
        copy_dllist.insert(this.value)
        this = this.next
    return copy_dllist

def delete(dllist,value):
    if len(dllist)>0 and isinstance(find(dllist,value),Point):
        node = linked_list.dllistnode(value)
        node_find = contains(dllist,node)
        dllist.remove(node_find)
    else:
        raise ValueError("Element not found!")


# A*x + B*y + C = 0
class Line_Standard(object):

    def __init__(self,p1,p2):
        self.a,self.b,self.c = p2.y-p1.y,p1.x-p2.x,-p1.x*(p2.y-p1.y)-p1.y*(p1.x-p2.x)

    def dist(self,x,y):
        return (self.a*x+self.b*y+self.c)/(math.sqrt(self.a ** 2 + self.b ** 2))

    @staticmethod
    def determinant(a,b,c,d):
        return a * d - b * c

    def intersect(self,other):
        zn = self.determinant(self.a,self.b,other.a,other.b)
        x,y = 0,0
        if abs(zn)<eps:
            return None
        x,y = -self.determinant(self.c,self.b,other.c,other.b)/zn,\
              -self.determinant(self.a,self.c,other.a,other.c)/zn
        return Point(x,y)

# {
# x = x0 + a * t
# y = y0 + b * t
# }

class Line(object):

    def __init__(self,p1,p2):
        self.a = p2.x - p1.x
        self.b = p2.y - p1.y
        self.x0 = p1.x
        self.y0 = p1.y

        # if self.a<0:
        #     self.a *= (-1)
        #     self.b *= (-1)
        #     self.x0 = p2.x
        #     self.y0 = p2.y

    def get(self,t):
        x,y = self.x0 + self.a * t,self.y0 + self.b * t
        # Error with float point
        return Point(int(x),int(y))

    def intersect(self,other):
        if (other.b * self.a - other.a * self.b) == 0:
            return None
        u = (other.a * (self.y0 - other.y0) - other.b * (self.x0 - other.x0)) / (other.b * self.a - other.a * self.b)
        w = (self.a * (self.y0 - other.y0) - self.b * (self.x0 - other.x0)) / (other.b * self.a - other.a * self.b)
        if 0 <= u <= 1 and 0 <= w <= 1:
            return self.get(u)
        else:
            return None


class Weilor_Azerton(object):

    class Operation(Enum):
        Intersect = 1
        Union = 2
        Difference = 3


    def __init__(self,clip_polygon,subject_polygon):
        self.clip_polygon = clip_polygon
        self.subject_polygon = subject_polygon
        self.reverse = False

        # For Weilor-Azerton
        self.copy_clip_polygon = None
        self.copy_subject_polygon = None

        # FOR DEBUG #
        self.points = []
        self.enter_points = []
        self.exit_points = []


    @staticmethod
    def determinant(p1,p2):
        return (p1.x * p2.y - p2.x * p1.y)

    def difference(self,subject_polygon,clip_polygon):
        if self.copy_clip_polygon == None and self.copy_subject_polygon == None:
            subject_polygon, self.copy_subject_polygon = deepcopy(self.subject_polygon.points),deepcopy(self.subject_polygon.points)
            clip_polygon,self.copy_clip_polygon = deepcopy(self.clip_polygon.points),deepcopy(self.clip_polygon.points)

        polygon = self.process(subject_polygon,clip_polygon,Weilor_Azerton.Operation.Difference)

        self.copy_clip_polygon,self.copy_subject_polygon = None, None
        return polygon

    def union(self,subject_polygon,clip_polygon):
        if self.copy_clip_polygon == None and self.copy_subject_polygon == None:
            subject_polygon, self.copy_subject_polygon = deepcopy(self.subject_polygon.points),deepcopy(self.subject_polygon.points)
            clip_polygon,self.copy_clip_polygon = deepcopy(self.clip_polygon.points),deepcopy(self.clip_polygon.points)

        polygon = self.process(subject_polygon,clip_polygon,Weilor_Azerton.Operation.Union)

        self.copy_clip_polygon,self.copy_subject_polygon = None, None
        return polygon


    def intersection(self,subject_polygon,clip_polygon):
        if self.copy_clip_polygon == None and self.copy_subject_polygon == None:
            subject_polygon, self.copy_subject_polygon = deepcopy(self.subject_polygon.points),deepcopy(self.subject_polygon.points)
            clip_polygon,self.copy_clip_polygon = deepcopy(self.clip_polygon.points),deepcopy(self.clip_polygon.points)

        polygon = self.process(subject_polygon,clip_polygon,Weilor_Azerton.Operation.Intersect)

        self.copy_clip_polygon,self.copy_subject_polygon = None, None
        return polygon

    def process(self,subject_polygon,clip_polygon,operation):

        def intersections(intersection,subject_polygon,clip_polygon):
            for i in range(len(subject_polygon)):
                current_subject_polygon = subject_polygon[i]
                for j in range(len(clip_polygon)):
                    current_clip_polygon = clip_polygon[j]
                    line_a = Line(current_subject_polygon,subject_polygon[(i+1)%(len(subject_polygon))])
                    line_b = Line(current_clip_polygon,clip_polygon[(j+1)%(len(clip_polygon))])
                    point = line_a.intersect(line_b)
                    if point is not None:
                        if point not in intersection:
                            self.points.append(point)
                            if not bool_find(subject_polygon,point):
                                subject_polygon.insert(point,subject_polygon.nodeat((i+1)%(len(subject_polygon))))
                            if not bool_find(clip_polygon,point):
                                clip_polygon.insert(point,clip_polygon.nodeat((j+1)%(len(clip_polygon))))
                            intersection[point]=(current_clip_polygon,point)
                            return True
            return False

        # Correct mistake for lines
        def remake_intersections(intersection,clip_polygon,subject_polygon):
            for key,value in intersection.items():
                intersection[key]=(prev(clip_polygon,find_node(clip_polygon,value[1])).value,value[1])


        # List of summary polygon
        def traverselist(subject_list,enter,exit,polygon,transitionNode,start_point,clip_list):
            subject_list_node = contains(subject_list,transitionNode)
            if subject_list_node == None:
                return None
            delete(enter,transitionNode.value)
            while (subject_list_node != None and (not bool_contains(exit,subject_list_node)) and (not bool_contains(enter,subject_list_node))):
                polygon.appendright(subject_list_node.value)
                subject_list_node = next(subject_list,subject_list_node)
                if subject_list_node.value == start_point:
                    return None
            return contains(clip_list,subject_list_node)

        def get_status(subject_polygon,clip_polygon):
            intersection = {}
            for i in range(len(subject_polygon)):
                current_subject_polygon = subject_polygon[i]
                for j in range(len(clip_polygon)):
                    current_clip_polygon = clip_polygon[j]
                    line_a = Line(current_subject_polygon,subject_polygon[(i+1)%(len(subject_polygon))])
                    line_b = Line(current_clip_polygon,Point(window.width,current_clip_polygon.y))
                    point = line_a.intersect(line_b)
                    if point is not None:
                        if point not in intersection:
                            intersection[point]=current_clip_polygon
            for i in range(len(subject_polygon)):
                current_subject_polygon = subject_polygon[i]
                if current_subject_polygon in intersection:
                    sign = 1 if self.determinant(Point(window.width-current_subject_polygon,0),prev(subject_polygon,subject_polygon.nodeat(i)).value-current_subject_polygon) > 0 else -1
                    sign2 = 1 if self.determinant(Point(window.width-current_subject_polygon,0),next(subject_polygon,subject_polygon.nodeat(i)).value-current_subject_polygon) > 0 else -1
                    if sign * sign2 < 0:
                        intersection.pop(current_subject_polygon)
            print("Количество пересечений %d" % (len(intersection)))
            if len(intersection) & 1 == 1:
                return Polygon.Status.IN
            else:
                return Polygon.Status.OUT

        intersections_ = {}
        polygons = []
        flag = True
        while flag:
            flag=intersections(intersections_,subject_polygon,clip_polygon)
        enter = linked_list.dllist()
        exit = linked_list.dllist()
        remake_intersections(intersections_,clip_polygon,subject_polygon)

        for i in range(len(subject_polygon)):
            current_subject_polygon = subject_polygon[i]
            if current_subject_polygon in intersections_:
                print("Анализ точки:",end=' ')
                print(current_subject_polygon,end='\n')
                sign = 1 if self.determinant(subject_polygon[(i+1)%(len(subject_polygon))]-subject_polygon[i],
                                            intersections_[current_subject_polygon][1]-intersections_[current_subject_polygon][0]) > 0 else -1
                if sign>0:
                    enter.appendright(current_subject_polygon)
                else:
                    exit.appendright(current_subject_polygon)


        if operation is Weilor_Azerton.Operation.Intersect:
            enter,exit = enter,exit
        elif operation is Weilor_Azerton.Operation.Union:
            enter,exit = exit,enter
        elif operation is Weilor_Azerton.Operation.Difference:
            enter,exit = exit,enter
            # Reverse order of a clip_polygon
            clip_polygon = linked_list.dllist(list(clip_polygon)[::-1])

        # self.enter_points = enter
        # self.exit_points = exit
        # return []

        subject_list = subject_polygon
        clip_list = clip_polygon

        print("Точки входа")
        for i in enter:
            print(i)
        print("Точки выхода")
        for j in exit:
            print(j)
        print("===============================")
        print("Отсекаемый многоугольник:")
        for a in subject_polygon:
            print(a)
        print("Отсекатель многоугольник:")
        for b in clip_polygon:
            print(b)


        while len(enter)>0:
            polygon = linked_list.dllist()
            start_point_polygon = enter.first.value
            count = 0
            transitionNode = enter.first
            while (transitionNode != None and (count == 0 or (count > 0 and start_point_polygon != transitionNode.value))):
                transitionNode = traverselist(subject_list,enter,exit,polygon,transitionNode,start_point_polygon,clip_list)
                subject_list, clip_list = clip_list, subject_list
                enter,exit = exit,enter
                count += 1
            polygons.append(polygon)
        for i in polygons:
            print(i)

        if len(polygons)==0:
            if len(subject_polygon) == 0 or len(clip_polygon)==0:
                return []
            status_clip_polygon = get_status(subject_polygon,clip_polygon)
            if operation is Weilor_Azerton.Operation.Intersect:
                if status_clip_polygon is not Polygon.Status.IN:
                    polygons.append(subject_polygon)
                else:
                    polygons.append(clip_polygon)
            elif operation==Weilor_Azerton.Operation.Union:
                if status_clip_polygon is not Polygon.Status.IN:
                    polygons.append(clip_polygon)
                else:
                    polygons.append(subject_polygon)
            elif operation==Weilor_Azerton.Operation.Difference:
                if status_clip_polygon is not Polygon.Status.IN:
                    pass
                else:
                    polygons.append(subject_polygon)
                    polygons.append(clip_polygon)
        return polygons



class Polygon(object):

    # class Status(Enum):
    #     IN = 1
    #     OUT = 2
    #     NOT_RECOGNIZE = 3


    def __init__(self):
        self.points = linked_list.dllist()
        #self.center_point = (0,0)

    def add_point(self,x,y):
        self.points.append(Point(x,y))
        # print(self.points)

    #def add_center_point(self,x,y):
    #self.center_point = (x,y)
    #for point in self.points:
    #    point.change_center(self.center_point)

    def draw(self,flag=True):

        if flag:
            glColor3f(0,1,0)
        else:
            glColor3f(1,0,0)

        for i in range(len(self.points)):
            glBegin(GL_LINES)
            if i == len(self.points)-1:
                glVertex2f(*self.points[i].print())
                glVertex2f(*self.points[0].print())
            else:
                glVertex2f(*self.points[i].print())
                glVertex2f(*self.points[i+1].print())
            glEnd()

        #if self.center_point != (0,0):
        #    glPointSize(10.0)
        #    glBegin(GL_POINTS)
        #    glColor3f(0,0,1)
        #    glVertex2f(*self.center_point)
        #    glEnd()

    def clear(self):
        self.points.clear()


class Point(object):


    def __init__(self,x,y,center = 0):
        self.x=x
        self.y=y
        #self.center = (0,0)
        #self.angle = self.calculate(x,y)
        #self.time = lambda: int(round(time.time() * 1000))
        #self.time = self.time()

    #def calculate(self,x,y):
        #return math.atan2(y-self.center[1],x-self.center[0])

    # def change_center(self,center):
    #     self.center = center
    #     self.angle = self.calculate(self.x,self.y)

    def print(self):
        return (self.x,self.y)

    def __lt__(self, other):
        return None
        #return self.time < other.time

    def __sub__(self, other):
        return Point(other.x-self.x,other.y-self.y)

    def __eq__(self, other):
        return (self.x,self.y) == (other.x,other.y)

    def __ne__(self, other):
        return not (self == other)

    def __hash__(self):
        return hash((self.x,self.y))

    def __str__(self):
        return "(x,y) : %f %f" % (self.x,self.y)



pyglet.gl.glClearColor(0,0,0,0)
global clip_polygon
global subject_polygon
clip_polygon = Polygon()
subject_polygon = Polygon()
algorithm = Weilor_Azerton(clip_polygon,subject_polygon)
global polygon
global operation
polygon = []
operation = False

@window.event
def on_draw():
    global polygon
    global operation
    global clip_polygon
    global subject_polygon
    window.clear()
    glClear(GL_COLOR_BUFFER_BIT)
    if not operation:
        clip_polygon.draw(flag=False)
        subject_polygon.draw()
    for i in range(len(polygon)):
        glColor3f(1,1,1)
        for j in range(len(polygon[i])):
            glBegin(GL_LINES)
            glVertex2f(*polygon[i][j].print())
            glVertex2f(*polygon[i][(j+1)%len(polygon[i])].print())
            glEnd()
    # for i in algorithm.enter_points:
    #     glPointSize(10.0)
    #     glColor3f(0,1,1)
    #     glBegin(GL_POINTS)
    #     glVertex2f(*i.print())
    #     glEnd()
    # for j in algorithm.exit_points:
    #     glPointSize(5.0)
    #     glColor3f(0,0,1)
    #     glBegin(GL_POINTS)
    #     glVertex2f(*j.print())
    #     glEnd()
    # for i in range(len(algorithm.points)):
    #     glPointSize(5.0)
    #     glColor3f(0,1,1)
    #     glBegin(GL_POINTS)
    #     glVertex2f(*algorithm.points[i].print())
    #     glEnd()

@window.event
def on_key_press(symbol,modifiers):
    #global center_mode
    global polygon
    global operation
    global clip_polygon
    global subject_polygon
    if symbol==key.D:
        polygon = algorithm.difference(algorithm.copy_subject_polygon,algorithm.copy_clip_polygon)
    elif symbol==key.U:
        polygon = algorithm.union(algorithm.copy_subject_polygon,algorithm.copy_clip_polygon)
    elif symbol==key.I:
        polygon = algorithm.intersection(algorithm.copy_subject_polygon,algorithm.copy_clip_polygon)
    elif symbol==key.O:
        if operation:
            operation = False
        else:
            operation = True
    elif symbol==key.R:
        clip_polygon,subject_polygon = subject_polygon, clip_polygon
        algorithm.subject_polygon,algorithm.clip_polygon = algorithm.clip_polygon,algorithm.subject_polygon
        algorithm.reverse = True
    elif symbol==key.SPACE:
        clip_polygon.clear()
        subject_polygon.clear()
    elif symbol==key.C:
        polygon = []
    #elif symbol==key.S:
    #    center_mode = True

@window.event
def on_mouse_press(x,y,button,modifiers):
    #global center_mode
    #if button & mouse.LEFT and center_mode:
    # print("WOW")
    #    clip_polygon.add_center_point(x,y)
    #    center_mode = False
    #    return None
    #elif button & mouse.RIGHT and center_mode:
    #    subject_polygon.add_center_point(x,y)
    #print("WOW")
    #center_mode = False
    #return None
    if button & mouse.LEFT:
        clip_polygon.add_point(x,y)
    elif button & mouse.RIGHT:
        subject_polygon.add_point(x,y)



@window.event
def on_resize(width,height):
    if height < 0:
        height = 1
    glViewport(0,0,width,height)
    glMatrixMode(GL_PROJECTION)
    glLoadIdentity()
    gluOrtho2D(0,width,0,height)
    glMatrixMode(GL_MODELVIEW)


if __name__ == '__main__':
    pyglet.app.run()