.model small
.stack 100h
.data
a db ?
b db ?
c db ?
d db ?
x dw ?
.code
  start:
	mov ax, @data
	mov ds, ax
	mov a,3
	mov b,4
	mov c,2
	mov d,5
	mov al,2
	mul a
	mov cx,ax
	mov al,b
	mul c
	add ax,cx
	mov ch,d
	sub ch,3
	div ch
	mov x,ax 
	mov ah,4ch
	int 21h
end start

	
	
	