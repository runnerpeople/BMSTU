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
        public:
                Mealy(int n,int m,int q,bool scan) {
                        this->n=n;
                        this->m=m;
                        this->q=q;  
                        delta=vector<vector<int>>(n,vector<int>(m,0)); 
                        phi=vector<vector<string>>(n,vector<string>(m,"x"));
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
};
               
int main() {
        int n,m,q;
        cin >> n >> m >> q;
        Mealy f(n,m,q,true);
        f.print();
        return 0;
}
