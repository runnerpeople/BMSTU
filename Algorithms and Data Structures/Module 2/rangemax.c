#include <stdio.h>
#include <stdlib.h>

void build (int *array,int a,int b,int *t,int help) {
        int center;
        if (a==b)
                t[help]=array[b];
        else {
                center=(a+b)/2;
                build(array,a,center,t,help*2);
                build(array,center+1,b,t,help*2+1);
                t[help]=max(t[help*2],t[help*2+1]);
        }
}

void update (int *t,int a,int b,int i,int new,int help) {
        int center;
        if (a==b)
                t[help]=new;
        else {
                center=(a+b)/2;
                if (i<=center)
                        update(t,a,center,i,new,help*2);
                else update(t,center+1,b,i,new,help*2+1);
                t[help]=max(t[help*2],t[help*2+1]);
        }       
}

int max1 (int *t, int a, int b,int help, int l, int r) {
        int center;
        if (l == a && r == b) {
                return t[help];
        }
        else {
		center = (a + b) / 2;
        if (r<=center)
                max1(t,a,center,help*2,l,r);
        else {
                if (l>center)
                        max1(t,center+1,b,help*2+1,l,r);    
                else return max((max1(t,a,center,help*2,l,min(r,center))),(max1(t,center+1,b,help*2+1,max(l,center+1), r)));
        }
	}
}

int max (int a,int b) {
        return (a > b) ? a : b;
}

int min (int a,int b) {
        return (a < b) ? a : b;
}


int log2help(int x) {
        int i;
        i=0;
        x=x>>1;
        for (i=0;x!=0;) {
                x=x>>1;
                i+=1;
        }
        return i;
}

int main(int argc, char** argv) {
        int n,i,y,q,p;
        char str[4];
        int a,b;
        scanf("%d", &n);
        int *array=(int*)malloc(n*sizeof(int));
        for (i=0;i<n;i++) {
                scanf ("%d", &array[i]);
        }
        int *t=(int*)malloc(4*n*sizeof(int));
        build(array,0,n-1,t,1); 
        scanf("%d", &y);
        q=0;
	int *r;
        r=(int*)malloc((y+1)*sizeof(int));
        for (i=0;i<y;i++) {        
                scanf("%s %d %d", str, &a, &b);
                if (strcmp(str,"MAX")==0) {
                        *(r+q)=max1(t,0,n-1,1,a,b);
                        q+=1;
                }            
                else update(t,0,n-1,a,b,1);
        }
        for (i=0;i<q;i++) {
                printf("%d\n", r[i]);
		
        }
	free(array);
	free(t);       
	free(r);
        return 0;
}
