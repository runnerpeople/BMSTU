#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct Elem Elem;

struct Elem { 
        struct Elem *next; 
        char *word; 
}; 

Elem* createlist(char* str) {
        Elem* tmp = calloc(1,sizeof(Elem));
        tmp->word=calloc(100,sizeof(char));
        strcpy(tmp->word,str);
        tmp->next=NULL;
        return tmp;
}

void insertafter(Elem* list, char* add) {
        Elem* z;
        z=createlist(add);
        Elem *y;
        while (list!=NULL) {
                y=list;
                list=list->next;
        }
        y->next=z;
}

int compare(Elem *a, Elem *b) {
        if (strlen(a->word)>strlen(b->word))
                return 1;
        else return 0;
}

void bsort(Elem *start) {
        Elem* p_list; 
        Elem* pp_list;
        for (p_list = start->next; p_list != NULL; p_list = p_list->next) 
                for (pp_list = start->next; pp_list->next != NULL; pp_list = pp_list->next)
                        if (compare(pp_list,pp_list->next)==1) {
                                char* tmp;
                                tmp = pp_list->word; 
                                pp_list->word = pp_list->next->word;
                                pp_list->next->word = tmp;
                        }
}

int main() {
        long int i,j,k,n;
        char *str=calloc(10000,sizeof(char));
        char *buf=calloc(1000,sizeof(char));
        Elem* list=createlist("");
        Elem *t=createlist("");
        Elem *r, *p;
        gets(str);
        for (i=0,j=0;str[i]!='\0';i++) {
                if (str[i]!=' ') {
                        buf[j]=str[i];
                        j++;
                }
                if (str[i]==' ' && j>0) {
                        insertafter(list,buf);
                        for (k=0;k<j;k++) {
                                buf[k]=0;  
                        }
                        j=0;
               
                } 
        }
        if (j!=0) {
                insertafter(list,buf);
                for (k=0;k<j;k++) {
                        buf[k]=0;  
                }
                j=0;                
        }
        free(str);
        free(buf);
        bsort(list);
	p=list->next;
        while (p!=NULL) {
		r=p;
		printf("%s ", p->word);
		free(p->word);
		p=p->next;
		free(r);
	} 
	free(t->word);
        free(t);
        free(list->word);
        free(list);
        return 0;
}
