#include <stdio.h>
#include <stdlib.h>

int gcd(int a, int b) {
   return b? gcd(b, a % b) : abs(a);
}

long power2 (int n, int y) {
        int i;
        unsigned long s;
        s=1;
            for (i=y;i!=0;i--) 
                s*=n;
    	if (y==0) return 1;
    	else return s;
}

void log2help(int x, int *lg) {
        int i,j;
        i=1;
        j=0;
        while(i<x) {
                while (j<power2(2,i)) {
                        lg[j]=i-1;
                        j+=1;
                }
                i+=1;
        }
}

int min (int a,int b) {
        return (a < b) ? a : b;
}

void sparseteble_build(int *array, int nel, int *lg, int **st) {
        int m,i,j;
        m = lg[nel] + 1;
        i=0;
        while (i < nel) {
                st[0][i] = array[i];
                i += 1;       
        }
        j = 1;
        while (j < m) {
                i = 0;
                while (i <= nel - power2(2,j)) {
                        st[j][i] =gcd (st[j-1][i],st[j-1][i+power2(2,j-1)]);
                        i +=1;
                }
                j +=1;
        }
}

int sparseteble_query(int **st,int l,int r,int *lg) {
        int j,v;
        j = lg[r - l + 1];
        v=gcd (st[j][l] ,st[j][r-power2(2,j)+1]);
        return v;
}

int main(int argc, char** argv) {
        int n,i,y,a,b;
        scanf("%d", &n);
        int *array=(int*)malloc(n*sizeof(int));
        for (i=0;i<n;i++) {
                scanf ("%d", &array[i]);
        }
        int *lg=(int*)malloc(2000000*sizeof(int));
        log2help(20,lg);
        int **s;
        s = (int**)malloc(100000*sizeof(int));
        for (i=0;i<=lg[n];i++) {
                s[i] = (int*)malloc(300001*sizeof(int));
        }
        sparseteble_build(array,n,lg,s);
        scanf("%d", &y);
        int *r;
        r=(int*)malloc((y+1)*sizeof(int));
        for (i=0;i<y;i++) {        
                scanf("%d %d", &a, &b);
                *(r+i)=sparseteble_query(s,a,b,lg);
        }
        for (i=0;i<y;i++) {
              printf("%d\n", r[i]);
        }
        free(array);
        free(lg);       
        free(r);
        free(s);
        return 0;
}
