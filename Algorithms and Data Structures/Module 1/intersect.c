#include <stdio.h>

int main()
{
        unsigned int  z;
	unsigned int x,y;
	int a,b,i,q;
	int f,n;
	int ff,nn;
	f = ff = 0;
	y = 0;
	scanf ("%d\n", &a);
	for (i = 0; i < a; i++) {
    	        scanf("%d",&n);
		f= f | (1 << n);
    		y= f | y;
    	}
    	i = 0;
   	x = 0;
    	scanf ("%d\n", &b);
    	for (i = 0; i < b; i++) {
    	        scanf ("%d",&nn);
		ff= ff | (1 << nn);
    		x= ff | x;
   	}
   	z = x & y;
    	q = 0;
    	while (z > 0) {
    	        if (z & 1) 
    		printf ("%d ", q);
    	        q += 1;
    	        z = z >> 1;
	}		
	return 0;		
}
