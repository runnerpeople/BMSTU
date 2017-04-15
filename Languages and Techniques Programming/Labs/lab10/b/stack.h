#ifndef LAB10_MY_STACK_H
#define LAB10_MY_STACK_H

#include <iostream>
#include <string>
#include <algorithm>
using namespace std;

template <typename T>
class Stack {
private:
    T *first;
    T *second;  //max
    int cap;
    int top;
public:
    Stack(int x) {
        this->first = new T[x];
        this->second = new T[x];
        this->cap = x;
        this->top = 0;
    }

    bool Push(T x) {
        if (top == cap)
            return false;
        first[top] = x;
        cout << "Push - " << x << endl;
        if (top == 0)
            second[top] = x;
        else {
            if (second[top - 1] > x)
                second[top] = second[top - 1];
            else
                second[top] = x;
        }
        top++;
        return true;
    }

    T Pop() {
        top--;
        cout << "Pop - " << first[top] << endl;
        return first[top];
    }

    T Max() {
        return second[top-1];
    }
    bool Empty() {
        return (top == 0);
    }

    virtual ~Stack() {
        delete [] first;
        delete [] second;
    }

    void Print() {
        if (top == 0)
            cout << "empty";
        else
            for(int i = 0; i < top; i++)
                cout << first[i] << " ";
        cout << endl;
    }

    Stack &operator= (Stack obj) {
        swap(first, obj.first);
        swap(second, obj.second);
        swap(cap, obj.cap);
        swap(top, obj.top);
        return *this;
    }

    Stack (const Stack &obj) {
        cap = obj.cap;
        top = obj.top;
        first = new T[cap];
        copy(obj.first, obj.first + cap, first);
        second = new T[cap];
        copy(obj.second, obj.second + cap, second);

    }
};

template<>
class Stack<string> {
private:
    string *first;
    string *first_reverse;
    string *second; //max
    string *second_reverse;
    int cap;
    int top;
public:
    void Reverse() {
        swap(first, first_reverse);
        swap(second, second_reverse);
    }

    Stack(int x) {
        this->first = new string[x];
        this->second = new string[x];
        this->first_reverse = new string[x];
        this->second_reverse = new string[x];
        this->cap = x;
        this->top = 0;
    }

    bool Push(string x) {
        if (top == cap)
            return false;
        first[top] = x;
        first_reverse[top] = x;
        reverse(first_reverse[top].begin(), first_reverse[top].end());
        cout << "Push - " << x << endl;
        if (top == 0) {
            second[top] = x;
            second_reverse[top] = x;
            reverse(second_reverse[top].begin(), second_reverse[top].end());
        }
        else {
            if (second[top - 1] > x) {
                second[top] = second[top - 1];
                second_reverse[top] = second[top - 1];
                reverse(second_reverse[top].begin(), second_reverse[top].end());
            }
            else {
                second[top] = x;
                second_reverse[top] = x;
                reverse(second_reverse[top].begin(), second_reverse[top].end());
            }
        }
        top++;
        return true;
    }

    string Pop() {
        top--;
        cout << "Pop - " << first[top] << endl;
        return first[top];
    }

    string Max() {
        return second[top-1];
    }
    bool Empty() {
        return (top == 0);
    }

    virtual ~Stack() {
        delete [] first;
        delete [] second;
        delete [] first_reverse;
        delete [] second_reverse;
    }

    void Print() {
        if (top == 0)
            cout << "empty";
        else
            for(int i = 0; i < top; i++)
                cout << first[i] << " ";
        cout << endl;
    }

    Stack &operator= (Stack obj) {
        swap(first, obj.first);
        swap(first_reverse, obj.second_reverse);
        swap(second, obj.second);
        swap(second, obj.second_reverse);
        swap(cap, obj.cap);
        swap(top, obj.top);
        return *this;
    }

    Stack (const Stack &obj) {
        cap = obj.cap;
        top = obj.top;
        first = new string[cap];
        copy(obj.first, obj.first + cap, first);
        first_reverse = new string[cap];
        copy(obj.first_reverse, obj.first_reverse + cap, first_reverse);
        second = new string[cap];
        copy(obj.second, obj.second + cap, second);
        second_reverse = new string[cap];
        copy(obj.second_reverse, obj.second_reverse + cap, second_reverse);

    }
};
#endif //LAB10_MY_STACK_H
