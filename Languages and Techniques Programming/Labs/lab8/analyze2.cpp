#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <dirent.h>
#include <fstream>
#include <iostream>
#include <string>
#include <cstdlib>
#include <vector>
#include <set>

using namespace std;

int main(int argc, char **argv) {
    vector<string> a;
    set<string> s;
    struct stat buf;
    struct dirent *help;
    int count=0;
    DIR* catalog = opendir(argv[1]);
    if(catalog==NULL)
        cout << "Error" << endl;
    else {
        help = readdir(catalog);
        while(help!=NULL) {
            int pos = string(help->d_name).find(".html");
            if (pos==-1 || string(help->d_name).size()<5 || string(help->d_name).substr(string(help->d_name).size()-5,5)!=".html") {
                help=readdir(catalog);
                continue;
            }
            else {
                string name = argv[1] + (string)"/" + (string)help->d_name;
                const char* name_plus = name.c_str();
                lstat(name_plus,&buf);
                int status = S_ISREG(buf.st_mode);
                if (status == 1)
                    a.push_back(name);
                help=readdir(catalog);
            }
        }
        for(int i=0;i<a.size();i++) {
            // cout << a[i] << endl;
            ifstream f(a[i]);
            string line;
            int line_number = 0;
            while(getline(f,line)) {
                line_number++;
                int pos = line.find("<a href=\"");
                if(pos==-1)
                    continue;
                else {
                    pos +=9;
                    string end = "\">";
                    string end2 = "\" ";
                    int pos_end = line.find(end);
                    int pos_end2 = line.find(end2);
                    
                    // if (line.substr(pos,pos_end2-pos).find("http://")!=0 && line.substr(pos,pos_end-pos).find("http://")!=0)
                    // continue;
                    // else {
                    if (pos_end<pos_end2 || pos_end2 == -1) {
                        if (line.substr(pos,pos_end-pos).find("\"")==-1)
                            s.insert(line.substr(pos,pos_end-pos));
                        else 
                            s.insert(line.substr(pos,line.substr(pos,pos_end-pos).find("\"")));
                    }
                    else {
                        if (line.substr(pos,pos_end2-pos).find("\"")==-1)
                            s.insert(line.substr(pos,pos_end2-pos));
                        else
                            s.insert(line.substr(pos,line.substr(pos,pos_end2-pos).find("\"")));
                    }
                    // }
                }
            }
        }
        ofstream f(string(argv[1]) + (string)"/" + "links.txt");
        for(string x : s) {
            if(x!="")
                f << x << endl;
        }
        f.close();
        cout << "Success" << endl;
    }
    closedir(catalog);
}
