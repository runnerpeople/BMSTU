#include <string>
#include <vector>
#include <cstdlib>
#include <iostream>

#include "polynom.h"
#include "polynom2.h"

using namespace std;

void change(polynom a) {
    a.access(3)=1;
    cout << a;
}

void change2(polynom2 a) {
    a.access(3)=1;
    cout << a;
}

int main() {
    int n,y;
    cin >> n;
    
    polynom test1(n);
    for(int i=0;i<n;i++) {
        cin >> test1[i];
    }
    cout << "A(x): " << test1;
    cout << test1.degree() << endl;
    cout << test1.value(10) << endl;
    test1.derivative();
    cout << test1;
    cout << "Function" << endl;
    change(test1);
    cout << test1;
    polynom equals1(n);
    for(int i=0;i<n;i++) {
        equals1[i]=rand() % 10;
    }
    cout << "Equals" << endl;
    cout << equals1;
    test1 = equals1;
    cout << test1;
    cout << "========================================" << endl;
    cin >> y;
    polynom2 test2(y);
    for(int i=0;i<y;i++) {
        cin >> test2[i];
    }
    cout << "B(x): " << test2;
    cout << test2.degree()<<endl;
    cout << test2.value(10) << endl;
    test2.derivative();
    cout << test2;
    cout << "Function" << endl;
    change2(test2);
    cout << test2;
    polynom2 equals2(n);
    for(int i=0;i<n;i++) {
        equals2[i]=rand() % 10;
    }
    cout << "Equals" << endl;
    cout << equals2;
    test2 = equals2;
    cout << test2;
    cout << "Success" << endl;
    return 0;
}
