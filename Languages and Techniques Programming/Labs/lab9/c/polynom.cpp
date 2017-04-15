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
    
    
    double polynom::value(double h) {
        double help=factor[0];
        for(int i=1;i<factor.size();i++)
               help+=factor[i]*pow(h,i);
        return help;
    }
    
    int polynom::degree() {  
        int n=0;
        int i;
        for(i=factor.size()-1;i>=0;i--) {
            n=factor[i];
            if(n!=0)
                break;
        }
        if (n==0) {
            cout << "Polynom is null!" << endl;
            return -1;
        }
        else return i;
    }
    
    
    double & polynom::operator[] (int i) throw(string) {
        if (i<factor.size())
            return factor[i];
        else {
            string exception = "ArrayIndexOutOfBoundException: " + to_string(i);
            throw exception;
        }
    }
    
    
    polynom polynom::derivative() {
        polynom der(factor.size()-1);
        der.factor[0]=factor[1];
        for(int i=2;i<factor.size();i++)
            der.factor[i-1]=factor[i]*i;
        return der;
    }
    
    ostream &operator<< (ostream &os, const polynom &a) {
        if (a.factor[0]!=0)
            os << a.factor[0] << " ";
        else os << "0" << " ";
        for(int i=1;i<a.factor.size();i++) {
            if (a.factor[i]==0)
                continue;
            else {
                if (a.factor[i]<0)
                    os << "-" << " " << -a.factor[i] << "*" << "x^" << i << " ";
                else os << "+" << " " << a.factor[i] << "*" << "x^" << i << " ";
            }
        }
        os << endl;
        return os;
    }
    
    polynom polynom::integral() {
        polynom der(factor.size()+1);
        der.factor[1]=factor[0];
        for(int i=1;i<factor.size();i++) {
            der.factor[i+1]=factor[i]/(i+1);
        }
        return der;
    }
    
    polynom polynom::sum(polynom g) {
        int size;
        if (g.factor.size()>factor.size())
            size = g.factor.size();
        else size = factor.size();
        polynom sumhelp(size);
        for (int i=0;i<size;i++) {
            sumhelp.factor[i]=g.factor[i] + factor[i];
        }
        return sumhelp;
    }
    
    polynom polynom::operator+ (polynom &obj) {
        return polynom(this->sum(obj));
    }
