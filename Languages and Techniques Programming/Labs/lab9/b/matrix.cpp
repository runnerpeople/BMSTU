#include <iostream>
#include "matrix.h"
    
using namespace std;
    
Matrix::Matrix(int row, int col) {
        this->row = row;
        this->col = col; 
        for(int i = 0; i < row; i++)
            for(int j = 0; j < col; j++)
                M.push_back(vector<int>(col, 0));
}
     
/* int Matrix::get_col() {
        return col;
} */

int Matrix::get_col() {
        return M[0].size();
}
     
/* int Matrix::get_row() {
        return row;
} */

int Matrix::get_row() {
        return M.size() / M[0].size();
}
    
    
vector<int> & Matrix::operator[] (int i) {
        return M[i];
}
     
     
ostream &operator<< (ostream &os, const Matrix &t) {
    for(int i = 0; i < t.row; i++) {
        for (int j = 0; j < t.col; j++)
        os << t.M[i][j] << " ";
        os << endl;
        }
        return os;
}
     
Matrix Matrix::delete_col(int x) {
    Matrix m_new(row, col-1);
    for(int i = 0; i < row; i++) {
        for(int j = 0, j_new = 0; j < col; j++, j_new++) {
            if (j == x)
                j++;
            m_new[i][j_new] = M[i][j];
        }
    }
 /*   this->col--;
    this->M = m_new.M; */
    return m_new;
}
     
Matrix Matrix::delete_row(int x) {
   Matrix m_new(row-1, col);
   for(int i = 0, i_new = 0; i < row; i++, i_new++)
      for(int j = 0; j < col; j++) {
            if (i == x)
                i++;
            m_new[i_new][j] = M[i][j];
      }
 /*  this->row--;
   this->M = m_new.M; */
   return m_new;
}

