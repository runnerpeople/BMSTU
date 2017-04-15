#include <stdio.h>
#include <math.h>

int main()
{
        long long m,i,j,f;
        scanf("%lld", &m);
        if (m < 0)
                m = -m;
        i = sqrt(m);
        char a[i];
	a[0] = 0;
	a[1] = 0;
	for (i = 2; i < sqrt(m); i++)
		a[i] = 1;
	for (i = 2; i * i <= sqrt(m); i++) {
                if (a[i] != 0)  {
                        for (j = i * i; j <= sqrt(m); j+=i) {
                                a[j] = 0;
                        }
                }
        }
        f = 0;
        for (i = 0; i < sqrt(m); i++) {
    	        if ((a[i] != 0) && (m % i == 0)) {
    	                m /= i;
    	                i = 0;
    	                }   	                
	}
        if (f == 0)
                f = m;
        if (m == 4)
                f = 2;
    	printf("%lld\n", f);
	return 0;
}
