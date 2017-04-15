#!/usr/bin/python
# -*- coding: utf-8 -*-
import pcap
import sys
import socket
import struct

__author__="great"

global content_length_var
content_length_var = 0


def print_packet(pktlen,data,timestamp):
    global content_length_var
    if not data:
        return None
    eth_length = 14
    ip_header = data[eth_length:20+eth_length]
    iph=struct.unpack('!BBHHHBBH4s4s',ip_header)
    version_ihl=iph[0]
    version = version_ihl>>4
    ihl = version_ihl & 0xF
    iph_length = ihl *4
    s_addr = socket.inet_ntoa(iph[8])
    d_addr = socket.inet_ntoa(iph[9])
    print "Source IP " + s_addr
    print "Destination IP " + d_addr
    t = iph_length + eth_length
    tcp_header = data[t:t+20]
    tcph = struct.unpack('!HHLLBBHHH' , tcp_header)
    doff_reserved = tcph[4]
    tcph_length = doff_reserved >> 4
    content = str(data+str(tcph_length))
    if content.find("\r\n\r\n")==-1:
        print "____________________________"
        return None
    http_data_pos = content.find("\r\n\r\n")
    http_header = content[:http_data_pos]

    def content_type():
        global content_length_var
        start = http_header.find("Content-Type")
        if start==-1:
            content_length_var = None
            print "Content-Type: \033[31mno\033[0m"
            return None
        start += len("Content-Type")
        num_of_chars = http_header.find("\r\n",start)
        print "Content-Type" + http_header[start:num_of_chars]

    def content_length():
        global content_length_var
        start = http_header.find("Content-Length: ")
        if start==-1:
            content_length_var = None
            print "Content-Length: \033[31mno\033[0m"
            return None
        start += len("Content-Length: ")
        content_length_var = int(http_header[start:])
        print "Content-Length: " + str(content_length_var)

    def urlencoded():
        global content_length_var
        start = 0
        print "Content: "
        print content
        while start<content_length_var:
            end = content.find("=",start)
            print "\tKey: \033[32m" + content[start:end] + "\033[0m"
            if end == -1:
                return None
            start = end + 1
            end = content.find("&", start)
            if end == -1:
                end = len(content)
            print "\tValue: \033[32m" + content[start:end] + "\033[0m"
            print "\t==================="
            if end == -1:
                return None
            start = end + 1

    content_type()
    content_length()
    if (content_length_var is not None) and (http_header.find("urlencoded") != -1):
        content=content[http_data_pos+4:content_length_var+http_data_pos+4]
        urlencoded()
    print "____________________________"



if __name__=='__main__':
    p = pcap.pcapObject()
    dev = "wlan0"
    filter_sniffing = "tcp port 80 && (((ip[2:2] - ((ip[0]&0xf)<<2)) - ((tcp[12]&0xf0)>>2)) != 0)"
    net, mask = pcap.lookupnet(dev)
    print "Open device " + dev
    p.open_live(dev,65536,1,512)
    p.setfilter(filter_sniffing,0,0)
    try:
        p.loop(-1,print_packet)
    except BaseException as e:
        print e.message
        print "Shut down"
        print '%d packets received, %d packets dropped, %d packets dropped by interface' % p.stats()
