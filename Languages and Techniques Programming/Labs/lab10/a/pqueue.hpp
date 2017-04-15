#ifndef PQUEUE_HPP_INCLUDED
#define PQUEUE_HPP_INCLUDED
#include <iostream>
#include <vector>
#include <cstdlib>
#include <string>

#define temp template <typename T, size_t N>
#define temp_n template <size_t N>
#define temp_t template <typename T>
#define error NULL

using namespace std;

template <typename T,size_t N>
class Elem;

template <typename T, size_t N>
class PriorityQueue {
private:
    Elem<T,N> *arr[N]; // max-heap
    Elem<T,N> *arr2[N]; // min-heap
    size_t size;        // max size of priority queue
    int count;
public:
    PriorityQueue();
    void print();
    void insert(T k);
    void heapify(int i);
    void heapify2(int i);
    T extractMin();
    T extractMax();
};

template <typename T,size_t N>
class Elem {
private:
    T k;
    int indexofmin;
    int indexofmax;
public:
    friend class PriorityQueue<T,N>;
    Elem(T k);
    Elem();
};

template <typename T,size_t N>
Elem<T,N>::Elem(T k) {
    this->k=k;
    indexofmin=0;
    indexofmax=0;
}

template <typename T,size_t N>
Elem<T,N>::Elem(){}

template <typename T, size_t N>
PriorityQueue<T,N>::PriorityQueue() {
    size = N;
    count = 0;
}

template <typename T,size_t N>
void PriorityQueue<T,N>::print()  {
    cout << "MAX_HEAP" << endl;
    cout << "========================" << endl;
    for(int i=0;i<count;i++)
        cout << arr[i]->k << " ";
    cout << endl;
    cout << "MIN_HEAP" << endl;
    cout << "========================" << endl;
    for(int i=0;i<count;i++)
        cout << arr2[i]->k << " ";
    cout << endl;
}

template <typename T, size_t N>
void PriorityQueue<T,N>::insert(T k) {
    Elem<T,N>* elem = new Elem<T,N>(k);
    int i = count;
    arr[i] = elem;
    while(i && arr[(i-1)/2]->k < k) {
        Elem<T,N> *elem_buf = new Elem<T,N>();            //insert in max-heap and update reference min-heap
        elem_buf->k=arr[(i-1)/2]->k;
        elem_buf->indexofmin=arr[(i-1)/2]->indexofmin;
        elem_buf->indexofmax=arr[(i-1)/2]->indexofmax;
        arr[i] = elem_buf;
        arr2[arr[i]->indexofmin]->indexofmax=i;
        i=(i-1)/2;
    }
    elem->indexofmax=i;
    arr[i] = elem;
    i = count;
    arr2[i] = elem;
    while(i && arr2[(i-1)/2]->k > k) {           //insert in min-heap and update reference max-heap
        Elem<T,N> *elem_buf = new Elem<T,N>();
        elem_buf->k=arr2[(i-1)/2]->k;
        elem_buf->indexofmin=arr2[(i-1)/2]->indexofmin;
        elem_buf->indexofmax=arr2[(i-1)/2]->indexofmax;
        arr2[i] = elem_buf;
        arr[arr2[i]->indexofmax]->indexofmin=i;
        i=(i-1)/2;
    }
    elem->indexofmin=i;
    arr2[i] = elem;
    count++;
}

template <typename T, size_t N>
void PriorityQueue<T,N>::heapify(int pos) {		// max heapify
    while (true) {
        int left=2*pos+1;
        int right=left+1;
        int j=pos;
        if (left <= count && arr[left]->k > arr[pos]->k)
            pos = left;
        if (right <= count && arr[right]->k > arr[pos]->k)
            pos = right;
        if (pos == j)
            break;
        int tempval = arr[j]->k;
        int tempMinIndex = arr[j]->indexofmin;
        // swap value arr[i] <-> arr[pos]
        arr[j]->k=arr[pos]->k;
        arr[j]->indexofmin=arr[pos]->indexofmin;
        arr[pos]->k=tempval;
        arr[pos]->indexofmin=tempMinIndex;
        // update reference in min heap
        arr2[arr[pos]->indexofmin]->indexofmax=pos;
        arr2[arr[j]->indexofmin]->indexofmax=j;
    }
}

