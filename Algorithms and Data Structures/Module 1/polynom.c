#include <stdio.h>
#include <math.h>
int main()
{
        long long p, q;
        p = q = 0;
        int i,n,x;
	i = n = x = 0;
	scanf("%d\n", &n);
	int a[n];
	scanf("%d\n", &x);
	for (i = 0; i < n+1; ++i)
	{
		a[i]=0;
		scanf("%d", &a[i]);
	}
	p = a[0];
	for (i = 0; i < n; ++i)
		p = p * x + a[i+1];
	printf("%lld\n", p);
	for (i = 0; i < n; ++i)
		a[i] = a[i] * (n-i);
	q = a[0];
	for (i = 1; i < n; ++i)
		q = q * x + a[i];
	printf("%lld", q);
	return 0;
}
