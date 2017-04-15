#include <stdio.h>

int main()
{
        unsigned long fib[200],n;
        long p;
        scanf ("%lu", &n);
        unsigned char b[500],i,c;
        if (n == 0)
		printf("%d ", 0);
	if (n == 1)
		printf("%d ", 1);
	fib[0] = 1;
	fib[1] = 1;
        b[0] = 2;
        b[1] = 2;
	i = c = p = 0;      
	for (i = 2; i <= n; i++) {
		fib[i]= 0;
		fib[i] = fib[i-1] + fib[i-2];
		c = i;
		if (fib[i]>n)
			break;
	}
        for (i = 0; i < c; i++)
            b[i]=0;
	for (i = c; i > 0; i--) {
		b[i] = 0;
                p=n-fib[i];
		if (p>=0) {
			b[i]=1;
			n-=fib[i];
		}
		if (n==0)
			break;
	}	
	if (b[c] == 0) {
	        for (i = c-1; i > 0; i--) {
		        printf("%d",b[i]);
	        }
	}
	if (b[c] == 1) {
	        for (i = c; i > 0; i--) {
		        printf("%d",b[i]);
	        }
	}
	return 0;	
}
