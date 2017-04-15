#include <stdio.h>

int main()
{
        int i,j,max,min,h,k;
	int a,b;
	a = b = i = j = min = max = h = k = 0;
        scanf("%d %d", &a, &b);
	int q[a][b], w[a], g[b];
	for (i = 0; i < a; i++) {
		for (j = 0; j < b; j++) {
		scanf ("%d", &q[i][j]);      
                }
        }  
        k = 0;
        for (i = 0; i < a; i++) {
            max = q[i][k];
            for (j = 0; j < b; j++) {
                if (max<q[i][j]) {
                    max=q[i][j];
                    
            }
                w[i]=max;              
            }
        }
        k = 0;
        for (j = 0; j < b; j++) {
            min = q[k][j];
            for (i = 0; i < a; i++) {
                if (min>q[i][j]) {
                    min=q[i][j];                 
            }
                g[j]=min;
            }
        }
        h = 0;
        for (i = 0; i < a; i++) {
            for (j = 0; j < b; j++) {
                if (w[i]==g[j]) {
                    h = 1;
                printf("%d %d ", i, j);
                }
            }
        }
        if (h==0)
            printf("none");           
	return 0;
}
