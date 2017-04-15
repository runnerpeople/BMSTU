#include <iostream>
#include <algorithm>
#include <fstream>
#include <map>
#include <vector>
#include <set>

using namespace std;

typedef struct token {
    int popularity;
    string word;

    token(int popularity,string word): popularity(popularity),word(word) {};
    token() {};
} token;

set<string> set_bigrams(const string& s) {
    set<string> out;
    if (s.length() == 1) out.insert(s);
    for (size_t i = 0; i + 1 < s.length(); i++) {
        out.insert(s.substr(i, 2));//substring обрезает строчку с позиции i на длину j
    }
    return out;
}

void reading_file(map<string,pair<set<string>,int>> &dict) {
    //ifstream file("/home/roman/ClionProjects/count_big.txt");
    ifstream file("count_big.txt");
    string word;
    int popularity;
    token current_token;
    while (file) {
        file >> word >> popularity;
        current_token = token(popularity,word);
        dict[current_token.word].second = current_token.popularity;
        dict[current_token.word].first = set_bigrams(current_token.word);
    }
    file.close();
}

void read_in(vector<string> &in) {
    string line;
    while (getline(cin, line)) {
        if (line == "") break;
        in.push_back(line);
    }
}

double similarity(const set<string> &a,const set<string> &b) {
    vector<string> buf;
    set_intersection(a.begin(),a.end(),b.begin(),b.end(),back_inserter(buf));
    if (buf.size()==0)
        return 0;
    vector<string> buf2;
    set_union(a.begin(),a.end(),b.begin(),b.end(),back_inserter(buf2));
    return (double)(buf.size())/(double)(buf2.size());
}

string find_result(const map<string,pair<set<string>, int>> &dict, const string& word) {
    set<string> bigrams = set_bigrams(word);
    string ans;
    double cur_similarity = 0;
    int pop = 0;

    for (auto pair: dict) {
        double buf = similarity(bigrams, pair.second.first);

        if ((cur_similarity < buf) ||
            (cur_similarity <= buf && pop < pair.second.second) ||
            (cur_similarity <= buf && pop <= pair.second.second && pair.first.length() < ans.length())) {
            pop = pair.second.second;
            cur_similarity = buf;
            ans = pair.first;
        }
    }
    return ans;
}

void error_correct(const vector<string> &in,vector<string> &out,const map<string, pair<set<string>,int>> &dict) {
    for (string s: in) {
        out.push_back(find_result(dict, s));
    }
}

int main() {
    vector<string> in, out;
    map<string,pair<set<string>,int>> global_dict;

    reading_file(global_dict);
    read_in(in);

    error_correct(in, out, global_dict);
    //cout << endl;
    for (string s:out) cout << s << endl;
    return 0;
}
