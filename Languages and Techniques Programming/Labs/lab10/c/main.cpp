#include <iostream>
#include <stdio.h>
#include "stack.h"

using namespace std;

int main(int argc, const char * argv[]) {
    Stack<int> *stack = new Stack<int>(8);
    for(int i = 4; i > 0; i--){
        stack->push(i);
    }
    //stack->reverseStack();
    stack->push(5);
    stack->push(6);
    stack->push(7);
    stack->push(8);
    stack->push(9);
    stack->reverseStack();
    /*for(int i = 0; i < stack->size; i++){
        printf("%d ", stack->data[i]);
    }*/
    for(; stack->count-1 != 0; ){
        printf("%d ", stack->pop());
    }
    printf("\n");
    Stack<string> *stack1 = new Stack<string>(4);
    stack1->push("a");
    stack1->push("o");
    stack1->push("kr");
    stack1->push("");
    stack1->reverseStack();
    if(stack1->EmptyString()) printf("%s", "Пустая строка присутсвует");
    else printf("%s", "Нет пустой строки");
    printf("\n");
    for(int i = 0; i < 4; i++){
        printf("%s\n", stack1->pop().c_str());
    }
    return 0;
}
