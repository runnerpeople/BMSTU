.model small
.stack 100h
.data
DAN db 3Bh,20h,0Dh,32h,0A1h,24h,0A0h,0Dh,0,0A2h,20h
    db 0b0h,40h,24h,0E1h,0Dh,0,24h,3Bh,30h,0C0h,0Dh
    db 20h,97h,3Bh,83h,0,0A0h,20h,0D0h,27h,20h,0C6h
    db 91h,0,20h,0FEh,3Bh,90h,0,3Bh,24h,17h,20h,24h
ZERO db '‘ã¬¬  < 0',0ah,0dh,24h
GREAT db '0 < ‘ã¬¬  < 32',0ah,0dh,24h
LESS db '‘ã¬¬  >= 32',0ah,0dh,24h
.code
  start:
	mov ax,@data
 	mov ds,ax
	mov cx,3
	mov dx,64
	lea si,DAN
	dec si
  ext:
	push cx
	mov cx,dx
  loc:
	inc si
	cmp byte ptr [si],0
	loopne loc
  						;jne no
	mov dx,cx
	pop cx
	loop ext

	mov cx,5
	xor bx,bx
	clc
  minus:
	sub al,2
	jmp b
  a:
	inc si
	mov al,byte ptr [si]
	cmp al,0
	jnl minus
  b:
	cbw
	add bx,ax
	loop a
  jnz nz
  lea dx,ZERO
  mov ah,9h
  int 21h
  jmp fine
  nz:
	jg gr
	lea dx,LESS
	mov ah,9h
	int 21h
	jmp fine
  gr:
	lea dx,GREAT
	mov ah,9h
	int 21h
	jmp fine
  fine:
	mov ax,4c00h
	int 21h
end start

	