

#include <stdio.h>
#include <stdlib.h>

typedef union Int32 Int32;

union Int32 { 
        int x; 
        unsigned char bytes[4]; 
};

Int32* dsort_invert(Int32 *src, int nel,int b) {
        char count[256];
        unsigned char k,q;
        int i,j=0;
        for(i=0; i<256; i++) {
                count[i]=0;
        } 
        for(i=0; i<nel; i++) {
                count[128 & src[i].bytes[b]]+=1;
        } 
        i=254;
        while (i>=0) {
                count[i] = count[i] + count[i + 1];
                i -= 1;
        }
        Int32* dest=(Int32*)malloc((nel+1)*sizeof(Int32));
        j=nel-1;
        for (i=0;i<nel;i++) {
                dest[i]=src[i];
        }
        while (j>=0) {
                k=src[j].bytes[b];
                i=count[128 & k]-1;
                count[128 & k]=i;
                dest[i].x = src[j].x;
                j-=1;
        }
        free(src);
        return dest;
}

Int32* dsort(Int32 *src, int nel,int b) {
        char count[256];
        unsigned char k;
        int i,j=0;
        for(i=0; i<256; i++) {
                count[i]=0;
        } 
        for(i=0; i<nel; i++) {
                count[src[i].bytes[b]]+=1;
        } 
        i=0;
        while (i<256) {
                count[i] = count[i] + count[i - 1];
                i += 1;
        }
        Int32* dest=(Int32*)malloc((nel+1)*sizeof(Int32));
        j=nel-1;
        for (i=0;i<nel;i++) {
                dest[i]=src[i];
        }
        while (j>=0) {
                k=src[j].bytes[b];
                i=count[k]-1;
                count[k]=i;          
                dest[i].x = src[j].x;
                j-=1;
        }
        free(src);
        return dest;
}

int main(int argc, char** argv) {
        int i,n;
        scanf("%d", &n);
        Int32* a=(Int32*)malloc((n+1)*sizeof(Int32));
        for (i=0;i<n;i++) {
                scanf("%d", &a[i].x);
        }
        for (i=0;i<=3;i++)
                a=dsort(a,n,i);
        a=dsort_invert(a,n,3);
        for (i=0;i<n;i++) {
                printf("%d ", a[i].x);
        }
        free(a);
        return 0;
}
