.model tiny
.stack 100h
.code
  start:
	mov ah,01001101b
	shr ah,1
	mov ah,01001101b
	shl ah,1
	mov ah,01001101b
	sar ah,1
	mov ah,01001101b
	ror ah,1
	mov ah,01001101b
	rol ah,1
	mov ax,4c00h
	int 21h
end start