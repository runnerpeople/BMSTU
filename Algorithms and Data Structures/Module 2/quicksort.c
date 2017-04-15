#include <stdio.h>
#include <stdlib.h>

void swap(long i, long j,int *array) 
{ 
        int t = array[i]; 
        array[i] = array[j]; 
        array[j] = t; 
} 

int compare(long i, long j,int *array) 
{  
        if (array[i] == array[j]) return 0; 
        return array[i] < array[j] ? -1 : 1; 
}

void selectsort (int low,int high,int *array) {
        int k,i;
        int j=high;
        while (j>low) {
                k=j;
                i=j-1;
                while (i>=0) {
                        if (compare(k,i,array)==-1)
                                k=i;
                        i-=1;
                }
                swap(j,k,array);
                j-=1;
        }
}

int partition (int low , int high , int *array) {
        int i,j;
        i = low;
        j = low;
        while (j < high) {
                if (compare(j,high,array)==-1) {
                        swap(j,i,array);
                        i +=1;
                }
                j +=1;
        }
        swap(i,high,array);
        return i;
}

void quicksort (int low, int high,int m, int *array) {
        int q;
        while ((high-low)>0) {
                if (high-low<=m) {
                        selectsort(low,high,array);
                        break;
                }
                q=partition(low,high,array);
                if ((q-low)<(high-q)) {
                        quicksort (low,q-1,m,array);
                        low=q+1;
                }
                else {
                        quicksort (q+1,high,m,array);
                        high=q-1;
                }
        }
}


int main() {
        int i,n,m; 
        scanf("%d %d", &n, &m);  
        int array[n];
        for (i = 0; i < n; i++) 
                scanf("%d", &array[i]);  
        quicksort(0,n-1,m,array); 
        for (i = 0; i < n; i++) 
                printf("%d ", array[i]);  
        return 0; 
}
