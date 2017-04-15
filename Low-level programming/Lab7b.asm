.MODEL TINY
.STACK 200h
.DATA
NEWLINE db 13,10,'$'   ;Перевод каретки на новую строку, чтобы вывод не затирал ввод
INPUT_BUFFER db 7, 0 ;Первый байт - размер буфера ;Второй байт заполняется при вводе строки ее длиной 
IN_STR db 8 dup('$')            ;Следующие 9 байт - сам буфер, заполняется символом конца строки, т.к. функция считывания его не ставит
OUT_STR db 8 dup('$') 
.CODE
reverse proc
	xor cx, cx
	mov al, byte ptr [INPUT_BUFFER+1]  ;Длина строки
	mov cl, al         ;Положить длину строки в регистр-счетчик
	mov si, offset IN_STR  ;source index
	mov di, offset OUT_STR
	add si, cx
	dec si ;Теперь si указывает на последний элемент строки
	circle:
		mov al, byte ptr [si]
		mov byte ptr [di], al
		dec si
		inc di
		loop circle  
	ret
reverse endp

Start:
	mov ax, @Data
	mov ds, ax
	call reverse
	mov ah, 9
	mov dx, offset OUT_STR
	int 21h
	mov dx, offset NEWLINE ;Сделать перевод каретки
	mov ah, 9h
	int 21h
	mov ax, 4c00h
	int 21h
END start