#ifndef DATA_H
#define DATA_H

#include<stdio.h>
#include<string.h>
#include<errno.h>
#include<stdlib.h>
#include<netdb.h>
#include<netinet/in.h>
#include<sys/socket.h>
#include <sys/types.h>
#include<arpa/inet.h>
#include<unistd.h>

#define packet 8

int receive(int sock,char* buffer) {
	int size_recv,total_size=0;
	int number_packets=0;
	while(1) {
		if ((size_recv = recv(sock,buffer+packet * number_packets,packet,0)) <= 0) {
			break;
		}
		else {
			total_size += size_recv;
			number_packets++;
		}
	}
	return total_size;
}


int sendall(int s, char *buf, int len) {
    	int total = 0;
    	int n;
    	while(total < len){
        	n = send(s, buf+total, len-total,0);
        	if(n == -1) {
			 break; 
		}
        	total += n;
    	}
    	return (n==-1 ? -1 : total);
}

int recvall(int s, char *buf, int len) {
    	int total = 0;
    	int n;
    	while(total < len){
        	n = recv(s, buf+total, len-total,0);
        	if(n == -1) {
			break;
		}
        	total += n;
    	}
    	return (n==-1 ? -1 : total);
}
	
#endif /* DATA_H */
