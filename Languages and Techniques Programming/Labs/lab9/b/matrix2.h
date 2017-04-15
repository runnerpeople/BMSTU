#ifndef MATRIX2_H
#define MATRIX2_H
     
#include "iostream"
 
using namespace std;
     
     
class Matrix2 {
    public:
        int row, col; // строка, столбец
        int **M;
     
        Matrix2(int row, int col);
        int get_row();
        int get_col();
        Matrix2 delete_row(int x);
        Matrix2 delete_col(int x);   
        int* & operator[] (int i);
        virtual ~Matrix2();
        Matrix2 &operator= (Matrix2 obj);
        Matrix2 (const Matrix2&);
        friend ostream &operator<< (ostream &os, const Matrix2 &m);
     
};
     
#endif //MATRIX2_H
