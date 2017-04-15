#include <stdio.h>
#include <stdlib.h>

typedef struct stack st;
typedef struct Task t;

void swap(int* a, int* b)
{
        int buffer;
        buffer = *a;
        *a = *b;
        *b = buffer;
}

struct Task { 
        int low, high; 
};

struct stack {
        struct Task *data;
        int cap;
        int top;
};

void initstack (st* s, int n) {
        s->cap=n;
        s->top=0;
        s->data=malloc((sizeof(struct Task))*(n+1));
}

int stackempty(st* s) {
        if (s->top==0)  
                return 1;
        else return 0;       
}

void push(st* s,int left,int right) {
        s->data[s->top].low=left;
        s->data[s->top].high=right;
        s->top++;
}

t pop(st* s) {
        t x;
        s->top--;
        x=s->data[s->top]; 
        return x;
}

void quicksort(int *array,int n) {
        int i,left,right,middle,help;
        st s;
        initstack(&s,5000);
        push(&s,0,n-1);
        while (!(stackempty(&s)))  {
                t temp=pop(&s);
                while (temp.low<temp.high)  {
                        middle=(temp.low+temp.high)/2;
                        left=temp.low;
                        right=temp.high;
                        help=array[middle];
                        do  {
                                while (array[left]<help) 
                                        left++;
                                while (array[right]>help)
                                        right--;
                                if (left<=right) {
                                        swap(&array[left], &array[right]);
                                        left++;
                                        right--;
                                }
                                
                        }  while (left<=right);
                        if (left<middle) {
                                if (left<temp.high) {
                                        push(&s,left,temp.high);
                                }          
                                temp.high=right;
                        }
                        else {
                                if (right>temp.low)  {
                                        push(&s,temp.low, right);
                                }
                                temp.low=left;    
                        }   
                } 
        } 
        free(s.data);
}

int main() {
        int i, n;
        scanf("%d", &n);
        int *arr=malloc((n+1)*sizeof(int));
        for (i = 0; i < n; i++)
                scanf("%d", &arr[i]);
        quicksort(arr,n);
        for (i = 0; i < n; i++)
                printf("%d ", arr[i]);
        printf("\n");
        free(arr);
        return 0;
}
