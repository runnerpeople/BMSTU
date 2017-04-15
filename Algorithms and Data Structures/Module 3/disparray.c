#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct Elem Elem;

struct Elem {
        long key;
        long val;
        struct Elem* next;
};


Elem* ListSearch(Elem *list, long key) {
        Elem *x = list;
        while (x !=NULL && x->key != key)
                x = x->next;
        return x;
} 

Elem* ListSearch1(Elem *list, long key) {
        Elem *x = list;
        while (x !=NULL && x->next!=NULL && x->key != key)
                x = x->next;
        return x;
}

Elem* insertafter(Elem* list, long k,long v,Elem **arr,int m) {
        Elem *y;
        if (list!=NULL && (list->key == k)) {
                list->val = v;
        }
        else {
                if (list!=NULL) {
                        list->next = (Elem*) malloc(sizeof(Elem));
                        list = list->next;
                }
                else {
                        arr[k % m] = (struct Elem*) malloc(sizeof(struct Elem));
                        list = arr[k % m];
                }
                list->key = k;
                list->val = v;
                list->next = NULL;
        }
        return list;
}

void freelist(long m, Elem **arr) {
        long i;
        for (i=0;i<m;i++) {
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

int main(void) {
        long int i, n, m, k, v;
        char cmd[10];
        scanf("%ld%ld", &n, &m);
        struct Elem **arr = calloc(m, sizeof(struct Elem*)), *p;
        for (i = 0; i < m; i++)  {
                arr[i]=0;
        }
        for (i = 0; i < n; i++) {
                scanf("%s", cmd);
                if (strcmp(cmd,"AT")==0) {
                        Elem *help;
                        scanf("%ld", &k);
                        help = arr[k % m];
                        p=ListSearch(help,k);
                        if (p != 0) 
                                printf("%ld\n", p->val);
                        else printf("0\n");
                }
                if (strcmp(cmd,"ASSIGN")==0) {
                        scanf("%ld %ld", &k, &v);
                        if (v!=0) {
                                p = arr[k % m];
                                p=ListSearch1(p,k);
                                p=insertafter(p,k,v,arr,m);
                        }
                        else {
                                p = arr[k % m];
                                if (p!=NULL)
                                        if (p->key == k) {
                                                Elem *help=p;
                                                arr[k % m] = p->next;
                                                free(help);
                                        }  
                                        else {
                                                while (p->next && (p->next->key != k))
                                                        p = p->next;
                                                if (p->next!=NULL) {
                                                        Elem* help = p->next;
                                                        p->next = p->next->next;
                                                        free(help);
                                                }
                                        } 
                        }
                }
        }
        freelist(m,arr);
        return 0;
}
