#include <string>
#include <vector>
#include <cstdlib>
#include <iostream>

#include "polynom.h"
#include "polynom2.h"

using namespace std;

void change(polynom a) {
    try {
        a[2]=1;
        cout << a;
    }
    catch(string e) {
        cout << e << endl;
    }
}

void change2(polynom2 a) {
    try {
        a[2]=1;
        cout << a;
    }
    catch(string e) {
        cout << e << endl;
    }
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
    cout << "Derivative" << endl;
    cout << test1.derivative();
    cout << test1;
    cout << "Integral" << endl;
    cout << test1.integral();
    cout << test1;
    cout << "Sum" << endl;
    polynom sum1(n);
    for(int i=0;i<n;i++) {
        sum1[i]=rand() % 10;
     //   sum1[i]  = -test1[i];
    }
    cout << sum1;
    cout << test1.sum(sum1);
  //  cout << test1.sum(sum1).degree() << endl;
    cout << "Sum+" << endl;
    cout << sum1;
    sum1 =  sum1 + test1;
    cout << sum1;
    cout << "Function" << endl;
    change(test1);
    cout << test1;
    polynom equals1(n);
    for(int i=0;i<n;i++) {
        equals1[i]=rand() % 10;
    }
    cout << "Equals" << endl;
    test1 = equals1;
    cout << equals1;
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
    cout << "Derivative" << endl;
    cout << test2.derivative();
    cout << test2;
    cout << "Integral" << endl;
    cout << test2.integral();
    cout << test2;
    cout << "Sum" << endl;
    polynom2 sum2(n);
    for(int i=0;i<n;i++) {
        sum2[i]=rand() % 10;
     //   sum1[i]  = -test1[i];
    }
    cout << sum2;
    cout << test2.sum(sum2);
  //  cout << test2.sum(sum2).degree() << endl;
    cout << "Sum+" << endl;
    cout << sum2;
    sum2 =  sum2 + test2;
    cout << sum2;
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
    cout << equals2;
    cout << test2;
    cout << "=========================================" << endl; 
    cout << "Success" << endl;
    return 0;
}
