#include <stdio.h>
#include <stdlib.h>

typedef struct Elem e;

struct Elem { 
        struct Elem *prev, *next; 
        int v; 
}; 

e* createlist() {
        e* tmp = malloc(sizeof(e));
        tmp->prev=tmp->next=tmp;
        return tmp;
}

void deletelist(e* list) {
        e* tmp=list->prev;
        e* help=list->next;
        tmp->next=help;
        help->prev=tmp;
        list->next=NULL;
        list->prev=NULL;
}

int list_empty(e* l) {
        return (l->next == l) ? 1 : 0;
} 

void insertafter(e* list, e* add) {
        e* z=list->next;
        list->next=add;
        add->prev=list;
        add->next=z;
        z->prev=add;
}

void insertsort(e* list) {
        e* i;
        e* j;
        i=list->next->next;
        while (i!=list) {   
                j=i->prev;
                while (j!=list && i->v < j->v) {
                        j=j->prev;
                }
                deletelist(i);
                insertafter(j, i);
                i=i->next;
        }
}

int listlength (e* a) {
        int len=0;
        e* x=a;
        for (;x->next!=a;x=x->next) 
                len+=1;
        return len;    
}

int main() {
        long int i, n;
        e *list = createlist(), *y,*p;
        scanf("%ld", &n);
        for (i = 0; i < n; i++) {
                y = createlist();
                scanf("%d", &(y->v));
                insertafter(list, y);
        }
        insertsort(list); 
        y=list->next;
        while (y != list) {
                printf("%d ", y->v);
                p = y->next; 
                free(y);
        	y=p;
        }
        free(list);
        return 0;
}
