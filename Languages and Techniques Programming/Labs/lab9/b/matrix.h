#ifndef MATRIX_H
#define MATRIX_H
     
#include "vector"
#include "iostream"
 
using namespace std;
     
     
class Matrix {
    public:
        int row, col; // строка, столбец
        vector<vector<int>> M;
     
        Matrix(int row, int col);
        int get_row();
        int get_col();
        Matrix delete_row(int x);
        Matrix delete_col(int x);   
        vector<int> & operator[] (int i);
        friend ostream &operator<< (ostream &os, const Matrix &m);
     
};
     
#endif //MATRIX_H
