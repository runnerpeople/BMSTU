#include <iostream>
#include <cstdlib>
#include "pqueue.hpp"

using namespace std;

int main() {
    srand(time(0));
    PriorityQueue<int,10> a;
    for(int i=0;i<10;i++) {
        int y = rand() % 153;
        cout << i << " ";
        a.insert(i);
    }
    cout << endl;
    cout << a.extractMin() << endl;
    cout << a.extractMin() << endl;
    cout << a.extractMax() << endl;
    cout << a.extractMin() << endl;
    cout << a.extractMax() << endl;
    cout << a.extractMax() << endl;
    PriorityQueue<bool,12> t;
    for(int i=0;i<12;i++) {
        int j=rand() % 2;
        if(j==1) {
            t.insert(true);
            cout << "True" << " ";
        }
        else {
            t.insert(false);
            cout << "False" << " ";
        }
    }
    cout << endl;
    t.print();
    cout << "Heap" << endl;
    cout << t.extractMin() << " " << t.extractMax() << endl;
    t.print();
    return 0;
}
