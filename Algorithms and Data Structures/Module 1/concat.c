#include <stdio.h>
#include <stdlib.h>
#include <string.h>

char *concat (char **s, int n) {
        long sum,i;
        i=0;
        sum=0;   
        for (i =1; i<=n;i++) {
                sum += strlen(*(s+i));
        }
        char *str1;
        str1=(char*) malloc(sum+1);
        for (i = 0; i <= n; i++) {
	        *(str1+i) = 0;
        }
        for (i = 1; i <= n; i++) {
		strcat(str1, *(s + i));       
        }
        return str1;
}

int main () {
        int n,i;
        n=i=0;
        scanf ("%d", &n);
        char **s;
        s = (char**)malloc(500);
        for (i=0;i<=n;i++) {
                s[i] = (char*)malloc(10000);
                gets(s[i]);
        }
	char *str1;
        str1 = concat(s,n);
        printf("%s", str1);
	for (i = 0; i <= n; i++) {
	        free(*(s+i));
	}
        free(str1);
        free(s);
        return 0;
}
