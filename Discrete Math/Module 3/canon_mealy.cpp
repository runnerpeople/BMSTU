#include <iostream>
#include <cstdlib>
#include <algorithm>
#include <vector>
#include <set>
#include <map>
#include <string>

using namespace std;

class Mealy{
        private:
                int n,m,q;
                vector<vector<int>> delta;
                vector<vector<string>>phi;
                vector<bool> used;  //for dfs
                vector<int> number; //for dfs
        public:
                Mealy(int n,int m,int q,bool scan) {
                        this->n=n;
                        this->m=m;
                        this->q=q;  
                        delta=vector<vector<int>>(n,vector<int>(m,0)); 
                        phi=vector<vector<string>>(n,vector<string>(m,"x"));
                        for(int i=0;i<n;i++) {
                                used.push_back(false);
                                number.push_back(-1);
                        }
                        for(int i=0;i<n;i++) {
                                for(int j=0;j<m;j++) {
                                        int buf;
                                        cin >> buf;                   
                                        delta[i][j]=buf;
                                }
                        }
                        for(int i=0;i<n;i++) {
                                for(int j=0;j<m;j++) {
                                        string buf;
                                        cin >> buf;                   
                                        phi[i][j]=buf;
                                }
                        }
                }
                Mealy(int n,int m,int q) {
                        this->n=n;
                        this->m=m;
                        this->q=q;  
                        delta=vector<vector<int>>(n,vector<int>(m,0)); 
                        phi=vector<vector<string>>(n,vector<string>(m,"x"));
                        for(int i=0;i<n;i++) {
                                used.push_back(false);
                                number.push_back(-1);
                        }
                }
                void dfs(int start,int &ind,vector<int> buf) {
                        number[start]=ind++;
                        used[start]=true;
                        for(int i=0;i<m;i++)
                                if (!(used[buf[i]]))
                                        dfs(buf[i],ind,delta[buf[i]]);
                }
                void print() {
                        cout << n << endl;
                        cout << m << endl;
                        cout << q << endl;
                        for(int i=0;i<n;i++) {
                                for(int j=0;j<m;j++)
                                        cout << delta[i][j] << " ";
                                cout << endl;
                        }
                        for(int i=0;i<n;i++) {
                                for(int j=0;j<m;j++)
                                        cout << phi[i][j] << " ";
                                cout << endl;
                        } 
                }
                Mealy canon() {
                        Mealy buf(this->n,this->m,0);
                        int index=0;
                        dfs(q,index,delta[q]);
                        buf.n=index;
                        for(int i=0;i<n;i++) {
                                if (used[i] && number[i]!=-1) {
                                        buf.phi[this->number[i]]=phi[i];
                                        for(int j=0;j<m;j++) {
                                                buf.delta[number[i]][j]=number[delta[i][j]];
                                        }           
                                }
                        }
                        return buf;
                }
};
               
int main() {
        int n,m,q;
        cin >> n >> m >> q;
        Mealy f(n,m,q,true);
        Mealy s(f.canon());
        s.print();
        return 0;
}
