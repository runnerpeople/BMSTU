#include <stdio.h>
#include <stdlib.h>

typedef struct a a;
typedef struct q q;

struct a {
        int v;
        int index;
};

struct q {
        int count;
        int cap;
        struct a *heap;
};

void swap (int a, int b, q* e) {
        int v1,index1;
        v1=e->heap[a].v;
        index1=e->heap[a].index;
        e->heap[a].v=e->heap[b].v;
        e->heap[a].index=e->heap[b].index;
        e->heap[b].v=v1;
        e->heap[b].index=index1;
}

void heapify (int i, int n, q* y) {
        int l,r,j;
        while (1) {
                l=2*i+1;
                r=l+1;
                j=i;
                if ((l < n) && (y->heap[i].v > y->heap[l].v))       
                        i = l;
                if ((r < n) && (y->heap[i].v > y->heap[r].v))
                        i=r;
                if (i==j)
                        break;
                swap(i,j,y);
        }        
}

void insert(q* y,a b)
{
        int i;   
        i=y->count;
        y->count+=1;
        y->heap[i]=b;
        while ((i > 0) && (y->heap[(i - 1)/2 ].v > y->heap[i].v)) {
                swap((i - 1) / 2, i,y);
                i = (i - 1) / 2;
        }
}

a extractmin(q* y) {
        a x=y->heap[0];
        y->count-=1;
        if (y->count>0) {
                y->heap[0]=y->heap[y->count];
                heapify(0,y->count,y);
        }
        return x;
}

int main() {
        int n,sum,i,j;
        sum=0;
        scanf("%d", &n);
        int *help = malloc(sizeof(int) * (n+1));
        int **help2 = malloc(sizeof(int*) * (n+1));  
        int help3[n];
        q* w=malloc(1*sizeof(q));
        w->cap=n;
        w->count=0;
        w->heap=malloc((n+1)*sizeof(a));    
        for (i=0;i <n;i++) {
                scanf("%d", &help[i]);
                sum+=help[i];
                help2[i]=malloc((help[i]+1)*sizeof(int));  
                help3[i]=0;
        }
        for (i=0;i<n;i++) {
                for(j=0;j<help[i];j++) {
                        scanf("%d", &help2[i][j]);
                }
        }
        for (i=0;i<n;i++) {
                if (help[i]!=0) {
                        struct a e;
                        e.v=help2[i][0];
                        e.index=i;
                        insert(w,e);
                        help3[i]+=1;
                }          
        }
        for (i=0;i<sum;i++) {
                struct a r;
                r=extractmin(w);
                printf("%d ", r.v);
                if (help3[r.index]!=help[r.index]) {
                        struct a x;
                        x.v=help2[r.index][help3[r.index]];
                        x.index=r.index;
                        insert(w,x);
                        help3[r.index]++;
                }       
        }
        free(w->heap);
        for (i=0;i<n;i++)
                free(help2[i]);
        free(help2);
        free(help);
        free(w);
        return 0;
}
