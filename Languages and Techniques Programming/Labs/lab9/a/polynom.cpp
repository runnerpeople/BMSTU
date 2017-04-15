#include <string>
#include <vector>
#include <iostream>
#include <cstdlib>
#include <cmath>

#include "polynom.h"

using namespace std;

polynom::polynom(int size) {
    for(int i=0;i<size;i++)
        factor.push_back(0);
}


int polynom::value(int h) {
    int help=factor[0];
    for(int i=1;i<factor.size();i++)
        help+=factor[i]*pow(h,i);
    return help;
}

int polynom::degree() {
    int n=0;
    for(int i=factor.size()-1;i>=0;i--) {
        n=factor[i];
        if(n!=0)
            break;
    }
    if (n==0) {
        cout << "Polynom is null!" << endl;
        return 0;
    }
    else return n-1;
}

int & polynom::access(int i) {
    if (i<factor.size())
        return factor[i];
    else {
        cout << "Error" << endl;
    }
}

int & polynom::operator[] (int i) {
    if (i<factor.size())
        return factor[i];
    else {
        cout << "Error" << endl;
    }
}

void polynom::derivative() {
    vector<int> degree2;
    degree2.push_back(factor[1]);
    for(int i=2;i<factor.size();i++)
        degree2.push_back(factor[i]*i);
    factor = degree2;
}

ostream &operator<< (ostream &os, const polynom &a) {
    if (a.factor[0]!=0)
        os << a.factor[0] << " ";
    for(int i=1;i<a.factor.size();i++) {
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
