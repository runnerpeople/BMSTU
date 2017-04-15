#include <stdio.h>
#include <stdlib.h>

void kadane(float *array,int nel) {
        float maxsum = array[0],sum=0;
        int r= 0, l=0, start = nel-1, i=nel-1;
        while (i>=0) {
	        sum += array[i];
	        if (sum >= maxsum) {
                        maxsum=sum;
                        l=start;
                        r=i;                
                }
                i-=1;
	        if (sum < 0) {
		        sum = 0;
		        start = i;
	        }
        }
	if (l==nel && r==nel && nel>1) {
		r=nel-1;
		l=nel-1;
	}
        printf("%d %d", r, l);
}

int main(int argc, char** argv) {
        int x,i;
        float a,b;
        int y;
        scanf ("%d", &x); 
        float *array=(float*)malloc((x+1)*sizeof(float));
        for (i=0;i<x;i++) {
                scanf("%f/%f", &a, &b);
                float u;
                u=a/b;
                array[i]=log(u);
        }
        kadane(array,x);
        free(array);
        return 0;
}
