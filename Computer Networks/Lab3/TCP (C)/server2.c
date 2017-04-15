#include<stdio.h>
#include<string.h>
#include<errno.h>
#include<stdlib.h>
#include<netdb.h>
#include<netinet/in.h>
#include<sys/socket.h>
#include<sys/types.h>
#include<sys/time.h>
#include<arpa/inet.h>
#include<unistd.h>

#define buf_size 1024

typedef struct Device Device;
struct Device {
    int status;
    char type[10];
};

Device* init() {
        Device *d;
        d = (Device*)malloc(sizeof(Device));
        d->status = 0; //turn off
        return d;
}

int turn_on(Device* device) {
    if (device->status==0) {
        device->status=1;
        return 1;
    }
    else
        return 0;
}
        
int turn_off(Device* device) {
    if (device->status==1) {
        device->status=0;
        return 1;
    }
    else
        return 0;
}        

char* status_method(Device* device) {
    int r = rand() % 3;
    char *types[]={"good","satisfactory","bad"};
    char *p = (char*)malloc(30 * sizeof(char));
    strcpy(p,types[r]);
    strcat(p," is now at time");
    if (device->status==0)
	strcpy(p,"Device is off");
    return p;
}

int sendall(int socket, char *buf, int len) {
    int total = 0;
    int n;
    while(total < len){
        n = send(socket, buf+total, len-total, 0);
        if(n == -1) { break; }
	total += n;
    }
    return (n==-1 ? -1 : total);
}

int recvall(int socket, char *buf, int len) {
    int total = 0;
    int n;
    while(total < len){
        n = recv(socket, buf+total, len-total, 0);
        if(n == -1) {break; }
        total += n;
    }
    return (n==-1 ? -1 : total);
}

void print_message(char *dst,char* from) {
	dst[0] = strlen(from);
	int count = 1,i;
    	for (i = 0; from[i]; i++)
            	dst[count++] = from[i];
    	dst[count] = '\0';
}
	
    
void parse(int sock,Device* device,int *status_return) {
    int n,status,len;
    int totalpacket = 0;
    char buffer[buf_size];
    bzero(buffer,buf_size);
    n = recv(sock,buffer,1,0);
    if (n>0) {
	len=buffer[0];
	bzero(buffer,buf_size);
    }
    n = recvall(sock,buffer,len);
    if (n==0) {
	*status_return = 1;
	return;
    }
    if (n < 0) {
	puts("Error reading to socket");
        exit(1);
    }
    if (strcmp(buffer,"on")==0) {
        status = turn_on(device);
        if (status==1) {
	    print_message(buffer,"Device is turn on");
            n = sendall(sock,buffer,strlen(buffer));
            if (n < 0) {
                puts("Error writing to socket");
                exit(1);
            }
        }
        else {
	    print_message(buffer,"Device has already turn on");
            n = sendall(sock,buffer,strlen(buffer));
            if (n < 0) {
                puts("Error writing to socket");
                exit(1);
            }
        }
    }
    else {
        if (strcmp(buffer,"off")==0) {
                status = turn_off(device);
                if (status==1) {
		    print_message(buffer,"Device is turn off");
                    n = sendall(sock,buffer,strlen(buffer));
                    if (n < 0) {
                        puts("Error writing to socket");
                        exit(1);
                    }
                }
                else {
		    print_message(buffer,"Device has already turn off");
                    n = sendall(sock,buffer,strlen(buffer));
                    if (n < 0) {
                        puts("Error writing to socket");
                        exit(1);
                    }
                }
        }
        else {
            if (strcmp(buffer,"status")==0) {
                char *status_string = status_method(device);
		print_message(buffer,status_string);
                n = sendall(sock,buffer,strlen(buffer));
                    if (n < 0) {
                        puts("Error writing to socket");
                        exit(1);
                    }
		free(status_string);
            }
            else {
                if (strcmp(buffer,"status_device")==0) {
                    if (device->status==0) {
			  print_message(buffer,"Device isn't working");
                          n = sendall(sock,buffer,strlen(buffer));
                          if (n < 0) {
                                puts("Error writing to socket");
                                exit(1);
                          }
                    }
                    else {
			  print_message(buffer,"Device is working");
                          n = sendall(sock,buffer,strlen(buffer));
                          if (n < 0) {
                                puts("Error writing to socket");
                                exit(1);
                          }
                    }  
                }
                else {	
			print_message(buffer,"Unknown command");
                        n = sendall(sock,buffer,strlen(buffer));
                        if (n < 0) {
                              puts("Error writing to socket");
                              exit(1);
                        }
                }        
            }
        }
    }
}

int main(int args,char **argv) {
     srand(time(NULL));
     Device *device = init();
     struct sockaddr_in server;
     int socket_serv,pid,i,sd,max_sd,activity_socket,dataread,status,addrlen,new_socket;
     int opt = 1;
     int max_clients = 10,client_socket[10];
     for(i=0;i<max_clients;i++)
	client_socket[i]=0;
     fd_set readfds;
     socket_serv = socket(AF_INET,SOCK_STREAM, 0);
     if (argv[1]==NULL) {
	puts("Error in argv");
	return 1;
     }
     if (socket_serv==-1) {
         puts("Couldn't create a socket");
         return 1;
     }
     if(setsockopt(socket_serv,SOL_SOCKET,SO_REUSEADDR,(char*)&opt,sizeof(opt))<0) {
	puts("Error in setsockopt");
	return 1;
     }
     puts("Socket is created");
     int port = atoi(argv[1]);
     //bzero((char*)&server,sizeof(server));
     server.sin_family = AF_INET;
     server.sin_addr.s_addr=INADDR_ANY;
     server.sin_port=htons(port);
     if (bind(socket_serv,(struct sockaddr*)&server,sizeof(server))<0) {
         puts("Bind failed. ERROR!");
         return 1;
     }
     printf("Bind done on port ");
     printf("%d\n",port);
     listen(socket_serv,10);
     addrlen = sizeof(server);
     puts("Waiting for incoming clients...");
     while(1) { 
	FD_ZERO(&readfds);
	FD_SET(socket_serv,&readfds);
	max_sd = socket_serv;
	for(i=0;i<max_clients;i++) {
		sd = client_socket[i];
		if (sd>0)
			FD_SET(sd,&readfds);
		if (sd > max_sd)
			max_sd = sd; 
     	}
	activity_socket = select(max_sd + 1,&readfds,NULL,NULL,NULL);
	if ((activity_socket<0) && (errno!=EINTR)) {
		puts("Error in select");
         	return 1;
	}
	if(FD_ISSET(socket_serv,&readfds)) {
		new_socket = accept(socket_serv,(struct sockaddr*)&server,(socklen_t*)&addrlen);
		if (new_socket < 0) {
			puts("Error in accept");
			return 1;
		}
		printf("New connection, socket is: %s:%d \n", inet_ntoa(server.sin_addr),ntohs(server.sin_port));
		for(i = 0; i<max_clients;i++) {
			if(client_socket[i]==0) {
				client_socket[i]=new_socket;
				puts("Adding to list sockets");
				break;
			}
		}
	}
	for(i=0;i<max_clients;i++) {
		sd=client_socket[i];
		if (FD_ISSET(sd,&readfds)) {
			status = 0;
			parse(sd,device,&status);
			if (status == 1) {
				getpeername(sd,(struct sockaddr*)&server,(socklen_t*)&addrlen);
				printf("Host disconnected, socket is: %s:%d \n",inet_ntoa(server.sin_addr),ntohs(server.sin_port));
				close(sd);
				client_socket[i]=0;
			}
		}			
	}
     }
     return 0;
}
        
