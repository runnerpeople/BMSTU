.model tiny
.stack 100h
.data   
words db 100,?,100 dup('$')
.code
	transWord proc
    push ax
	push dx
	push bx
	push cx
	mov  cx,100
	mov  bx,OFFSET words+1
		cycle:			
		cmp byte ptr [bx], 'a';  сравнить содержимое двух областей памяти, размером в один байт или в одно слово;
		jl lower;Если меньше
		jg increase; если больше
		lower:
			cmp byte ptr [bx], 91
			jg next
			cmp byte ptr [bx], 65
			jl next		
			add byte ptr [bx],32
			jmp next;
		increase:
			cmp byte ptr [bx], 'z'
			jg next
			sub byte ptr [bx],32
			jmp next
		next:
			inc	bx		
			loop cycle	
	pop cx
	pop bx
	pop dx
	pop ax
	ret
	transWord endp

	start:
		mov ax,@Data      
		mov ds,ax 
		mov es,ax	
		mov ah,0ah ;функция, считывающая из stdin в буфер  
		lea dx,words          
		int 21h 
		xor	ax,ax
		mov words+1,0ah
		call transWord
		xor dx,dx
		mov	ah,09h;Функция DOS 09h вывода на экран	
		mov	dx,OFFSET words+1
		int 21h 
		mov ah,4Ch         ;Функция 4Ch завершения программы 
		int 21h  
	end  start