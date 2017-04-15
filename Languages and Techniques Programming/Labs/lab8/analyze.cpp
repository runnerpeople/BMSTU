#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <dirent.h>
#include <fstream>
#include <iostream>
#include <string>
#include <cstdlib>
#include <vector>

using namespace std;

int main(int argc,char **argv) {
    string separator = "/";
    vector<string> text;
    vector<string> files;
    struct stat buf;
    struct dirent *help;
    DIR* catalog=opendir(argv[1]);
    if (catalog == NULL)
    {
        cout<< "Exception in open file: " << string(argv[1]) << endl;
        closedir(catalog);
        return 1;
    }
    else
    {
        help = readdir(catalog);
        while(help != NULL) {
            if (string(help->d_name).size() < 4)
            {
                help = readdir(catalog);
                continue;
            }
            else
            {
                string txt = string(help->d_name).substr(string(help->d_name).size() - 4, 4);
                if(txt != ".txt")
                {
                    help = readdir(catalog);
                    continue;
                }
                else
                {
                    string path = argv[1] + separator + (string)help->d_name;
                    lstat(path.c_str(), &buf);
                    int status = S_ISREG(buf.st_mode);
                    if (status == 1)
                    {
                        files.push_back(path);
                    }
                    help = readdir(catalog);
                }
            }
        }
        for (int i = 0; i < files.size(); i++)
        {
            ifstream f(files[i]);
            string line;
            while(getline(f, line))
            {
                text.push_back(line);
            }
            f.close();
            ofstream l(files[i]);
            for(int i = text.size() - 1; i >= 0; i--)
            {
                l << text[i] << endl;
            }
            l.close();
        }
        cout << "Success" << endl;
        closedir(catalog);
    } 
}
