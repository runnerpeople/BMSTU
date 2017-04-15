#include <iostream>
using namespace std;

template <typename T>
class Stack{
public:
    T *data;
    int count, size, head, tail, flag;
    Stack(int size){
        data = (T*)malloc(size*sizeof(T));
        this->size = size;
        this->head = this->tail = this->count = 0;
        this->flag = 1;
    }
    T pop(){
        if(flag == 1 && tail == -1){
            this->count--;
            return data[(tail = size-1)--];
        }
        else if(flag == 0 && tail == size){
            this->count--;
            return data[(tail = 0)];
        }
        else if(flag == 0){
            this->count--;
            T x = data[tail];
            tail++;
            return x;
        }
        else if(flag == 1){
            this->count--;
            T x = data[tail];
            tail--;
            return x;
        }
        return NULL;
    }
    void push(T elem){
        if(!stackoverflow() && flag == 0 && tail == 0) data[(tail = size-1)--] = elem;
        else if(!stackoverflow() && flag == 1 && tail == size) data[(tail = 0)++] = elem;
        else if(!stackoverflow() && flag == 0) data[tail--] = elem;
        else if(!stackoverflow() && flag == 1) data[tail++] = elem;
        else{
            double_size();
            push(elem);
        }
        this->count++;
    }
    void double_size(){
        realloc(data, 2*size);
        size = 2*size;
    }
    bool stackEmpty(){
        return count == 0;
    }
    bool stackoverflow(){
        return size == count;
    }
    void reverseStack(){
        int temp = head;
        if(flag == 1) head = tail-1;
        else head = tail+1;
        tail = temp;
        if(flag == 1) flag = 0;
        else flag = 1;
    }
    
    Stack operator= (const Stack<T> & a){
        if(this == &a) return *this;
        T *data = new int[a.count];
        for(int i = 0; i < a.count; i++){
            data[i] = a.data[i];
        }
        delete [] this->data;
        tail = a.tail;
        head = a.head;
        size = a.count;
        data = new int[a.count];
        delete [] data;
        return *this;
    }
    
    Stack(const Stack & a){
        tail = a.tail;
        head = a.head;
        data = new int[a.count];
        for(int i = 0; i<a.count; i++){
            data[i] = a.data[i];
        }
    }
    ~Stack(){
        delete [] data;
    }
};

template<>
class Stack<string>{
public:
    string *data;
    int count, size, head, tail, flag;
    bool flag1 = false;
    Stack(int size){
        data = (string*)malloc(size*sizeof(string));
        this->size = size;
        this->head = this->tail = this->count = 0;
        this->flag = 1;
    }
    string pop(){
        if(flag == 1 && tail == -1){
            this->count--;
            return data[(tail = size-1)--];
        }
        else if(flag == 0 && tail == size){
            this->count--;
            return data[(tail = 0)];
        }
        else if(flag == 0){
            this->count--;
            string x = data[tail];
            tail++;
            return x;
        }
        else if(flag == 1){
            this->count--;
            string x = data[tail];
            tail--;
            return x;
        }
        return NULL;
    }
    void push(string elem){
        if(elem.length() == 0) flag1 = true;
        if(!stackoverflow() && flag == 0 && tail == 0) data[(tail = size-1)--] = elem;
        else if(!stackoverflow() && flag == 1 && tail == size) data[(tail = 0)++] = elem;
        else if(!stackoverflow() && flag == 0) data[tail--] = elem;
        else if(!stackoverflow() && flag == 1) data[tail++] = elem;
        else{
            double_size();
            push(elem);
        }
        this->count++;
    }
    void double_size(){
        realloc(data, 2*size);
        size = 2*size;
    }
    bool stackEmpty(){
        return count == 0;
    }
    bool stackoverflow(){
        return size == count;
    }
    void reverseStack(){
        int temp = head;
        if(flag == 1) head = tail-1;
        else head = tail+1;
        tail = temp;
        if(flag == 1) flag = 0;
        else flag = 1;
    }
    
    bool EmptyString(){
        return flag1;
    }
    
    Stack operator= (const Stack<string> & a){
        if(this == &a) return *this;
        string *data = new string[a.count];
        for(int i = 0; i < a.count; i++){
            data[i] = a.data[i];
        }
        delete [] this->data;
        tail = a.tail;
        head = a.head;
        size = a.count;
        data = new string[a.count];
        delete [] data;
        return *this;
    }
    
    Stack(const Stack & a){
        tail = a.tail;
        head = a.head;
        data = new string[a.count];
        for(int i = 0; i<a.count; i++){
            data[i] = a.data[i];
        }
    }
    ~Stack(){
        delete [] data;
    }
};
