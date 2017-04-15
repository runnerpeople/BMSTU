#!/usr/bin/python3
# -*- coding: utf-8 -*-
__author__="great"

from pyglet.gl import *
from pyglet.window import mouse
from pyglet.clock import *
import random
import math
import time

class WindowOptions:

    def __init__(self,window_size):
        self.width = window_size[0]
        self.height = window_size[1]
        self.max_circles = 10
        self.circles = []

    def update(self,dt):
        if len(self.circles)<self.max_circles:
            self.circles.append(Circle(random.randint(10,self.width),0))
        for i in self.circles:
            i.dy()
        self.circles = [x for x in self.circles if not x.lost(self.height)]


class Circle:
    def __init__(self,x,y):
        self.x=x
        self.y=y
        self.r=random.randint(40,50)
        self.speed = random.randint(10,20)
        self.create_time = int(round(time.time()*1000.0))
        self.rgb = (random.randint(0,100)/100,random.randint(0,100)/100,random.randint(0,100)/100)
        self.draw()

    def draw(self):
        glColor3f(*self.rgb)
        glEnable(GL_BLEND)
        glBegin(GL_LINE_LOOP)
        for i in range(25):
            glVertex2f(self.x,self.y)
            alpha = 2*i*math.pi/25
            glVertex2f(self.x + math.sin(alpha)*self.r,self.y + math.cos(alpha)*self.r)
        glEnd()
        glDisable(GL_BLEND)
        glFlush()

    def dy(self):
        current_time = int(round(time.time()*1000.0))
        self.y += int((current_time-self.create_time)*self.speed/5000)


    def inside(self,x,y):
        dx = self.x-x
        dy = self.y-y
        return dx*dx+dy*dy<=self.r*self.r

    def lost(self,width):
        return self.y-self.r > width



window = pyglet.window.Window(800,600,resizable = False,caption="Lab 1")
window.set_mouse_visible(True)
gl.glClearColor(0,0,0,0)
glClear(GL_COLOR_BUFFER_BIT)
global count
count = 0
game = WindowOptions((800,600))
fps_display = pyglet.clock.ClockDisplay(format='%(fps).1f fps')

@window.event
def on_draw():
    global count
    window.clear()
    for circles in game.circles:
        circles.draw()
    label = pyglet.text.Label("Count: " + str(count),font_size=25,y=window.height-50)
    fps_display.draw()
    #label1 = pyglet.text.Label("FPS: " + str(float(pyglet.clock.ClockDisplay(format="%(fps).1ffps"))),font_size=25,x=window.width-250,y=window.height-50)
    label.draw()
   # label1.draw()

@window.event
def on_mouse_press(x,y,button,modifiers):
    global count
    if button == mouse.LEFT or button == mouse.RIGHT:
        index = -1
        for circle in game.circles:
            if circle.inside(x,y):
                index = game.circles.index(circle)
                break
        if index != -1:
            game.circles.remove(game.circles[index])
            count += 1


pyglet.clock.schedule_interval(game.update,1/10)
pyglet.app.run()

