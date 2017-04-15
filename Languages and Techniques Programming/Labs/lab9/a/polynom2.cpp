#include <string>
#include <vector>
#include <iostream>
#include <cstdlib>
#include <algorithm>
#include <cmath>

#include "polynom2.h"

using namespace std;

polynom2::polynom2(int size) {
    factor = new int [size];
    this->size=size;
}


int polynom2::value(int h) {
    int help=factor[0];
    for(int i=1;i<size;i++)
        help+=factor[i]*pow(h,i);
    return help;
}

int polynom2::degree() {
    int n=0;
    for(int i=size-1;i>0;i--) {
        n=factor[i];
        if(n!=0)
            break;
    }
    if (n==0) {
        cout << "Polynom is null! ";
        return 0;
    }
    else return n-1;
}

int & polynom2::access(int i) {
    if (i<size)
        return factor[i];
    else {
        cout << "Error" << endl;
    }
}

void polynom2::derivative() {
    int *degree2 = new int [size-1];
    degree2[0]=factor[1];
    int count=1;
    for(int i=2;i<size;i++)
        degree2[count++]=factor[i]*i;
    delete [] factor;
    factor = degree2;
}

ostream &operator<< (ostream &os, const polynom2 &a) {
    if (a.factor[0]!=0)
        os << a.factor[0] << " ";
    for(int i=1;i<a.size;i++) {
        if (a.factor[i]==0)
            continue;
        else {
            if (a.factor[i]<0)
                os << "-" << " " << a.factor[i] << "*" << "x^" << i << " ";
            else os << "+" << " " << a.factor[i] << "*" << "x^" << i << " ";
        }
    }
    os << endl;
    return os;
}

int & polynom2::operator[] (int i) {
    if (i<size)
        return factor[i];
    else {
        cout << "Error" << endl;
    }
}

polynom2::~polynom2() {
    delete [] factor;
}

polynom2::polynom2(const polynom2 &obj) {
    size = obj.size;
    factor = new int[size];
    copy(obj.factor,obj.factor + size, factor);
}


polynom2& polynom2::operator= (const polynom2 &obj) {
    if(this!= &obj) {
        int *new_a = new int [obj.size];
        copy(obj.factor,obj.factor + obj.size, new_a);
        delete [] factor;
        size = obj.size;
        factor = new_a;
    }
    return *this;
}
