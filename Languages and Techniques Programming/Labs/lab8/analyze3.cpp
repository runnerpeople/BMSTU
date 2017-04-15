#include <sys/stat.h>
#include <sys/types.h>
#include <dirent.h>
#include <string>
#include <iostream>
#include <vector>
#include <fstream>
#include <iomanip>
#include <algorithm>
using namespace std;

int main(int argc,char **argv) {
    string separator = "/";
    vector<string> text;
    vector<string> files;
    vector <string> dex;
    struct stat buf;
    struct dirent *help;
    vector <string> ln;
    DIR* catalog=opendir(argv[1]);
    if (catalog == NULL) {
        cout << "Exception in open file: " << string(argv[1]) << endl;
        closedir(catalog);
        return 1;
    }
    else {
        help=readdir(catalog);
        while(help!=NULL) {
            if (string(help->d_name).size()<4) {
                help = readdir(catalog);
                continue;
            }
            else {
                string txt = string(help->d_name).substr(string(help->d_name).size()-4,4);
                if(txt!=".txt") {
                    help = readdir(catalog);
                    continue;
                }
                else {
                    string path = argv[1] + separator + (string)help->d_name;
                    lstat(path.c_str(),&buf);
                    int status = S_ISREG(buf.st_mode);
                    if (status == 1){
                        files.push_back(path);
                        dex.push_back((string)help->d_name);}
                    help = readdir(catalog);
                }
            }
        }
        for (int k=0;k<files.size();k++) {
            ifstream f(files[k]);
            string line, l = "";
            while (getline(f, line)) {
                for (int i=0; i<line.length()-3; i++) {
                    if (line.substr(i,1) ==" "){
                        l = l + line.substr (i+1, 1);
                        for(int j=i+1; j<line.length(); j++) {
                            if (line.substr(j,1)==" ") {
                                vector <string> v;
                                for (int k = i+2; k < j-1; k++)
                                    v.push_back(line.substr(k, 1));
                                random_shuffle(v.begin(), v.end());
                                for (int k = 0; k < v.size(); k++)
                                    l = l + v[k];
                                l = l + line.substr(j-1, 1) + " ";
                                break;
                            }
                        }
                    }
                }
                ln.push_back(l);
                l = "";
            }
            f.close();
            ofstream g (dex[k]);
            for (int q = 0; q < ln.size(); q++)
                g << ln[q] << endl;
            g.close();
            ln.clear();
        }
        
        int closedir (DIR *dirp );
    }
    return 0;
}
