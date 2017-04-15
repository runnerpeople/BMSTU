#ifndef POLYNOM2_H
#define POLYNOM2_H

#include <string>
#include <vector>

using namespace std;

class polynom2 {
public:
    int *factor;
    int size;
    friend ostream &operator<< (ostream &os, const polynom2 &a);
    polynom2(int i);
    int value(int i);
    int degree();
    int & access(int i);
    void derivative();
    virtual ~polynom2();
    polynom2 &operator= (const polynom2 &obj);
    polynom2(const polynom2&);
    int & operator[] (int i);
};

#endif /* POLYNOM2_H */
