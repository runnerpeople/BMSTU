.model tiny
.stack 100h
.data
	ArrayA db 05,10,06,44,20,32,05,11,46,0
	ArrayB db 35,10,15,44,20,02,65,10,46,0
	Difference db 10 dup (0)
	NumOfDiff dw 0
	NumOfEqual dw 0
	SumOfEqual dw 0
	SumOfDiff dw 0
.code
start:
	mov ax,@data
	mov ds,ax
	push ds
	pop es
	mov di,offset Difference
	mov cx,10
	mov al,'Y'
	cld
	rep stosb
	mov si,offset ArrayA
	mov di,offset ArrayB
	mov bx,offset Difference
	mov cx,10
findDE:
	cmpsb
	jne NotEqual
	inc NumOfEqual
	inc bx
	dec di
	dec si
	mov al,byte ptr ds:[si]
	cbw
	add SumOfEqual, ax
	inc si
	inc di
	jmp NextElement
NotEqual:
	inc NumOfDiff
	mov byte ptr ds:[bx],'N'
	inc bx
	dec di
	dec si
	mov al,byte ptr ds:[si]
	cbw
	add SumOfDiff,ax
	mov al,byte ptr ds:[di]
	cbw
	add SumOfDiff,ax
	inc si
	inc di
NextElement:
	loop findDE
	mov ax,4c00h
	int 21h
end start