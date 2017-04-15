#!/usr/bin/env python
# -*- coding: utf-8 -*-
import socket
import threading
__author__ = 'great'


class Traffic_light():
    def __init__(self):
        self.status = True  # Ok - True
        self.light = "Red"

    def is_turn_on(self):
        return self.status

    def get_status(self):
        if self.status:
            return self.light
        else:
            return "turn_off"

    def turn_on(self):
        if self.status is not True:
            self.status = True

    def turn_off(self):
        if self.status is True:
            self.status = False

    def change(self, light):
        if self.status is False:
            return "Can't change and traffic light have turned off"
        if light in ["Red", "Yellow", "Green"]:
            self.light = light
            pass
        else:
            return "Error to change on color " + light

def check_variable(parse):
    if (len(parse)==2 and parse[1] in ["Red", "Yellow", "Green"] or (len(parse)==1 and parse[0] in
        ["get","on","off","turn_on","turn_off","change","help","new","traffics"])):
        return "default"  # Default value
    elif len(parse)==2 and (parse[1] in traffics) and (parse[1] not in ["Red", "Yellow", "Green"]):
        return parse[1]
    elif len(parse)==3 and (parse[2] in traffics):
        return parse[2]
    else:
        pass

def server_answer(s,lock):
        while True:
            data, addr = s.recvfrom(1024)
            #print("Connected to port " + str(s.getsockname()[1]) + " client with " + "(" + str(addr[0]) + str(addr[1]) + ")")
            data = data.decode('utf-8')
            parse = data.split(" ")
            with lock:
                if not data:
                    s.sendto(b'', addr)
                    continue
                variable = check_variable(parse)
                if variable is None and parse[0]!="new":
                    s.sendto(b'Error in command - ' + data.encode("utf-8"), addr)
                    continue
                if parse[0] == "get":
                    status = traffics[variable].get_status()
                    s.sendto(status.encode("utf-8"), addr)
                elif parse[0] == "change":
                    if len(parse) == 1:
                        s.sendto(b'Error! You should choose color of traffic_light - ' + variable.encode("utf-8"), addr)
                        continue
                    status = traffics[variable].change(parse[1])
                    if status is None:
                        s.sendto(b'Changed', addr)
                    else:
                        s.sendto(status.encode("utf-8"), addr)
                elif parse[0] == "turn_on" or parse[0] == "on":
                    if traffics[variable].is_turn_on() is False:
                        traffics[variable].turn_on()
                        s.sendto(b'Turn on traffic light - ' + variable.encode("utf-8"), addr)
                    else:
                        s.sendto(b'Have already turned on traffic light - ' + variable.encode("utf-8"), addr)
                elif parse[0] == "turn_off" or parse[0] == "off":
                    if traffics[variable].is_turn_on() is True:
                        traffics[variable].turn_off()
                        s.sendto(b'Turn off traffic light - ' + variable.encode("utf-8"), addr)
                    else:
                        s.sendto(b'Have already turned off traffic light - ' + variable.encode("utf-8"), addr)
                elif parse[0] == "help":
                    help_label = b'\nget (name_traffic) - Get a status of name_traffic light\n' \
                                 b'change (light) (name_traffic) - Change color if name_traffic light is on. Color must be "Red","Yellow","Green"\n' \
                                 b'turn_on or on (name_traffic) - Turn on name_traffic light\n' \
                                 b'turn_off or off (name_traffic)- Turn off name_traffic light\n' \
                                 b'help - Help program\n' \
                                 b'new (name_traffic) - Add new traffic light with variable name - "name_traffic"\n' \
                                 b'traffics - Print all possible traffics. If do not create traffics, you have 1 default "traffic"'
                    s.sendto(help_label, addr)
                elif parse[0] == 'new':
                    if len(parse) == 1:
                        s.sendto(b'Error! You should named of new variable', addr)
                        continue
                    if parse[1] in traffics:
                        s.sendto(b'This traffic ' + parse[1].encode('utf-8') + b' have already exists', addr)
                    else:
                        traffics[parse[1]] = Traffic_light()
                        s.sendto(b'Now you have ' + str(len(traffics)).encode('utf-8') + b' traffic lights', addr)
                elif parse[0] == 'traffics':
                    enabled_traffics = ""
                    for key, value in traffics.items():
                        enabled_traffics = enabled_traffics + key + "\n"
                    s.sendto(b'Possible traffics are:\n' + enabled_traffics.encode('utf-8'),addr)
                else:
                    s.sendto(b'Error in command - ' + data.encode("utf-8"), addr)
        s.close()

def socket_threading(ports):
    lock = threading.Lock()
    for port in ports:
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.bind(('localhost', port))
        socket_list.append(s)
        t = threading.Thread(target=server_answer,args=(s,lock,))
        t.start()
        print("Server start at port " +str(port))

socket_list = []
socket_threading((8080,8081,8082))

default_traffic = Traffic_light()
# For many traffics
traffics = {"default": default_traffic}

