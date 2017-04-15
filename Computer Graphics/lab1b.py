#!/usr/bin/python3
# -*- coding: utf-8 -*-
__author__="great"

from pyglet.gl import *
from pyglet.window import mouse,key
from pyglet.clock import *
import random
import math
import time

class WindowOptions:

    def __init__(self,window_size):
        self.width = window_size[0]
        self.height = window_size[1]
        self.max_circles = 7
        self.circles = []
        self.weapons = []
        self.max_weapon = 3
        self.count = 0

    def update(self,dt):
        if len(self.circles)<self.max_circles:
            self.circles.append(Circle(random.randint(10,self.width),self.height))
        self.kill_circle()
        for i in self.circles:
            i.dy()
        for weapon in self.weapons:
            weapon.dy(flag=True)
        self.circles = [x for x in self.circles if not x.lost(self.height)]
        self.weapons = [x for x in self.weapons if not x.lost(900,flag=False)]


    def blaster(self,x,y):
        if len(self.weapons)<self.max_weapon:
            self.weapons.append(Circle(x,y,flag=True))

    def collision(self,weapon,circle):
        return math.sqrt(math.pow((weapon.y - circle.y),2) + math.pow((weapon.x - circle.x),2)) <= (weapon.r + circle.r)

    def kill_circle(self):
        for weapon in self.weapons:
           for circle in list(self.circles):
               if self.collision(weapon,circle):
                   self.circles.remove(circle)
                   self.count +=1


class Circle:
    def __init__(self,x,y,flag = False):
        self.x=x
        self.y=y
        if flag:
            self.r=20
            self.speed = 100
            self.rgb=(1,0,0)
        else:
            self.r=random.randint(40,50)
            self.speed = random.randint(10,20)
            self.rgb = (random.randint(0,100)/100,random.randint(0,100)/100,random.randint(0,100)/100)
        self.create_time = int(round(time.time()*1000.0))
        self.draw()

    def draw(self):
        glColor3f(*self.rgb)
        glEnable(GL_BLEND)
        glBegin(GL_POLYGON)
        for i in range(25):
            alpha = 2*i*math.pi/25
            glVertex2f(self.x + math.sin(alpha) * self.r,self.y + math.cos(alpha) * self.r)
        glEnd()
        glDisable(GL_BLEND)
        glFlush()

    def dy(self,flag = True):
        current_time = int(round(time.time()*1000.0))
        if flag:
            self.y -= int((current_time-self.create_time)*self.speed/5000)
        else:
            self.y += int((current_time-self.create_time)*self.speed/5000)

    def inside(self,x,y):
        dx = self.x-x
        dy = self.y-y
        return math.sqrt(dx*dx+dy*dy)<=self.r

    def lost(self,width,flag = True):
        if flag:
            return self.y-self.r < 0
        else:
            return self.y-self.r > width



class Triangle:
    def __init__(self,height):
        self.x = height/2
        self.y = 50
        self.rgb = (random.randint(0,100)/100,random.randint(0,100)/100,random.randint(0,100)/100)
        self.draw()

    def draw(self):
        glColor3f(*self.rgb)
        glEnable(GL_BLEND)
        glBegin(GL_TRIANGLES)
        glVertex2f(self.x-80,self.y)
        glVertex2f(self.x+80,self.y)
        glVertex2f(self.x,self.y+80)
        glEnd()
        glDisable(GL_BLEND)
        glFlush()

    def move_left(self):
        if self.x-60>0:
            self.x-= 60

    def move_right(self,height):
        if self.x+60<height:
            self.x += 60



window = pyglet.window.Window(1600,900,resizable = False,caption="Lab 1")
window.set_mouse_visible(False)
gl.glClearColor(0,0,0,0)
glClear(GL_COLOR_BUFFER_BIT)
game = WindowOptions((1600,900))
blaster = Triangle(window.width)

@window.event
def on_draw():
    global count
    window.clear()
    for circles in game.circles:
        circles.draw()
    for weapon in game.weapons:
        weapon.draw()
    label = pyglet.text.Label("Count: " + str(game.count),font_size=25,y=50)
    label1 = pyglet.text.Label("FPS " + str(int(pyglet.clock.get_fps())),font_size=25,x=window.width-250,y=50)
    label.draw()
    label1.draw()
    blaster.draw()


""" @window.event
def on_mouse_press(x,y,button,modifiers):
    global count
    if button == mouse.LEFT or button == mouse.RIGHT:
        index = -1
        for circle in game.circles:
            if circle.inside(x,y):
                index = game.circles.index(circle)
                break
        if index != -1:
            game.circles.remove(game.circles[index]) """

@window.event
def on_key_press(symbol,modifiers):
    if symbol == key.MOTION_LEFT:
        blaster.move_left()
    elif symbol == key.MOTION_RIGHT:
        blaster.move_right(window.width)
    elif symbol == key.SPACE:
        game.blaster(blaster.x,blaster.y+90)



pyglet.clock.schedule_interval(game.update,1/10)

pyglet.app.run()
