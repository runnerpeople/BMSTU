.MODEL SMALL
.STACK 200h
.DATA
NEWLINE db 13,10,'$'   ;Перевод каретки на новую строку, чтобы вывод не затирал ввод
;Первый байт - размер буфера 
;Второй байт заполняется при вводе строки ее длиной 
;Следующие 255 байт - сам буфер
INPUT_BUFFER db 9 ;Первый байт - размер буфера 
db 0                         ;Второй байт заполняется при вводе строки ее длиной 
db 10 dup($)            ;Следующие 255 байт - сам буфер, заполняется символом конца строки, т.к. функция считывания его не ставит
.CODE
Start:
mov ax, @Data
mov ds, ax

mov ah, 0Ah   ;Считать строку в буфер
mov dx, offset INPUT_BUFFER   
int 21h

mov dx, offset NEWLINE ;Сделать перевод каретки , иначе ввод затрется выводом
mov ah, 9h
int 21h

xor ax, ax        ;Подготовка регистра для дальнейшей работы
mov al, byte ptr [INPUT_BUFFER+1]  ;Длина строки
mov cl, al         ;Положить длину строки в регистр-счетчик

mov si, offset INPUT_BUFFER + 2     ;Положить начало строки в регистр-приемник
xor dx , dx       ;Подготовка регистра для дальнейшей работы, так как нам нужен будет dl
;-----------------
Circle:
cmp cl, 0
jle next ;условный переход - не выполнять тело цикла, если стало меньше или равно 0

mov dl, byte ptr[si]
;проверка на то, что буква не равна a
cmp dl, 'a'
jne print_char ;jump if not equal
mov dl, 'b'

print_char:
mov ah, 2 ; print char function
int 21h

inc si ;перейти к след символу
dec cl ;уменьшить счетчик
jmp Circle  ;безусловный переход - вернуться в начало цикла к проверке условия
;-------------------
next:

;Завершение программы
mov ax, 4c00h;Лучше писать mov ax, 4C00h, так как код возврата лежит в al, и если там мусор - то выдвст ошибку или придется еще одной командой инициализировать явно
int 21h
END start


; xor ax, ax
; mov al, byte ptr [INPUT_BUFFER+1]
; mov dx, offset INPUT_BUFFER + 1
; mov si, ax
; add si, dx
; mov byte ptr [si], '$'