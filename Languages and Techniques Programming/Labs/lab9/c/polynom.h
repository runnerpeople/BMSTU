#ifndef POLYNOM_H
#define POLYNOM_H
#include <iostream>
#include <vector>
#include <cstdlib>

using namespace std;
class polynom {
    private: 
        vector<double> factor;
    public:             
        friend ostream &operator<< (ostream &os, const polynom &a);
        polynom(int i);
        double value(double i);
        int degree();
        polynom derivative();
        double & operator[] (int i) throw (string);
        polynom integral();
        polynom sum(polynom i);
        polynom operator+ (polynom &obj);
};

#endif /* POLYNOM_H */
