#include <iostream>
#include "matrix2.h"
    
using namespace std;
    
Matrix2::Matrix2(int row, int col) {
        this->row = row;
        this->col = col; 
        M = new int *[row];
        for(int i = 0; i < row; i++) {
            M[i] = new int [col];
            for(int j = 0; j < col; j++)
                M[i][j]=0;
        }
}
     

int Matrix2::get_col() {
        return col;
}
     

int Matrix2::get_row() {
        return row;
}
    
    
int* & Matrix2::operator[] (int i) {
        return M[i];
}

Matrix2 Matrix2::delete_col(int x) {
    Matrix2 m_new(row, col-1);
    for(int i = 0; i < row; i++) {
        for(int j = 0, j_new = 0; j < col; j++, j_new++) {
            if (j == x)
                j++;
            m_new[i][j_new] = M[i][j];
        }
    }
    return m_new;
}
     
Matrix2 Matrix2::delete_row(int x) {
   Matrix2 m_new(row-1, col);
   for(int i = 0, i_new = 0; i < row; i++, i_new++)
      for(int j = 0; j < col; j++) {
            if (i == x)
                i++;
            m_new[i_new][j] = M[i][j];
      }
   return m_new;
}
     
     
ostream &operator<< (ostream &os, const Matrix2 &t) {
    for(int i = 0; i < t.row; i++) {
        for (int j = 0; j < t.col; j++)
            os << t.M[i][j] << " ";
        os << endl;
    }
    return os;
}

Matrix2::~Matrix2() {
    for (int i = 0; i < row; i++)
        delete [] M[i];
    delete [] M;
}
     


 Matrix2::Matrix2(const Matrix2 &obj) {
    row = obj.row;
    col = obj.col;
    M = new int *[row];
    for(int i = 0; i < row; i++) {
        M[i] = new int [col];
        copy(obj.M[i], obj.M[i]+col, M[i]);
      /*  for (int k = 0;k<col;k++)
            M[i][k] = obj.M[i][k]; */
    }
}
        
    
Matrix2& Matrix2::operator= (Matrix2 obj) {
        swap(row,obj.row);
        swap(col,obj.col);
        swap(M,obj.M);
        return *this;    
} 
