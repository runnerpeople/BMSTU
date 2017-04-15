#include <stdio.h>
#include <stdlib.h>

typedef struct a a;
typedef struct q q;

struct a {
        int v1;
        int v2;
        int sum;
};

struct q {
        int count;
        int cap;
        struct a *heap;
};

void swap (int a, int b, q* e) {
        int v1,sum1,v2;
        v1=e->heap[a].v1;
        v2=e->heap[a].v2;
        sum1=e->heap[a].sum;
        e->heap[a].v1=e->heap[b].v1;
        e->heap[a].v2=e->heap[b].v2;
        e->heap[a].sum=e->heap[b].sum;
        e->heap[b].v1=v1;
        e->heap[b].v2=v2;
        e->heap[b].sum=sum1;
}

void heapify (int i, int n, q* y) {
        int l,r,j;
        while (1) {
                l=2*i+1;
                r=l+1;
                j=i;
                if ((l < n) && (y->heap[i].sum > y->heap[l].sum))       
                        i = l;
              if ((r < n) && (y->heap[i].sum > y->heap[r].sum))
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
        y->count++;
        y->heap[i]=b;
        while ((i > 0) && (y->heap[(i - 1)/2 ].sum > y->heap[i].sum)) {
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
        int n,i,k;
        scanf("%d", &k);
        scanf("%d", &n);
        q w;
        w.cap=n;
        w.count=0;
        w.heap=(a*)malloc((n+1)*sizeof(a));
        a* task=(a*)malloc((n+1)*sizeof(a));
        a help;
        for (i=0;i<n;++i) {
                scanf("%d", &task[i].v1);
                scanf("%d", &task[i].v2);
        }
        for (i=0;i<k;++i) {
                task[i].sum = task[i].v1 + task[i].v2;
                insert(&w, task[i]);   
        }
        i=k;
        int max;
        while (w.count) {
                help=extractmin(&w);  
                if (i<n) {                        
                        if (task[i].v1>help.sum)
                                max=task[i].v1;
                        else max=help.sum;
                        task[i].sum=max+task[i].v2;
                        insert(&w, task[i]);
                        i++;                  
                }
        }
        printf("%d", help.sum);
        free(task);
        free(w.heap);
        return 0;
}
