#!/usr/bin/env python
# -*- coding: utf-8 -*-
import socket
import os
import threading
import datetime
__author__ = 'great'

socket_list = []
if not os.path.exists(os.getcwd() + "/info.txt"):
    open("info.txt",mode="w").close()

def create_socket(ports):
    lock = threading.Lock()
    for port in ports:
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.bind(('localhost', port))
        socket_list.append(s)
        threading.Thread(target=parse_answer,args=(s,lock)).start()
        print("Server start at port " + str(port))

def parse_answer(s,lock):
    while True:
        data, addr = s.recvfrom(4096)
        parse = data.decode("utf-8").split(" ")
        with lock:
            if not data:
                s.sendto(b'',addr)
                continue
            print(datetime.datetime.now().strftime("%d.%m.%Y %H:%M:%S") + " (" + str(addr[0]) + ":" + str(addr[1]) +")" +
                  " >> " + data.decode("utf-8"))
            if parse[0]=="send":
                if len(parse)<2 or parse[1] not in ["info","warn","error"]:
                    s.sendto(b'Error in command send',addr)
                    continue
                try:
                    file = open("info.txt",mode="a")
                    file.write(str(addr[0]) + ":" + str(addr[1]) + " | " + datetime.datetime.now().strftime("%d.%m.%Y %H:%M:%S") +
                            " | " + parse[1] + " | " + parse[2] + "\n")
                    file.close()
                    s.sendto(b'Success send message',addr)
                    continue
                except:
                    s.sendto(b'Error in output_file',addr)
                    continue
            elif parse[0]=="get":
                if parse[1]=="level" and len(parse)==3:
                    file = open("info.txt",mode="r")
                    result = [line.replace("\n","") for line in file if line.split(" | ")[2] == parse[2]]
                    file.close()
                    if result == []:
                        s.sendto(b'No messages with ' + parse[2].encode("utf-8"),addr)
                    else:
                        s.sendto(b'\n' + "\n".join(result).encode("utf-8"),addr)
                    continue
                elif len(parse)==3:
                    file = open("info.txt",mode="r")
                    time = parse[1] + " " + parse[2]
                    result = [line.replace("\n","") for line in file if line.split(" | ")[1] >= time]
                    file.close()
                    if result == []:
                        s.sendto(b'No messages at ' + time.encode("utf-8"),addr)
                    else:
                        s.sendto(b'\n' + "\n".join(result).encode("utf-8"),addr)
                    continue
                elif len(parse)==5:
                    file = open("info.txt",mode="r")
                    start = parse[1] + " " + parse[2]
                    end = parse[3] + " " + parse[4]
                    result = [line.replace("\n","") for line in file if end >= line.split(" | ")[1] >= start]
                    file.close()
                    if result == []:
                        s.sendto(b'No messages at ' + start.encode("utf-8") + b' ' + end.encode("utf-8"),addr)
                    else:
                        s.sendto(b'\n' + "\n".join(result).encode("utf-8"),addr)
                    continue
                else:
                    s.sendto(b'Error in command get',addr)
                    continue
            elif parse[0]=="clear":
                open("info.txt",mode="w").close()
                s.sendto(b'Success clear',addr)
                continue
            else:
                s.sendto(b'Unknown command',addr)
                continue
    s.close()

create_socket([8080,8081,8082])