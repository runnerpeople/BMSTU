#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>

int main(int argc, char** argv) {
        int i,j,k,c;
	i=j=k=c=0;
	if (argv[3] == NULL) {
		printf("Usage: frame <height> <width> <text>");
		return 0;		
	} 
	k = (atoi(argv[2]) - strlen(argv[3]))/2;
	if (k <= 0 || atoi(argv[1]) <= 2) {
		printf("Error");
		return 0;		
	}
	c = ((atoi(argv[1])+1)/2);
    	for (j=1;j<=atoi(argv[1]);j++) {
		if (j==1 || j==atoi(argv[1]))
    			for (i=0;i<atoi(argv[2]);i++)
        			printf("*");
    		else { 
			if (j == c) {
				printf("*");
				for (i=1;i<=k-1;i++)
					printf(" ");
				printf("%s", argv[3]);
				for (i=k+strlen(argv[3])+1;i<=atoi(argv[2]);i++)
					if (i==atoi(argv[2]))
        					printf("*");
					else printf(" ");
			}	
                	else {		
				for (i=1;i<=atoi(argv[2]);i++) {
					if (i==1 || i==atoi(argv[2]))
        					printf("*");
					else printf(" ");
                    		}
                	}
        	}
    		printf("\n");
	}
    	return 0;
}
