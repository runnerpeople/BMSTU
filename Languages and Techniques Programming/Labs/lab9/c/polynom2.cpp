#include <string>
#include <vector>
#include <iostream>
#include <cstdlib>
#include <algorithm> 
#include <cmath>

#include "polynom2.h"

using namespace std;

    polynom2::polynom2(int size) {
        factor = new double [size];
        for (int i=0;i<size;i++) 
            factor[i] = 0;
        this->size=size;
    }
    
    
    double polynom2::value(double h) {
        double help=factor[0];
        for(int i=1;i<size;i++)
               help+=factor[i]*pow(h,i);
        return help;
    }
    
    int polynom2::degree() {  
        int n=0;
        int i;
        for(i=size-1;i>0;i--) {
            n=factor[i];
            if(n!=0)
                break;
        }
        if (i==0) {
            cout << "Polynom is null! ";
            return -1;
        }
        else return i;
    }
    
    
    polynom2 polynom2::derivative() {
        polynom2 deg(size-1);
        deg.factor[0]=factor[1];
        int count=1;
        for(int i=2;i<size;i++)
            deg.factor[count++]=factor[i]*i;
      //  delete [] factor;
      //  factor = degree2;
        return deg;
    } 
    
    ostream &operator<< (ostream &os, const polynom2 &a) {
        if (a.factor[0]!=0)
            os << a.factor[0] << " ";
        else os << "0" << " ";
        for(int i=1;i<a.size;i++) {
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
    
    double & polynom2::operator[] (int i) throw(string) {
        if (i<size)
            return factor[i];
        else {
            string exception = "ArrayIndexOutOfBoundException: " + to_string(i);
            throw exception;
        }
    }
    
    polynom2::~polynom2() {
        delete [] factor;
    }
    
    polynom2::polynom2(const polynom2 &obj) {
        size = obj.size;
        factor = new double[size];
        copy(obj.factor,obj.factor + size, factor);
    }
          
    polynom2& polynom2::operator= (polynom2 obj) {
        swap(size,obj.size);
        swap(factor,obj.factor);
        return *this;
     /*   if(this!= &obj) {
            int *new_a = new int [obj.size];
            copy(obj.factor,obj.factor + obj.size, new_a);
            delete [] factor;
            size = obj.size;
            factor = new_a;
        }
        return *this; */
     
    }
    
    polynom2 polynom2::integral() {
        polynom2 der(size+1);
        der.factor[1]=factor[0];
        for(int i=1;i<size;i++) {
            der.factor[i+1]=factor[i]/(i+1);
        }
        return der;
    } 
    
    polynom2 polynom2::sum(polynom2 g) {
        int size;
        if (g.size>this->size)
            size = g.size;
        else size = this->size;
        polynom2 sumhelp(size);
        for (int i=0;i<size;i++) {
            sumhelp.factor[i]=g.factor[i] + factor[i];
        }
        return sumhelp;
    }
    
    polynom2 polynom2::operator+ (polynom2 &obj) {
        return polynom2(this->sum(obj));
    }
