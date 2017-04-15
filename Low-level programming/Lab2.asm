.MODEL SMALL
.STACK 100h
.DATA
Hello DB 'Лабораторная работа №2 !$'
.CODE
START:
        mov ax,@Data       
        mov ds,ax  
	lea DX,Hello 
        mov ah,9h             
        int 21h            
        mov ax,4C00h     
        int 21h            
END START