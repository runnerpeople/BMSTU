#!/usr/bin/env python
# -*- coding: utf-8 -*-
import socket

__author__ = 'great'

buf_size = 4096

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
print("Connecting...")
s.connect(("localhost",8080))
print("Connected")
print("It is network logger. Please put a command. If you don't know command, input 'help'")
while True:
    try:
        msg = input("Your_answer:")
        if msg=="exit":
            break
        s.send(msg.encode("utf-8"))
        data,addr =s.recvfrom(4096)
        data = data.decode("utf-8")
        if not data:
            continue
        print("Server: "  + data)
    except KeyboardInterrupt:
        break
print("Disconnecting...")
s.close()
