#include <cstdlib>
#include <iostream>
#include <string>

#include "parser.h"

using namespace std;

int main() {
    // Ввод текста из стандартного потока ввода
    // (при вводе текста из консоли в конце нужно нажать Ctrl-D).
    string text( (istreambuf_iterator<char>(cin)),
                (istreambuf_iterator<char>()) );
    try {
        parser::parse(text);
    } catch (parser::coord pos) {
        cout << "syntax error at " << pos.line << ", " << pos.col << endl;
        return 1;
    }
    
    cout << "success" << endl;
    return 0;
}
