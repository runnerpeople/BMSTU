#include <stdio.h>
 
int main()
{
        long n,k,j,i,sum,sum1;
        scanf ("%ld\n", &n);
        i = n;
         int a[i];
        for (i = 0;i < n;i++)
                scanf ("%d\n", &a[i]);
        scanf("%ld",&k);
        sum = i = sum1 = j = 0;
        for (i = 0; i < k; i++) 
                sum += a[i];
        sum1 = sum; 
        if (k == n)
	        printf ("%ld", sum1);
        else { 
                for (j = k, i = 0; j < n;j++,i++) {
	                sum1 = sum1 - a[i] + a[j];
		        if (sum1 > sum)
		                sum = sum1;
	}
	printf ("%ld",sum);
	}
    return 0;
}
