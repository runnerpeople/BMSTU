#!/usr/bin/env python
# -*- coding: utf-8 -*-
import socket
__author__ = 'great'

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
print("It is traffic_light program. Please put a command. If you don't know command, input 'help'")
while True:
    msg = input("Your_answer:").encode("utf-8")
    s.sendto(msg,("localhost",8080))
    data,addr =s.recvfrom(1024)
    data = data.decode("utf-8")
    if not data:
        continue
    print("Server: "  + data)
s.close()

