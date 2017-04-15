#include <stdio.h>
#include <stdlib.h>

void swap(const void *a, const void *b, int width) {
        int i;
        char *va=(char*)a; 
        char *vb=(char*)b;
        char *t =malloc(width+1);
        i=0;
        while (i<width) {
                t[i]=0;
                t[i]=va[i];
                va[i]=vb[i];
                vb[i]=t[i];
                i+=1;
        }
        free(t);
}

int compare (const void *a, const void *b) {
        char *p;
        int i,la,lb,q,w;
        la=lb=0;
        i=0;
        q=strlen(a);
        w=strlen(b);
        p=a;
        while (i<q) {
                if (*(p+i)=='a')
                        la+=1;
                i+=1;
        }
        p=b;
        i=0;
        while (i<w) {
                if (*(p+i)=='a')
                        lb+=1;
                i+=1;
        }
        if (la == lb)
                return 0;
        return (la > lb) ? 1 : -1;
}

void heapify (void *base,size_t width,int (*compare)(const void *a, const void *b),size_t i,size_t nel) {
        int z=0;
        while (z==0) {        
                int l,r,j;    
                l = 2*i + 1;
                r = l + 1;
                j = i;
                if ((l < nel) && (compare((char*)base + width*i,(char*)base + width*l)==-1))
                        i=l;
                if ((r < nel) && (compare((char*)base + width*i,(char*)base + width*r)==-1))
                        i=r;
                if (i==j)
                        break;
                swap((char*)base + i * width, (char*)base + j * width, width);
        }
}

void  buildheap (void *base,size_t width,int (*compare)(const void *a, const void *b),size_t nel) {
        int i;
        i=nel/2-1;
        while (i >= 0) {
                heapify(base,width,compare,i,nel);
                i -= 1; 
        }
}

void hsort(void *base, size_t nel, size_t width, int (*compare)(const void *a, const void *b)) {
        int i,u;
        buildheap (base,width,compare,nel);
        i = nel-1;
        while (i > 0) {
                swap((char*)base, (char*)base + i * width, width);
                heapify(base,width,compare,0,i);
                i -= 1;
        }
}

int main(int argc, char** argv) {
        int i,x;
        scanf("%d", &x);
        char s[x][1000];
        for (i=0;i<x;i++) {
            scanf("%s", s[i]);
        }
        hsort(s,x,1000,compare);
        for (i=0;i<x;i++) {
                printf("%s\n", s[i]);
        }
        return 0;
}


