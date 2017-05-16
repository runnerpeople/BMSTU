procedure GetSymbol;
var k,s:integer;
    StrEnd,InStr:boolean;
    LastChar:char;
    num:integer;

begin
    while (CurrentChar>#0) and (CurrentChar<=' ') do begin
        ReadChar;
    end;
    if (('a'<=CurrentChar) and (CurrentChar<='z')) or (('A'<=CurrentChar) and (CurrentChar<='Z')) then begin
        k:=0;
        while ((('a'<=CurrentChar) and (CurrentChar<='z')) or
        (('A'<=CurrentChar) and (CurrentChar<='Z')) or
        (('0'<=CurrentChar) and (CurrentChar<='9'))) or
        (CurrentChar='_') do begin
            if k<>MaximalAlfa then begin
                k:=k+1;
                if ('a'<=CurrentChar) and (CurrentChar<='z') then begin
                    CurrentChar:=chr(ord(CurrentChar)-32);
                end;
                CurrentIdentifer[k]:=CurrentChar;
            end;
            ReadChar;
        end;
        while k<>MaximalAlfa do begin
            k:=k+1;
            CurrentIdentifer[k]:=' ';
        end;
        CurrentSymbol:=TokIdent;
        s:=SymBEGIN;
        while s<=SymPROC do begin
            if StringCompare(Keywords[s],CurrentIdentifer) then begin
                CurrentSymbol:=s;
            end;
            s:=s+1;
        end;
    // adding recognition hex number (such as 0x12AB) //
    end else if (('0'<=CurrentChar) and (CurrentChar<='9')) or (CurrentChar='$') then begin
        LastChar:=CurrentChar;
        ReadChar;
        if (CurrentChar='x') or (CurrentChar='X') then begin
            CurrentChar:='$';
            CurrentNumber:=ReadNumber;
        end else if (LastChar='$') then begin
            num:=0;
            while (('0'<=CurrentChar) and (CurrentChar<='9')) or
            (('a'<=CurrentChar) and (CurrentChar<='f')) or
            (('A'<=CurrentChar) and (CurrentChar<='F')) do begin
                if ('0'<=CurrentChar) and (CurrentChar<='9') then begin
                    num:=(num*16)+(ord(CurrentChar)-ord('0'));
                end else if ('a'<=CurrentChar) and (CurrentChar<='f') then begin
                    num:=(num*16)+(ord(CurrentChar)-ord('a')+10);
                end else if ('A'<=CurrentChar) and (CurrentChar<='F') then begin
                    num:=(num*16)+(ord(CurrentChar)-ord('A')+10);
                end;
                ReadChar;
            end;
            CurrentNumber:=num;
        end else begin
            num:=(ord(LastChar)-ord('0'));
            while ('0'<=CurrentChar) and (CurrentChar<='9') do begin
                num:=(num*10)+(ord(CurrentChar)-ord('0'));
                ReadChar;
            end;
            CurrentNumber:=num;
        end;
        CurrentSymbol:=TokNumber;
    // --------------    the end --------------------- //
    end else if CurrentChar=':' then begin
        ReadChar;
        if CurrentChar='=' then begin
            ReadChar;
            CurrentSymbol:=TokAssign;
        end else begin
            CurrentSymbol:=TokColon;
        end;
    end else if CurrentChar='>' then begin
        ReadChar;
        if CurrentChar='=' then begin
            ReadChar;
            CurrentSymbol:=TokGEq;
        end else begin
            CurrentSymbol:=TokGtr;
        end;
    end else if CurrentChar='<' then begin
        ReadChar;
        if CurrentChar='=' then begin
            ReadChar;
            CurrentSymbol:=TokLEq;
        end else if CurrentChar='>' then begin
            ReadChar;
            CurrentSymbol:=TokNEq;
        end else begin
            CurrentSymbol:=TokLss;
        end;
    end else if CurrentChar='.' then begin
        ReadChar;
        if CurrentChar='.' then begin
            ReadChar;
            CurrentSymbol:=TokColon;
        end else begin
            CurrentSymbol:=TokPeriod
        end;
    end else if (CurrentChar='''') or (CurrentChar='#') then begin
        CurrentStringLength:=0;
        StrEnd:=false;
        InStr:=false;
        CurrentSymbol:=TokStrC;
        while not StrEnd do begin
            if InStr then begin
                if CurrentChar='''' then begin
                    ReadChar;
                    if CurrentChar='''' then begin
                        if CurrentStringLength=MaximalStringLength then begin
                            Error(147);
                        end;
                        CurrentStringLength:=CurrentStringLength+1;
                        CurrentString[CurrentStringLength]:=CurrentChar;
                        ReadChar;
                    end else begin
                        InStr:=false;
                    end;
                end else if (CurrentChar=#13) or (CurrentChar=#10) then begin
                    Error(100);
                    StrEnd:=true;
                end else begin
                    if CurrentStringLength=MaximalStringLength then begin
                        Error(147);
                    end;
                    CurrentStringLength:=CurrentStringLength+1;
                    CurrentString[CurrentStringLength]:=CurrentChar;
                    ReadChar;
                end;
            end else begin
                if CurrentChar='''' then begin
                    InStr:=true;
                    ReadChar;
                end else if CurrentChar='#' then begin
                    ReadChar;
                    if CurrentStringLength=MaximalStringLength then begin
                        Error(147);
                    end;
                    CurrentStringLength:=CurrentStringLength+1;
                    CurrentString[CurrentStringLength]:=chr(ReadNumber);
                end else begin
                    StrEnd:=true;
                end;
            end;
        end;
        if CurrentStringLength=0 then begin
            Error(101);
        end;
    end else if CurrentChar='+' then begin
        ReadChar;
        CurrentSymbol:=TokPlus;
    end else if CurrentChar='-' then begin
        ReadChar;
        CurrentSymbol:=TokMinus;
    end else if CurrentChar='*' then begin
        ReadChar;
        CurrentSymbol:=TokMul;
    end else if CurrentChar='(' then begin
        ReadChar;
        if CurrentChar='*' then begin
            ReadChar;
            LastChar:='-';
            while (CurrentChar<>#0) and not ((CurrentChar=')') and (LastChar='*')) do begin
                LastChar:=CurrentChar;
                ReadChar;
            end;
            ReadChar;
            GetSymbol;
        end else begin
            CurrentSymbol:=TokLParent;
        end;
    end else if CurrentChar=')' then begin
        ReadChar;
        CurrentSymbol:=TokRParent;
    end else if CurrentChar='[' then begin
        ReadChar;
        CurrentSymbol:=TokLBracket;
    end else if CurrentChar=']' then begin
        ReadChar;
        CurrentSymbol:=TokRBracket;
    end else if CurrentChar='=' then begin
        ReadChar;
        CurrentSymbol:=TokEql;
    end else if CurrentChar=',' then begin
        ReadChar;
        CurrentSymbol:=TokComma;
    end else if CurrentChar=';' then begin
        ReadChar;
        CurrentSymbol:=TokSemi;
    end else if CurrentChar='{' then begin
        while (CurrentChar<>'}') and (CurrentChar<>#0) do begin
            ReadChar;
        end;
        ReadChar;
        GetSymbol;
    end else if CurrentChar='/' then begin
        ReadChar;
        if CurrentChar='/' then begin
            repeat
                ReadChar;
            until (CurrentChar=#10) or (CurrentChar=#0);
            GetSymbol;
        end else begin
            Error(102);
        end;
    end else begin
        Error(102);
    end;
end;