#include <stdio.h> 
#include <stdlib.h> 
#include "elem.h"

struct Elem *searchlist(struct Elem *list, int k) {
        struct Elem *elem;
        if (list->tag == LIST && list->value.list != NULL) {
                elem = searchlist (list->value.list, k);
                if (elem != NULL) 
                        return elem;
                else searchlist (list->tail, k);
        }
        if (list -> value.i == k)
                return list;
        else { 
                if (list -> tail != 0)
                        searchlist(list -> tail, k);
        }
}
