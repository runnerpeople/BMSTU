#include <iostream>
#include <fstream>
#include <sstream>
#include <cstdlib>
#include <algorithm>
#include <vector>
#include <set>
#include <map>
#include <string>

using namespace std;

set<string> bigramm(const string &s) {
        set<string> buf;
        if (s.size()<=2)
                buf.emplace(s);        
        else {       
                for(auto it=s.begin();it!=s.end()-1;it++) {
                        string t(it,it+2);
                        buf.emplace(t);
                }
        }
        return buf;
}

double similarity(const set<string> &f,const set<string> &s) {
        set<string> tmp;    
        set_intersection(f.begin(),f.end(),s.begin(),s.end(),inserter(tmp,tmp.begin()));
        double inter_ = tmp.size();
        if (inter_==0)
                return 0;
        else tmp.clear();
        set_union(f.begin(),f.end(),s.begin(),s.end(),inserter(tmp,tmp.begin()));
        double union_ = tmp.size();  
        return  inter_ / union_;
}


int main() {
        ifstream f("count_big.txt");
        map<string,pair<set<string>,int>> dictionary;
        string line; 
        while (getline(f,line)) {   
                string a;
                stringstream ssin(line);
                vector<string> freq_string;
                while (ssin >> a)
                        freq_string.push_back(a);
                dictionary.emplace(freq_string[0],make_pair(bigramm(freq_string[0]),stoi(freq_string[1])));
        }
        f.close();
        do {
                string a;
                cin >> a;
                string accept_ = a;
                double measure = -0.00001;
                set<string> bigrams =bigramm(a);
                int freq = -1;
                for(auto it=dictionary.begin();it!=dictionary.end();it++) {
                        double similar = similarity(bigrams,(*it).second.first);
                        int freq_word = (*it).second.second;
                        string word = (*it).first;
                        if (similar>measure){
                                measure = similar;
                                accept_ = word;
                                freq=freq_word;
                        }
                        else if ((similar==measure && freq < freq_word) || (similar==measure && freq == freq_word && word < accept_)) {
                                measure = similar;
                                accept_ = word;
                                freq=freq_word;
                        }
                }
                if (a != "")
                        cout << accept_ << endl;
        } while (getline(cin,line));
        return 0;
}
