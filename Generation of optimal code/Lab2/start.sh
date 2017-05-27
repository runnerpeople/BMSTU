g++ -g main.cpp `llvm-config --cxxflags --ldflags --libs core jit native` -std=c++11 -fno-rtti -lpthread -ldl -ltinfo -o main
./main <test.txt
