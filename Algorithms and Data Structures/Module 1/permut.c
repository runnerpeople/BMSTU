#include <stdio.h>

int main()
{
        int i,j;
	int a[8],b[8];
	i = j = 0;
	for (i = 0; i < 8; i++) {
		scanf("%d", &a[i]);
	}
	for (i = 0; i < 8; i++) {
		scanf("%d", &b[i]);
	}
	for (i = 0; i < 8; i++) {
		for (j = 0; j < 8; j++) {
			if (a[i] == b[j]) {
				b[j]=0;
				break;		
			}
	}
	}
        for(i=0;i<8;++i)
                if (b[i]!=0)
                {
                        printf("no");
                        return 0;
                }
                
		
	printf ("yes");
	return 0;
}
