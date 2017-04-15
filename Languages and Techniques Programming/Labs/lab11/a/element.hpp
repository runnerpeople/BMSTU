#ifndef ELEMENT_HPP_INCLUDED
#define ELEMENT_HPP_INCLUDED
#include <iostream>
#include <vector>
#include <cstdlib>
#include <string> 
#include <algorithm>

using namespace std;

template <typename Element> 
class Seq {
    private:
        vector<Element> arr;
    public:
        Seq<Element>(const Seq<Element>& obj) {
            arr = obj.arr;
        }
        Seq<Element>& operator= (Seq<Element> &obj) {
            swap(arr,obj.arr);
            return *this;           
        }
        Seq<Element>& operator* (Seq<Element> &obj) {
            this->arr.insert(this->arr.end(),obj.arr.begin(),obj.arr.end());
            for(auto i = this->arr.begin(); i!= this->arr.end()-1;) {  
                if (*i == !(*(i+1))) {
                     !(*(i+1));
                     this->arr.erase(i);
                }
                else {
                    !(*(i+1));
                    i++;
                }
            }
            return *this;
        }
        Seq<Element>& operator! () {
           reverse(arr.begin(),arr.end()); 
           for(auto i = arr.begin();i != arr.end(); i++)
                !(*i);
        }
        Seq<Element>& operator<< (Element obj) {
            arr.push_back(obj);
            for(auto i = arr.begin(); i!= arr.end()-1;) {  
                if (*i == !(*(i+1))) {
                    !(*(i+1));
                    arr.erase(i);
                }
                else {
                    !(*(i+1));
                    i++; 
                }
            }
        }
        Seq<Element>& operator>> (Element obj) {
            arr.insert(arr.begin(),obj);
            for(auto i = arr.begin(); i!= arr.end()-1;) {  
                if (*i == !(*(i+1))) {
                    !(*(i+1));
                    arr.erase(i);
                }
                else {
                    !(*(i+1));
                    i++;
                }
            } 
        }
        const bool operator== (Seq<Element> &obj) const {
            auto i = arr.begin();
            auto j = obj.arr.begin();
            if (obj.arr.size() != this->arr.size())
                return false;
            for(; i!= arr.end() && j!=obj.arr.end();i++,j++)
                if (!(*i == *j))
                    break;
            if (i!= arr.end() && j!=obj.arr.end())
                return false;
            else return true;
                
        }
        const bool operator!= (Seq<Element> &obj) const {
            return !(*this == obj);
        }
        
        Seq(){}
        
        void print() {
            for(auto i = arr.begin();i != arr.end(); i++)
                cout << *i << " ";
            cout << endl; 
        }
};

class Elem {
    private:
        string a;
    public:  
        Elem(string a){
            this->a=a;
        }
        Elem& operator! () {
            reverse(this->a.begin(),this->a.end());
            return *this;
        }
        const bool operator== (Elem &obj) const {
            string y = (!(!obj)).a;
            if (y.compare(this->a)==0)
                return true;
            else return false;
        }
        
        friend ostream& operator<< (ostream &os, const Elem &b) {
            cout << b.a << " ";
        }
};


#endif /*PQUEUE_HPP_INCLUDED */