template <typename T, size_t N>
void PriorityQueue<T,N>::heapify2(int pos) {     // min heapify
    while (true) {
        int left=2*pos+1;
        int right=left+1;
        int j=pos;
        if (left <= count && arr2[left]->k < arr2[pos]->k)
            pos = left;
        if (right <= count && arr2[right]->k < arr2[pos]->k)
            pos = right;
        if (pos == j)
            break;
        int tempval = arr2[j]->k;
        int tempMaxIndex = arr2[j]->indexofmax;
        // swap value arr[i] <-> arr[pos]
        arr2[j]->k=arr2[pos]->k;
        arr2[j]->indexofmax=arr2[pos]->indexofmax;
        arr2[pos]->k=tempval;
        arr2[pos]->indexofmax=tempMaxIndex;
        // update reference in max heap
        arr[arr2[pos]->indexofmax]->indexofmin=pos;
        arr[arr2[j]->indexofmax]->indexofmin=j;
    }
}


template <typename T, size_t N>
T PriorityQueue<T,N>::extractMax() {
    T elem = (T)0;
    if (--count) {
        elem = arr[0]->k;
        int minIndexOfMax = arr[0]->indexofmin;
        //create a new node with all last element min heap details
        Elem<T,N> *elem_buf = new Elem<T,N>();
        elem_buf = arr[count];
        arr[0] = elem_buf;
        arr2[elem_buf->indexofmin]->indexofmax=0;
        if(minIndexOfMax==count) {
            heapify2(0);
            heapify(0);
        } else {
            Elem<T,N> *elem_buf2 = new Elem<T,N>();
            elem_buf2=arr2[count];
            arr2[minIndexOfMax] = elem_buf2;
            arr[elem_buf2->indexofmax]->indexofmin=minIndexOfMax;
            heapify(0);
            heapify2(minIndexOfMax);
        }
    }
    return elem;
}

template <typename T, size_t N>
T PriorityQueue<T,N>::extractMin() {
    T elem = (T)0;
    if (--count) {
        elem = arr2[0]->k;
        int maxIndexOfMax = arr2[0]->indexofmax;
        //create a new node with all last element max heap details
        Elem<T,N> *elem_buf = new Elem<T,N>();
        elem_buf=arr[count];
        arr2[0] = elem_buf;
        arr[elem_buf->indexofmax]->indexofmin=0;
        if(maxIndexOfMax==count) {
            heapify2(0);
            heapify(0);
        } else {
            Elem<T,N> *elem_buf2 = new Elem<T,N>();
            elem_buf2=arr[count];
            arr[maxIndexOfMax] = elem_buf2;
            arr2[elem_buf2->indexofmin]->indexofmax=maxIndexOfMax;
            heapify2(0);
            heapify(maxIndexOfMax);
        }
    }
    return elem;
}

template <size_t N>
class PriorityQueue<bool,N> {
private:
    int true_number;
    int false_number;
    int size;
    int count;
public:
    PriorityQueue();
    void insert(bool elem);
    bool extractMin() throw (string);
    bool extractMax() throw (string);
    void print();
};

template <size_t N>
void PriorityQueue<bool,N>::print() {
    cout << "True_number " << true_number << endl;
    cout << "False_number " << false_number << endl;
}

template <size_t N>
PriorityQueue<bool,N>::PriorityQueue() {
    size = N;
    true_number = 0;
    false_number = 0;
    count = 0;
}

template <size_t N>
void PriorityQueue<bool,N>::insert(bool elem) {
    if (elem == true)
        true_number++;
    else false_number++;
    count++;
}


template <size_t N>
bool PriorityQueue<bool,N>::extractMax() throw(string) {
    if (size>=0 && false_number>0) {
        false_number--;
        size--;
        return false;
    }
    else {
        if (size >= 0) {
            true_number--;
            size--;
            return true;
        }
        else {
            string exception = "NoSuchElementException";
            throw exception;
        }
    }
}

template <size_t N>
bool PriorityQueue<bool,N>::extractMin() throw(string) {
    if (size>=0 && true_number>0) {
        true_number--;
        size--;
        return true;
    }
    else {
        if (size >= 0) {
            false_number--;
            size--;
            return true;
        }
        else {
            string exception = "NoSuchElementException";
            throw exception;
        }
    }
}
#endif /*PQUEUE_HPP_INCLUDED */
