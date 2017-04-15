#include <stdio.h>

long power2 (int n, int y) {
        int i;
        unsigned long s;
	s=1;
    	for (i=y;i!=0;i--) 
        	s*=n;
    	if (y==0) return 1;
    	else return s;
}

int power1 (long i) {
    	return (!(i&(i-1)));
}

long power3 (int n, long array[]) {
    	long r,s,y;
    	long i,j,p;
    	s=0;
    	r=power2(2,n);
    	y=0;
    	char b[n];
    	for (j=1;j<r;j++) {
        	p=j;
        	for (i = 0; i<n; i++) {
                	b[i]= 0;
                	b[i]= p % 2;
                	p /= 2;
        	}
        	for (i=0;i<n;i++) {
            		if (b[i]==1) {
                		s+=array[i];
        		}
        	}
        	if (power1 (s)==1 && s>0) y+=1;
        	s=0;
    	}
    	return y;
}

long *array;

int main() 
{ 
        int i,n;
        long u,f;
        scanf("%d", &n); 
        array = (int*)malloc(n*sizeof(long)); 
        for (i = 0; i < n; i++) scanf("%ld", array+i); 
        u = power3(n,array);
        printf("%ld", u); 
        free(array); 
        return 0; 
} 
