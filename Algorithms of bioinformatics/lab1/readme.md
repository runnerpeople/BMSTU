# Реализация алгоритмов выравнивания последовательностей

1. [Smith–Waterman](https://en.wikipedia.org/wiki/Smith–Waterman_algorithm)
2. [Needleman–Wunsch](https://en.wikipedia.org/wiki/Needleman–Wunsch_algorithm)
3. [Hirschberg](https://en.wikipedia.org/wiki/Hirschberg%27s_algorithm)

## Пример

   На вход подается имя файла *.fasta, который содержит последовательности, алгоритм выравнивания (под ключом alg
   может иметь значения sw - Smith-Waterman, nw - Needleman-Wunsch, hb - Hirschberg), значение MATCH/MISMATCH
   (под ключом m и mm соответственно) или же файл, содержащий таблицу этих значений (например, BLOSUM62 под ключом f),
   значение штрафа (под ключом g)
```python
# python3
python3 lab1.py input.fasta -alg sw -m 10 -mm 5 -g 5
```