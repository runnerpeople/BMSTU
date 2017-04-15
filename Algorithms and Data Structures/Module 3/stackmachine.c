#include <stdio.h>
#include <stdlib.h>

int main() {
        int i,n,j;
        char str[5];
        long help;
        help=0;
        long *array=(long*)malloc(100000*sizeof(long));
        scanf("%d", &n);
        j=0;
        for (i=0;i<n;i++) {
                scanf("%s", str);
                if (strcmp(str,"CONST")==0) {
                        scanf("%ld", &help);
                        *(array+j)=help;
                        j++;
                }
                if (strcmp(str,"ADD")==0) {
                        j--;
                        *(array+j-1)=*(array+j)+*(array+j-1);
                }
                if (strcmp(str,"SUB")==0) {
                        j--;
                        *(array+j-1)=*(array+j)-*(array+j-1);
                }
                if (strcmp(str,"MUL")==0) {
                        j--;
                        *(array+j-1)=*(array+j)*(*(array+j-1));
                }
                if (strcmp(str,"DIV")==0) {
                        j--;
                        *(array+j-1)=*(array+j)/(*(array+j-1));
                }
                if (strcmp(str,"MAX")==0) {
                        j--;
                        if (*(array+j)>(*(array+j-1)))
                                *(array+j-1)=*(array+j);
                        else *(array+j-1)=*(array+j-1);
                }
                if (strcmp(str,"MIN")==0) {
                        j--;
                        if (*(array+j)>(*(array+j-1)))
                                *(array+j-1)=*(array+j-1);
                        else *(array+j-1)=*(array+j);
                }
                if (strcmp(str,"NEG")==0) {
                        j--;
                        help=*(array+j);
		        help = help - 2*help;
		        *(array+j)=help;
                        j++;
                }
                if (strcmp(str,"DUP")==0) {
                        *(array+j)=*(array+j-1);
                        j++;
                }
                if (strcmp(str,"SWAP")==0) {
		        j--;
                        help=*(array+j-1);
                        *(array+j-1)=*(array+j);
                        *(array+j)=help;
                        j++;
                }
        }
        printf("%ld", *(array+j-1));
        free(array);
        return 0;
}
