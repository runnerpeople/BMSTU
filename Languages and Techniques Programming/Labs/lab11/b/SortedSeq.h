#ifndef SORTEDSEQ_H
#define SORTEDSEQ_H
#include <vector>
#include <algorithm>

using namespace std;

template <typename Symbol>
class SortedSeq{
public:
    vector<Symbol> seq;
    SortedSeq& operator+= (const SortedSeq &obj){
        for(auto s : obj.seq) {
            if (find(this->seq.begin(), this->seq.end(), ~s) != this->seq.end())
                while (find(this->seq.begin(), this->seq.end(), ~s) != this->seq.end())
                    this->seq.erase((find(this->seq.begin(), this->seq.end(), ~s)));
            else
                this->seq.push_back(s);
        }
    };
    SortedSeq& operator~ (){
        vector<Symbol> temp;
        for(auto elem : this->seq)
            temp.push_back(~elem);
        this->seq = temp;
    };
    SortedSeq() {};
    SortedSeq(Symbol s)
    {
        this->seq.push_back(s);
    }

    bool operator== (const SortedSeq &obj) const{
        return (this->seq == obj.seq);
    };
    bool operator!= (const SortedSeq &obj) const{
        return !(this->seq == obj.seq);
    };
    bool operator> (const SortedSeq &obj) const{
       if (this->seq.size() > obj.seq.size())
           return 1;
        if (this->seq.size() < obj.seq.size())
            return 0;
        for (int i = 0; i < obj.seq.size(); ++i) {
            if (this->seq[i] > obj.seq[i])
                return 1;
            if (this->seq[i] < obj.seq[i])
                return 0;
        }
        return 0;
    };

    bool operator< (const SortedSeq &obj) const {
        if (this->seq.size() < obj.seq.size())
            return 1;
        if (this->seq.size() > obj.seq.size())
            return 0;
        for (int i = 0; i > obj.seq.size(); ++i) {
            if (this->seq[i] < obj.seq[i])
                return 1;
            if (this->seq[i] > obj.seq[i])
                return 0;
        }
        return 0;
    }
    bool operator>= (const SortedSeq &obj) const{
        return (this->seq > obj.seq || this->seq == obj.seq);
    };
    bool operator<= (const SortedSeq &obj) const{
        return (this->seq < obj.seq || this->seq == obj.seq);
    };

};
#endif /*SORTEDSEQ_H*/
