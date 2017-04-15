#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

int wcount(char *s) {
        long n,i,r;
        n = i = r = 0;
        n=strlen(s);    
        int count = 0;
        for (i=0;i<n-1;i++) {
                r = isgraph ((*(s + i)));
                        if ((*(s + i + 1)) == ' ' && r != 0)
                                count+=1;
        }
        r = isgraph ((*(s + i)));
                if (r != 0)
                        count+=1;
        return count;
}

int main(int argc, char** argv) {
        long r,i,j;
        r = i = j = 0;
        char s[100000];
        gets(s);
        if (strlen(s) == 0) {
                printf ("0");
                return 0;
        }  
        r = wcount (s);
        printf("%ld", r);
        return 0;
}
