# Реализация алгоритма множественного выравнивания последовательностей

## Пример

   На вход подается имя файла *.fasta, который содержит последовательности, значение MATCH/MISMATCH
   (под ключом m и mm соответственно) или же файл, содержащий таблицу этих значений (например, BLOSUM62 под ключом f),
   значение штрафа (под ключом g)
```python
# python3
python3 lab4.py input.fasta -m 10 -mm 5 -g 5
```
