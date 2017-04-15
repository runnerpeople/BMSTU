#!/usr/bin/env python
# -*- coding: utf-8 -*-
import socket,sys,select
__author__ = 'great'

def connect_socket():
    try:
        s.connect(("localhost",8080))
    except:
        print("Error in connect")
        sys.exit(1)

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
print("It is traffic_light program. Please put a command. If you don't know command, input 'help'")
s.settimeout(1)
connect_socket()
try:
    while True:
        print(">>>",end="",flush=True)
        read_sockets,write_sockets,error_sockets = select.select([sys.stdin,s],[],[])
        for sock in read_sockets:
            if sock == s:
                data = sock.recv(4096)
                if not data:
                    print("Recv return 0 bytes")
                    raise Exception
                else:
                    sys.stdout.flush()
                    print("Server: " + data.decode("utf-8"))
            else:
                msg = input().encode("utf-8")
                s.send(msg)
except:
    pass
finally:
    s.shutdown(socket.SHUT_RDWR)
    s.close()
    print('Console: Sockets are closed')

