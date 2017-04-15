#ifndef POLYNOM2_H
#define POLYNOM2_H

#include <string>
#include <vector>

using namespace std;

class polynom2 {
    private:  
        double *factor;
        int size;
    public:      
        friend ostream &operator<< (ostream &os, const polynom2 &a);
        polynom2(int i);
        double value(double i);
        int degree();
        polynom2 derivative();
        virtual ~polynom2();
        polynom2 &operator= (polynom2 obj);
        polynom2(const polynom2&);
        double & operator[] (int i) throw (string);
        polynom2 integral();
        polynom2 sum(polynom2 i);
        polynom2 operator+ (polynom2 &obj);
};

#endif /* POLYNOM2_H */
