#include <stdio.h>
#include <stdlib.h>

typedef struct q q;

struct q
{
        int *data;
        int head;
        int tail;
        int cap;
        int count;
};

struct q initqueue (int n) {
        struct q a;
        a.data=(int*)malloc(4*sizeof(int));
        a.head=0;
        a.tail=0;
        a.cap=n;
        a.count=0;
        return a;
}

int queueempty(q* a) {
        if (a->count == 0)
                return 1;
        else return 0;
}

void enqueue(q* a,int x) {
        int i,help;
	help=a->cap;
        if (a->count == a->cap) {  
		a->head+=help;              
                a->cap *= 2;
                a->data = realloc (a->data, sizeof(int)*(a->cap)); 
		for (i=a->tail;i<help;i++) 
			a->data[help+i]=a->data[i];
			 
	}
        a->data[a->tail]=x;
        a->tail+=1;
        if (a->tail==a->cap)
                a->tail=0;
        a->count+=1;
}

int dequeue(q* a) {
        int x;
        x=a->data[a->head];
        a->head+=1;
        if (a->head==a->cap)
                a->head=0;
        a->count-=1;
        return x;
}

int main() {
        int i,n,j;
        char str[5];
        int x;
        struct q s = initqueue (4);
        scanf("%d", &n);
        for (i=0;i<n;i++) {
                scanf("%s", str);
                if (strcmp(str,"ENQ")==0) {
                        scanf("%d", &x);	
                        enqueue(&s,x);
                }
                if (strcmp(str,"DEQ")==0) {
                        x=dequeue(&s);
                        printf("%d\n", x);                      
                }
                if (strcmp(str,"EMPTY")==0) {
                        j=queueempty(&s);
                        if (j==1)
                                printf("true\n");
                        else printf("false\n");
                }
        }
	free(s.data);
        return 0;
}
