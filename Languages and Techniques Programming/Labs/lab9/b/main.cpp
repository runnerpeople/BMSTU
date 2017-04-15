#include <iostream>
#include "matrix.h"
#include "matrix2.h"     
using namespace std;

void change(Matrix y) {
    y[0][1] = 1;
    cout << y;
}

void change2(Matrix2 y) {
    y[0][1] = 1;
    cout << y;
}
     
int main() {
    int n,y;
    cin >> n;
    cin >> y;
    cout << "Matrix" << endl;
    Matrix m(n, y);
    for(int i=0;i<n;i++) {
        for(int j=0;j<y;j++)
            m[i][j]=rand() % 10;
    }
    cout << m;
    cout << "===============================" << endl;
    cout << m.get_row() << "*" << m.get_col() << endl; 
    cout << "===============================" << endl;
    cout << m.delete_col(1);
    cout << "===============================" << endl;
    cout << m;
    cout << "===============================" << endl;
    cout << m.delete_row(3); 
    cout << "===============================" << endl;
    cout << "Change" << endl;
    change(m);
    cout << "===============================" << endl;
    cout << "Now" << endl;
    cout << m;
    cout << "===============================" << endl;
    Matrix m2(n,y);
    for(int i=0;i<n;i++) {
        for(int j=0;j<y;j++)
            m2[i][j]=i+j;
    }
    cout << m2;
    cout << "===============================" << endl;
    cout << "Equals" << endl;
    m = m2;
    cout << m;
    int n1,y1;
    cin >> n1;
    cin >> y1;
    cout << "Matrix" << endl;
    Matrix2 mb(n1, y1);
    for(int i=0;i<n;i++) {
        for(int j=0;j<y;j++)
            mb[i][j]=rand() % 10;
    }
    cout << mb;
    cout << "===============================" << endl;
    cout << mb.get_row() << "*" << mb.get_col() << endl; 
    cout << "===============================" << endl;
    cout << mb.delete_col(1);
    cout << "===============================" << endl;
    cout << mb;
    cout << "===============================" << endl;
    cout << mb.delete_row(1); 
    cout << "===============================" << endl;
    cout << "Change" << endl;
    change2(mb);
    cout << "===============================" << endl;
    cout << "Now" << endl;
    cout << mb;
    cout << "===============================" << endl;
    Matrix2 mb2(n,y);
    for(int i=0;i<n;i++) {
        for(int j=0;j<y;j++)
            mb2[i][j]=i+j;
    }
    cout << mb2;
    cout << "===============================" << endl;
    cout << "Equals" << endl;
    mb = mb2;
    cout << mb;
        return 0;
    }
