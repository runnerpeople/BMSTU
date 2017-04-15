.model tiny
.stack 100h
.data
	ArrayA db "TEST"
	ArrayB db "MESR"
	NumOfDiff db 0
	NumOfEqual db 0
	Diffstr db "Different letters",0ah,24h
	Equalstr db "Equal letters",0ah,24h
.code
start:
	mov ax,@data
	mov ds,ax
	push ds
	pop es
	std
	mov si,offset ArrayA + 3
	mov di,offset ArrayB + 3
	mov cx,4
findDE:
	cmpsb
	jne NotEqual
	inc NumOfEqual
	jmp NextElement
NotEqual:
	inc NumOfDiff
NextElement:
	loop findDE
	jmp Print
Print:
	lea dx,Diffstr
    mov ah,9h    
    int 21h
	xor ax,ax
    mov al,NumOfDiff
    aam 
    add ax,3030h
    mov dl,ah 
    mov dh,al 
	mov ah,02
    int 21h
    mov dl,dh
    int 21h
	lea dx,Equalstr
    mov ah,9h    
    int 21h
	xor ax,ax
    mov al,NumOfEqual
    aam
    add ax,3030h
    mov dl,ah
    mov dh,al
    mov ah,02
    int 21h
    mov dl,dh
    int 21h
	mov ax,4c00h
    int 21h
end start