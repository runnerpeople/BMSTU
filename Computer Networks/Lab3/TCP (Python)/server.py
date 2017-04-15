#!/usr/bin/env python
# -*- coding: utf-8 -*-
import socket
import select
__author__ = 'great'


class Traffic_light():
    def __init__(self):
        self.status = True  # Ok - True
        self.light = "Red" # "Red", "Yellow", "Green" - three status of traffics

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
            return None
        else:
            return "Error to change on color " + light

traffic = Traffic_light()
socket_list = []
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.setsockopt(socket.SOL_SOCKET,socket.SO_REUSEADDR,1)
s.bind(('localhost', 8080))
s.listen(10)
socket_list.append(s)
print("Server started on port " + str(8080))
try:
    while True:
        read_sockets,write_sockets,error_sockets = select.select(socket_list,[],[],0)
        for sock in read_sockets:
            if sock == s:
                conn,addr = s.accept()
                socket_list.append(conn)
                print("Сlient " + str(addr[0]) + ":"  + str(addr[1]) + " has connected to server ")
            else:
                data = sock.recv(1024)
                data = data.decode('utf-8')
                parse = data.split(" ")
                if not data:
                    sock.send(b'')
                    continue
                if parse[0] == "get":
                    status = traffic.get_status()
                    sock.send(status.encode("utf-8"))
                elif parse[0] == "change":
                    if len(parse) == 1:
                        sock.send(b'Error! You should choose color of traffic_light')
                        continue
                    status = traffic.change(parse[1])
                    if status is None:
                        sock.send(b'Changed')
                    else:
                        sock.send(status.encode("utf-8"))
                elif parse[0] == "turn_on" or parse[0] == "on":
                    if traffic.is_turn_on() is False:
                        traffic.turn_on()
                        sock.send(b'Turn on traffic light')
                    else:
                        sock.send(b'Have already turned on traffic light')
                elif parse[0] == "turn_off" or parse[0] == "off":
                    if traffic.is_turn_on() is True:
                        traffic.turn_off()
                        sock.send(b'Turn off traffic light')
                    else:
                        sock.send(b'Have already turned off traffic light')
                elif parse[0] == "help":
                    help_label = b'get  - Get a status of traffic_light\n' \
                         b'change (light) - Change color if name_traffic light is on. Color must be "Red","Yellow","Green"\n' \
                         b'turn_on or on - Turn on traffic light\n' \
                         b'turn_off or off - Turn off traffic light\n' \
                         b'help - Help program\n'
                    sock.send(help_label)
                else:
                    sock.send(b'Error in command - ' + data.encode("utf-8"))
finally:
    for s in socket_list:
        s.shutdown(socket.SHUT_RDWR)
        s.close()
    print("Server is closed")

