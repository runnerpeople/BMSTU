#include <iostream>
#include <cstdlib>
#include <algorithm>
#include <vector>
#include <set>
#include <map>
#include <string>

using namespace std;

class Dsu {
    private:
        int i;
        vector<int> parent;
        vector<int> depth;
    public:
        Dsu(int m) {
            this->i=m;
            for(int j=0;j<m;j++)
                    parent.emplace_back(j);
            depth=vector<int>(m,0);
        }
    
        int find(int x) {
            if (x == parent[x])
                return x;
            return parent[x] = find(parent[x]);
        }
    
        void Union(int x,int y) {
            x = find(x);
            y = find(y);
            if (x != y) {
                if (depth[x] < depth[y]) {
                    swap(x,y);
                }
                parent[y] = x;
                if (depth[x] == depth[y]) {
                        ++depth[x];
                }
            }
        }
};


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
                        cout << "digraph {" << endl;
                        cout << "\trankdir = LR" << endl;
                        cout << "\tdummy [label = \"\", shape = none]" << endl;
                        for(int i=0;i<n;i++) {
                                cout << "\t" + to_string(i) + " " + "[shape = circle]" << endl;
                        }
                        cout << "\tdummy -> " + to_string(q) << endl;
                        for(int i=0;i<n;i++) {
                                for(int j=0;j<m;j++) {
                                        cout << "\t" + to_string(i) + " -> " + to_string(delta[i][j]) + " ";
                                        cout << "[label = \"";
                                        cout << static_cast<char>(0x61+j);
                                        cout << "(" + phi[i][j] + ")" + "\"]" << endl;
                                }
                        }
                        cout << "}" << endl;
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
                void split1(vector<int> &pi,int &m) {
                    m = this->n;
                    Dsu q(this->n);
                    for(int i=0;i<this->n;i++) {
                        for(int j=i+1;j<this->n;j++) {
                            if (q.find(i) != q.find(j)) {
                                bool eq = true;
                                for(int k=0;k<this->m;k++) {
                                        if(phi[i][k]!=phi[j][k]) {
                                                eq = false;
                                                break;
                                        }
                                }
                                if (eq) {
                                    q.Union(i,j);
                                    m--;
                                }
                            }   
                        }
                    }
                    for(int i=0;i<this->n;i++)
                        pi[i]=q.find(i);
                }
                void split(vector<int> &pi,int &m) {
                    m = this->n;
                    Dsu q(this->n);
                    for(int i=0;i<this->n;i++) {
                        for(int j=i+1;j<this->n;j++) {
                            if (pi[i] == pi[j] && q.find(i) != q.find(j)) {
                                bool eq = true;
                                for(int k=0;k<this->m;k++) {
                                        int w1=delta[i][k];
                                        int w2=delta[j][k];
                                        if(pi[w1]!=pi[w2]) {
                                                eq = false;
                                                break;
                                        }
                                }
                                if (eq) {
                                    q.Union(i,j);
                                    m--;
                                }
                            }   
                        }
                    }
                    for(int i=0;i<this->n;i++)
                        pi[i]=q.find(i);
                }
                
                Mealy AufenkampHohn() {
                    vector<int> pi(this->n);
                    int m=-1,m_=-1;
                    split1(pi,m);
                    while (true) {  
                        if (m==m_)
                            break;
                        m_=m;
                        split(pi,m);                       
                    }
                    Mealy buf(m,this->m,0);                   
                    vector<int> new_vertex(this->n,-1);
                    vector<int> vertex(this->n);
                    int q1=0;                   
                    for(int i=0;i<this->n;i++) {
                        if(pi[i]==i) {
                            vertex[q1]=i;
                            new_vertex[i]=q1++; 
                        }
                    }
                    buf.q=new_vertex[pi[this->q]];
                    for(int i=0;i<buf.n;i++) {
                        for(int j=0;j<buf.m;j++) {
                            buf.delta[i][j]=new_vertex[pi[delta[vertex[i]][j]]];
                            buf.phi[i][j]=phi[vertex[i]][j];                                   
                        }
                    }
                    return buf;
                }
                

};

int main() {
        int n,m,q;
        cin >> n >> m >> q;
        Mealy f(n,m,q,true);
        f.AufenkampHohn().canon().print();
        return 0;
}
