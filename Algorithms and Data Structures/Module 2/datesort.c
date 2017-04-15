#include <stdio.h>
#include <stdlib.h>

typedef struct Date Date;

struct Date { 
        int Day; 
        int Month;
        int Year; 
};

    
Date* dsort(Date *src, char d, int nel)
{
        int help,k,temp;
        if (d=='d')
                help = 32;
        if (d=='m') 
                help = 13;
        if (d=='y') 
                help =61;
        int i,j=0;
        int count[help];
        for(i=0; i<help; i++) {
                count[i]=0;
        } 
        for(i=0; i<nel; i++) {
                if (d=='d')
                        k=src[i].Day;
                if (d=='m')
                        k=src[i].Month;    
                if (d=='y')
                        k=src[i].Year-1970;
                count[k]+=1;
        } 
        i=1;
        while (i<help) {
                count[i] = count[i] + count[i - 1];
                i += 1;
        }
        j=nel-1;
        Date* dest=(Date*)malloc((nel+1)*sizeof(Date));
        for (i=0;i<nel;i++) {
                dest[i]=src[i];
        }
        while (j>=0) {
                if (d=='d')
                        k=src[j].Day;
                if (d=='m')
                        k=src[j].Month;    
                if (d=='y')
                        k=src[j].Year-1970;
                i=count[k]-1;
                count[k]=i;
                dest[i].Day=src[j].Day;
                dest[i].Month=src[j].Month;
                dest[i].Year=src[j].Year;
                j-=1;
        }
        free(src);
        return dest;        
}


    
int main(int argc, char** argv) {
        int i,n;
        scanf("%d", &n);
        Date* a=(Date*)malloc((n+1)*sizeof(Date));
        for (i=0;i<n;i++) {
                scanf("%d %d %d", &a[i].Year,&a[i].Month, &a[i].Day);
        }
        a=dsort(a,'d',n);
        a=dsort(a,'m',n);
        a=dsort(a,'y',n);
        for (i=0;i<n;i++) {
                printf("%d %d %d", a[i].Year,a[i].Month, a[i].Day);
                printf("\n");
        }
        free(a);
        return 0;
}
