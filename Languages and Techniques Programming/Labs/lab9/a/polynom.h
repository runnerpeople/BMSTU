#ifndef POLYNOM_H
#define POLYNOM_H
#include <iostream>
#include <vector>
#include <cstdlib>

using namespace std;
class polynom {
public:
    vector<int> factor;
    friend ostream &operator<< (ostream &os, const polynom &a);
    polynom(int i);
    int value(int i);
    int degree();
    int & access(int i);
    void derivative();
    int & operator[] (int i);
};

#endif /* POLYNOM_H */
