.model tiny
.stack 100h
.data
	outStr db '0000$'
.code
	translByte proc
	push ax
	push ax
	shr al,4
	cmp al,9
	ja greater10
	mov byte ptr [bx],'0'
	add [bx],al
	jmp next4Bit
	greater10:
		mov byte ptr [bx],'A'
		sub al,10
		add [bx],al
	next4Bit:
		pop ax
		and al,0Fh
		cmp al,9
		ja _greater10
		mov byte ptr [bx+1],'0'
		add [bx+1],al
		jmp exitByteProc
	_greater10:
		mov byte ptr [bx+1],'A'
		sub al,10
		add [bx+1],al
	exitByteProc:
		pop ax
		ret
	translByte endp
	
	translWord proc
		push ax
		push ax
		shr ax,8
		call translByte
		pop ax
		add bx,2
		call translByte
		sub bx,2
		pop ax
		ret
	translWord endp
	
	start:
		mov ax,@data
		mov ds,ax
		mov bx,offset outStr
		mov ax,60000
		call translWord
		mov ah,9
		mov dx,offset outStr
		int 21h
		mov ax,4c00h
		int 21h
	end start