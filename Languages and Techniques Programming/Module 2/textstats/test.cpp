#include <iostream>
#include <iterator>
 
#include "textstats.hpp"

// Множество разделителей слов в тексте.
const unordered_set<char> delimiters { 
	'~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_',
	'+', '-', '=', '`', '{', '}', '[', ']', '|', '\\', ':', ';',
	'\"', '\'', ',', '.', '/', '<', '>', '?', ' ', '\t', '\n'
};

int main()
{
	// Ввод текста из стандартного потока ввода
	// (при вводе текста из консоли в конце нужно нажать Ctrl-D).
        string text( (istreambuf_iterator<char>(cin)),
		     (istreambuf_iterator<char>()) );
	
	// Итератор для вывода слов через пробел.
	ostream_iterator<string> owords(cout, " ");

	// Разбиение текста на слова.
	vector<string> tokens;
	get_tokens(text, delimiters, tokens);
	copy(tokens.begin(), tokens.end(), owords);
	cout << endl;

	// Составление частотного словаря.
	map<string, int> freqdi;
	get_type_freq(tokens, freqdi);
	for (auto p : freqdi) {
		cout << "(" << p.first << " => " << p.second << ") ";
	}
	cout << endl;

	// Составление списка уникальных слов.
	vector<string> wtypes;
	get_types(tokens, wtypes);
	copy(wtypes.begin(), wtypes.end(), owords);
	cout << endl;

	// Вычисление средней длины уникальных слов.
	int sum = 0;
	for (auto w : wtypes) sum += w.length();
	int med = tokens.size() > 0 ? (sum + wtypes.size() - 1)/wtypes.size() : 0;

	// Формирование списка слов, длина которых не ниже средней.
	vector<string> long_wtypes;
	get_x_length_words(wtypes, med, long_wtypes);
	copy(long_wtypes.begin(), long_wtypes.end(), owords);
	cout << endl;

	// Формирование списка слов, встречающихся больше одного раза.
	vector<string> multi_wtypes;
	get_x_freq_words(freqdi, 2, multi_wtypes);
	copy(multi_wtypes.begin(), multi_wtypes.end(), owords);
	cout << endl;

	// Составление словаря распределения слов по длинам.
	map<int, vector<string> > lengthdi;
	get_words_by_length_dict(wtypes, lengthdi);
	for (auto p : lengthdi) {
		cout << p.first << " => ";
		copy(p.second.begin(), p.second.end(), owords);
		cout << endl;
	}

	return 0;
}
