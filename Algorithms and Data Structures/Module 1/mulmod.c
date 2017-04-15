#include <stdio.h>

int main()
{
        long long a,b,m,d,e,d1;
        long long w,d2 = 0;
        b = d = e = w = 0;
	char f[65];
	int i;
	i = 0;
	a = b = m = 0;
	scanf("%lld %lld %lld", &a, &b, &m);
	for (i = 0; b != 0; i++) {
		f[i]= 0;
		f[i]= b % 2;
		b /= 2;
	}
	d = f[--i] * a;
	for (--i; i != -1; i--) {
		w = 0;
		w = w + (((d % m) * (2 % m))% m);
		d2 = (a * (f[i] % m)) % m;
		e = w + d2;
                d = e;
	}
        e = e % m;
	printf("%lld", e);
	return 0;
}
