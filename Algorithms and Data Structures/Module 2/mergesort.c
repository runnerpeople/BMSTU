#include <stdio.h>
#include <stdlib.h>

int *array;

int compare(unsigned long i, unsigned long j) 
{  
        if (abs(array[i]) == abs(array[j])) return 0; 
        return abs(array[i]) < abs(array[j]) ? -1 : 1; 
}

void swap(unsigned long i, unsigned long j) 
{ 
        int t = array[i]; 
        array[i] = array[j]; 
        array[j] = t; 
}
void insertsort(long i,unsigned long nel,int (*compare)(unsigned long i, unsigned long j)) {
        int n,loc,d;
        d=i;
        while (d<nel) {
            loc=d;
                while (loc>=i && compare(loc,loc+1)==1) {
                        swap(loc,loc+1);
                        loc-=1;
                }
                d+=1;      
        }       
}
void merge(unsigned long i,unsigned long c,unsigned long nel,int (*compare)(unsigned long i, unsigned long j)) {
        int help[nel-i+1],a,b,f,d,e;
        for (d=0;d<(nel-i+1); d++) {
                help[d]=0;
        }
        a=i;
        b=c+1;
        f=0;
        while (f<(nel-i+1)) {
                if ((b<=nel) && (a==b || (compare(a,b)>0) || (!(a<c+1)))) {
                        help[f]=array[b];
                        b+=1;
                }
                else {
                        help[f]=array[a];
                        a+=1;
                }
                f+=1;
        }
        e=0;
        for (d=i;d<nel+1;d++) {               
                array[d]=help[e];
                e+=1;
        } 
}

void mergesort(unsigned long i,unsigned long nel,int (*compare)(unsigned long i, unsigned long j)) {
        int c,r;
        if (i<nel) {
                c=(i+nel)/2;
                if (c>4) {
                        mergesort(i,c,compare);
                        mergesort(c+1,nel,compare);                       
                        
                }
                else {
                        insertsort(i,c,compare);
                        insertsort(c+1,nel,compare);
                }
                merge(i,c,nel,compare);
        }
}

int main(int argc, char **argv) 
{ 
        int i, n; 
        scanf("%d", &n);  
        array = (int*)malloc(n * sizeof(int)); 
        for (i = 0; i < n; i++) scanf("%d", array+i);  
        mergesort(0,n-1,compare); 
        for (i = 0; i < n; i++) printf("%d ", array[i]);  
        free(array); 
        return 0; 
} 
