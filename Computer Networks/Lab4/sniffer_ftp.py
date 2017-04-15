#!/usr/bin/python
# -*- coding: utf-8 -*-
import pcap
import socket
import struct

__author__ = "great"

global setIp
global me
global server
me = None
server = None
setIp = False

def print_packet(pktlen,data,timestamp):
    global setIp
    global me
    global server
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
    t = iph_length + eth_length
    tcp_header = data[t:t+20]
    tcph = struct.unpack('!HHLLBBHHH' , tcp_header)
    doff_reserved = tcph[4]
    tcph_length = doff_reserved >> 4
    content_ = data[14+iph_length+tcph_length*4:]
    if len(content_)>0 and not setIp:
        # content_.find("Welcome") -> error
        me = s_addr
        server = d_addr
        setIp = True

    def content():
        print "____________________________"
        print "Source IP " + s_addr
        print "Destination IP " + d_addr
        if s_addr == server:
            print "Response: " + content_
        elif s_addr == me:
            print "Request: " + content_
        print "____________________________"

    if len(content_)>0:
        content()



if __name__=='__main__':
    p = pcap.pcapObject()
    dev = "wlan0"
    filter_sniffing = "port ftp"
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
