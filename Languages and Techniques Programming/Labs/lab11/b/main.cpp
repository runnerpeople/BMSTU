#include <iostream>
#include "SortedSeq.h"
using namespace std;

int main() {
    SortedSeq<int> t1(15);
    SortedSeq<int> t2(16);
    t1.seq.push_back(22);
    t1.seq.push_back(-17);
    cout << (t1 < t2) << endl;
    cout << (t1 > t2) << endl;
    cout << (t1 == t2) << endl;
    cout << (t1 != t2) << endl;
    cout << (t1 >= t2) << endl;
    cout << (t1 <= t2) << endl;
    t1+=t2;
    for(auto s : t1.seq)
        cout << s << " ";
    cout << endl;
    t1 += ~t1;
    for(auto s : t1.seq)
        cout << s << endl;
    return 0;
}
