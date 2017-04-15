SSEG		segment    para stack   'stack'
Db	1,2,3,4,5,6,7,8,9,128 dup(0Ah)
SSEG		ends
DSEG 	segment    para public   'data'
B_TAB	db	1Ah,2Bh,3Ch,4Dh,5Eh,6Fh,7Ah,8Bh
W_TAB   	dw	1A2Bh,3C4Dh,5E6Fh,7A8Bh
B_TAB1  	db	0Ah,8 dup(1)
W_TAB1 	dw	8 dup(1)
DSEG    	ends
ESEG		segment
W_TAB2	dw         11h,12h,13h,14h,15h,16h,17h,18h
ESEG		ends
CSEG		segment para public 'code'
PROG	proc	far
assume	ds:DSEG,cs:CSEG,ss:SSEG,es:ESEG
push	ds
mov	ax,0
push	ax
;инициализация сегментных регистров
mov	ax,dseg
mov	ds,ax
mov	ax,eseg
mov	es,ax
;непосредственная (операнд-источник)
mov	al,-3		;расширение знака
	mov	ax,3
	mov	B_TAB,-3
	mov	W_TAB,-3
	mov	ax,2A1Bh
;регистровая
mov	bl,al
mov	bh,al
sub	ax,bx
sub	ax,ax
;прямая
mov	ax,W_TAB
	mov	ax,W_TAB+3
	mov	ax,W_TAB+5
	mov	al,byte ptr W_TAB+6
	mov	al,B_TAB
	mov	al,B_TAB+2
	mov	ax,word ptr B_TAB
	mov	es:W_TAB2+4,ax
;косвенная
	mov	bx,offset B_TAB
	mov	si,offset B_TAB+1
	mov	di,offset B_TAB+2
	mov	dl,[bx]
	mov	dl,[si]
	mov	dl,[di]
	mov	ax,[di]
	mov	bp,bx
	mov	al,[bp]			;какой сегмент?
	mov	al,ds:[bp]
	mov	al,es:[bx]
	mov	ax,cs:[bx]
; базовая
	mov	ax,[bx]+2		;основная форма
	mov	ax,[bx]+4		;проверьте допустимость других
	mov	ax,[bx+2]
	mov	ax,[4+bx]
	mov	ax,2+[bx]
	mov	ax,4+[bx]
	mov	al,[bx]+2
	mov	bp,bx				;другой базовый регистр
	mov	ax,[bp+2]			;откуда содержимое ax?
	mov	ax,ds:[bp]+2			;попробуем переназначить 
;сегментный регистр
	mov	ax,ss:[bx+2]
;индексная
	mov	si,2				;загрузка индекса
	mov	ah,B_TAB[si]			;основная форма
	mov	al,[B_TAB+si]		;проверьте другие
	mov	bh,[si+B_TAB]
	mov	bl,[si]+B_TAB
	mov	bx,es:W_TAB2[si]
	mov	di,4
	mov	bl,byte ptr es:W_TAB2[di]
	mov	bl,B_TAB[si]
;базовая индексная
	mov	bx,offset B_TAB		;загрузка базы
	mov	al,3[bx][si]			;основная форма
	mov	ah,[bx+3][si]
	mov	al,[bx][si+2]
	mov	ah,[bx+si+2]
	mov	bp,bx
	mov	ah,3[bp][si]			;из какого сегмента?
	mov	ax,ds:3[bp][si]
	mov	ax,word ptr ds:2[bp][si]
	ret
PROG    endp
CSEG    ends
	end	PROG