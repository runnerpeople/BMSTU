.MODEL SMALL
.STACK 100h
.DATA
Message DB '????!',13,10,'$'
.CODE
START:
        mov dx,@data      
        mov ds,ax            
        mov ah,9h          
        mov dx,OFFSET Message      
        int 21h            
        mov ah,4ch         
        int 21h            
END START


