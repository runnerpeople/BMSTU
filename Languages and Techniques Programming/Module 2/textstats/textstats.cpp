#include <string>
#include <vector>
#include <unordered_set>
#include <map>
#include <set>
#include <algorithm>

#include "textstats.hpp"

void get_tokens(const string &s, const unordered_set<char> &delimiters, vector<string> &tokens) {   
        string help="";
        for(auto it=s.begin();it!=s.end();++it) {
                auto got = delimiters.find(*it);
                if (got == delimiters.end())
                        help = help + *it; 
                else {
                        if (help != "") {
                                transform(help.begin(),help.end(),help.begin(),::tolower);
                                tokens.push_back(help);
                                help="";
                        }          
                }        
        }
        if (help!="") {
                transform(help.begin(),help.end(),help.begin(),::tolower);
                tokens.push_back(help);
                help="";
        }
}

void get_type_freq(const vector<string> &tokens, map<string, int> &freqdi) {
        for(auto it=tokens.begin();it!=tokens.end();++it) {
                auto search = freqdi.find(*it);
                if (search==freqdi.end()) {
                        freqdi.emplace(*it,1);
                }
                else {
                        search->second++;
                }
        }
}

void get_types(const vector<string> &tokens, vector<string> &wtypes) {
        set<string> buf;
        for(auto it=tokens.begin();it!=tokens.end();++it) {
                buf.insert(*it);
        }
        for(auto it2=buf.begin();it2!=buf.end();++it2){
                wtypes.push_back(*it2);
        }
}

void get_x_length_words(const vector<string> &wtypes, int x, vector<string> &words) {
        for(auto it=wtypes.begin();it!=wtypes.end();++it) {
                if ((*it).length() >= x)
                        words.push_back(*it);
        }
}

void get_x_freq_words(const map<string, int> &freqdi, int x, vector<string> &words) {
        for(auto it=freqdi.begin();it!=freqdi.end();++it) {
                if(it->second >= x)
                        words.push_back(it->first);
        }
}

void get_words_by_length_dict(const vector<string> &wtypes, map<int, vector<string> > &lengthdi) {
        for(auto it=wtypes.begin();it!=wtypes.end();++it) {
                auto search = lengthdi.find((*it).length());
                if (search==lengthdi.end()) {
                        vector<string> buf;
                        buf.push_back(*it);
                        lengthdi.emplace((*it).size(),buf);
                }
                else
                        search->second.push_back(*it);               
        }
}
