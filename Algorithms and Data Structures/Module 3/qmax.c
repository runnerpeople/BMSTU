#include <stdio.h>
#include <stdlib.h>

typedef struct q b;

struct q {
                int *data;
                int *max;
                int cap;
                int top1;
                int top2;
};

int stackempty1(b* a) {
        if (a->top1 == 0)
                return 1;
        else return 0;
}

int stackempty2(b* a) {
        if (a->top2 == (a->cap-1))
                return 1;
        else return 0;
}

void push1 (b* a, int x) {
        a->data[a->top1]=x;
        if (a->top1 == 0)
                a->max[a->top1]=x;   
        else 
                if (a->max[a->top1-1]<x)
                        a->max[a->top1]=x;
                else a->max[a->top1]=a->max[a->top1-1];
        a->top1++;
}

void push2 (b* a, int x) {
        a->data[a->top2]=x;
        if (a->top2 == (a->cap-1))
                a->max[a->top2]=x;
        else 
                if (a->max[a->top2+1]>x)
                        a->max[a->top2]=a->max[a->top2+1];
                else a->max[a->top2]=x;
        a->top2--;
}

int pop1 (b* a) {
        int x;
        a->top1--;
        x=a->data[a->top1];
        return x;
}

int pop2 (b* a) {
        int x;
        a->top2++;
        x=a->data[a->top2];
        return x;
}

int queueempty(b* a) {
        if ((stackempty1(a)==1) && (stackempty2(a)== 1))
                return 1;
        else return 0;
}

void enqueue(b* a,int x) {
        push1(a, x);
}

int dequeue(b* a) {
        int x;
        if (stackempty2(a)== 1) {
                while (stackempty1(a)== 0) {
                        push2(a,pop1(a));
                }
        }
        x=pop2(a);
        return x;
}

int max(b a) {
        int x;
        if ((a.top1 != 0) && ((a.top2 == a.cap - 1) || ((a.top2 != a.cap - 1) && (a.max[a.top1 - 1]> a.max[a.top2 + 1]))))
                return a.max[a.top1-1];
        else return a.max[a.top2+1];
}
 
    
int main(int argc, char** argv) {
        int i,n,j;
        char str[5];
        int x;
        scanf("%d", &n);
        struct q a;
        a.data=(int*)malloc(100000*sizeof(int));
        a.max=(int*)malloc(100000*sizeof(int));
        a.cap=100000;
        a.top1=0;
        a.top2=99999;
        for (i=0;i<n;i++) {
                scanf("%s", str);
                if (strcmp(str,"ENQ")==0) {
                        scanf("%d", &x);       
                        enqueue(&a,x);
                }
                if (strcmp(str,"DEQ")==0) {
                        x=dequeue(&a);
                        printf("%d\n", x);                      
                    }
                if (strcmp(str,"EMPTY")==0) {
                        j=queueempty(&a);
                        if (j==1)
                                printf("true\n");
                        else printf("false\n");
                }
                if (strcmp(str,"MAX")==0) { 
                        j=max(a);
                        printf("%d\n", j);
                }
            }
        free(a.data);
        free(a.max);
        return 0;
}
