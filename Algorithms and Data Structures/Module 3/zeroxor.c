#include<stdio.h>
#include<stdlib.h>
#include<math.h>

typedef struct Elem Elem;

struct Elem {
        int val;
        int k;
        Elem *next;
};

Elem* ListSearch(Elem *list, int key) {
        Elem *x = list;
        if (x==NULL)
                return 0;
        while (x !=NULL && x->k != key)
                x = x->next;
        return x;
} 

void InsertBeforeHead(struct Elem *list, int key) {
        struct Elem *x = calloc(1, sizeof(Elem));
        x->k=key;
        x->val==(key)? 0: 1;
        x->next = list->next;
        list->next = x; 
} 

void freelist(Elem **arr) {
        long i;
        for (i=0;i<100000;i++) {
                Elem *p=arr[i]; 
                for (;p!=NULL;) {
                        Elem *help=p;
                        p=p->next;
                        free(help);
                }
                free(p);
        } 
        free(arr);
}

int main() {
        int n, i, a, count,help;
        Elem **arr = calloc(100000, sizeof(Elem*)), *t;
        scanf("%d", &n);
        for (i=0;i<100000;i++) {
                arr[i]=calloc(1, sizeof(Elem));
                arr[i]->k = 0;
                arr[i]->val = 0;
                arr[i]->next = NULL;  
        }  
        help=0;
        for (i = 0; i < n; i++) {
                scanf("%d", &a);                
                help ^= a;
                t = ListSearch(arr[abs(help) % 100000], help);
                if (t != NULL)
                        t->val++;
                else InsertBeforeHead(arr[abs(help) % 100000], help);
        }
	count=0;
	a=0;
        for (i = 0; i < 100000; i++) {
                t= arr[i];
                while (t!=NULL) {
                        int v = t->val;
                        count += v * (v + 1) / 2;
                        t = t->next;
                }
        }
        printf("%d", count);
        freelist(arr);
        return 0;
}
