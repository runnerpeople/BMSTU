#include <iostream>
#include <cstdlib>
#include <algorithm>
#include "element.hpp"

using namespace std;

int main() {
    srand(time(0));
    Seq<Elem> a;
    for(int i =0;i<5;i++) {
        string s =to_string(rand() % 10 + 10);
        Elem x(s);
        cout << "Insert begin " << s << endl;
        a >> x;
        string y = to_string(rand() % 10 + 20);
        Elem r(y);
        cout << "Insert end " << y << endl;
        a << r; 
    }
    a.print();
    Seq<Elem> b;
    for(int i =0;i<5;i++) {
        string s =to_string(rand() % 10 + 10);
        Elem x(s);
        cout << "Insert begin " << s << endl;
        b >> x;
        string y = to_string(rand() % 10 + 20);
        Elem r(y);
        cout << "Insert end " << y << endl;
        b << r; 
    }
    b.print();
    cout << boolalpha << (a==b) << endl;
    cout << boolalpha << (a!=b) << endl;
    !a;
    a.print();
    !b;
    b.print();
    Seq<Elem> c;
    c = a * b;
    c.print();
    return 0;
}
