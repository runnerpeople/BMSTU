program Hello;

const Hex_number = 0x12ABcd; //0x12ABcd;

var
    hex_var : Integer;
    y: array[0..3] of Integer;

begin
    WriteLn(Hex_number);
    hex_var := 0xFFFF;
    WriteLn(hex_var);
    WriteLn('Hello, student! ', $AbCd1, ' ', 0xAbCd1);
end.