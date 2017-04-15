#include<stdio.h>
#include<string.h>
#include<errno.h>
#include<stdlib.h>
#include<netdb.h>
#include<netinet/in.h>
#include<sys/socket.h>
#include<sys/types.h>
#include<arpa/inet.h>
#include<unistd.h>

#define buf_size 1024

void print_message(char *dst,char* from) {
	int count = 1,i;
    	for (i = 0; from[i]; i++)
		if (from[i] != '\n' && from[i] != ' ' && from[i] != '\t')
            		dst[count++] = from[i];
    	dst[count] = '\0';
	dst[0] = strlen(dst)-1;
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

int main(int argc,char **argv) {
    struct sockaddr_in server;
    struct hostent *server2;
    int socket_serv, client_sock,clilen,c,n;
    char buffer[buf_size];
    char message[buf_size];
    socket_serv = socket(AF_INET, SOCK_STREAM, 0);  
    if (argv[1]==NULL) {
	puts("Error in argv");
	return 1;
     }  
    if (socket_serv < 0) {    
        puts("Error creating socket!");    
        exit(1);    
    }    
    puts("Socket created...");  
    bzero((char*)&server,sizeof(server));
    server.sin_family = AF_INET;
    server2 = gethostbyname("localhost");
    bcopy((char *)server2->h_addr, (char *)&server.sin_addr.s_addr,server2->h_length);
    server.sin_port=htons(atoi(argv[1]));
    if (connect(socket_serv,(struct sockaddr *) &server,sizeof(server)) < 0)  {
        puts("Error connecting");
        exit(1);
    }
    puts("Please enter the message");
    while(1) {
	int len;
        bzero(buffer,buf_size);
        int count = 0;
    	do {		
		scanf("%c",&buffer[count]);
		if ((buffer[count]==' ' || buffer[count]=='\n' || buffer[count]=='\t') && count==0) {
			break;
		}	
	}
	while (buffer[count++] != ' ');
	buffer[count]='\0';
	if (buffer[0]=='\0') { 
		continue; 
	}
	print_message(message,buffer);
	if (strstr(buffer,"help")!=NULL) {
            printf("Device has 4 functions:\n");
            printf("1. Turn off the device. If you want to do this type 'off'\n");
            printf("2. Turn on the device. If you want to do this type 'on'\n");
            printf("3. Check device status. If you want to do this type 'status'\n");
            printf("4. Check index of turned on device. If you want to do this type 'status_device'\n");
            continue;
        }
	if (strstr(message,"exit")!=NULL)
		break;
       n = sendall(socket_serv,message,strlen(message));
        if (n < 0) {
            perror("ERROR writing to socket");
            exit(1);
        }
        bzero(buffer,buf_size);
	n = recv(socket_serv,buffer,1,0);
    	if (n>0) {
		len=buffer[0];
		bzero(buffer,buf_size);
    	}
        n = recvall(socket_serv,buffer,len);
        if (n < 0) {
            perror("ERROR reading from socket");
            exit(1);
        }
        printf("Server: %s\n",buffer);
    }
    puts("You are disconnect to server");
    close(socket_serv);
}
