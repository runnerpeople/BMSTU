#include <stdio.h>
#include <stdlib.h>
#include<string.h>


void dsort(char *src, int nel)
{
        int count[26];
        char k;
        int i,j=0;
        for(i=0; i<26; i++) {
                count[i]=0;
        } 
        for(i=0; i<nel; i++) {
                k=src[i]-97;
                count[k]+=1;
        } 
        i=1;
        while (i<26) {
                count[i] = count[i] + count[i - 1];
                i += 1;
        }
        j=nel-1;
        char dest[1000001];
        while (j>=0) {
                k=src[j]-97;
                i=count[k]-1;
                count[k]=i;
                dest[i] = src[j];
                j-=1;
        }
        for (i=0;i<nel;i++) {
		src[i]=dest[i];
	}
}   

int main() {
        int r,i;
        char src[1000001];	
        gets(src); 
        r=0;
        for (i=0;src[i]!='\0';i++) 
                r+=1;
        dsort (src,r);
        printf("%s", src);
        return 0;
}
