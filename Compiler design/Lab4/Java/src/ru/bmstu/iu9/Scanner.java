package ru.bmstu.iu9;


public class Scanner {
    public final String program;

    private Compiler compiler;
    private Position cur;

    public Scanner(String program, Compiler compiler) {
        this.compiler = compiler;
        this.program = program;
        cur = new Position(program);
    }

    public Token nextToken() {
        while (!cur.isEOF()) {
            while (cur.isWhitespace())
                cur = cur.next();
            Token token;
            switch (cur.getCode()) {
                case ':':
                    if (cur.next().getCode() == ':')
                        token = readComment(cur);
                    else
                        token = readLabel(cur);
                    break;
                case '\"':
                    token = readWord(cur,true);
                    break;
                default:
                    token = readWord(cur,false);
                    break;
            }
            if (token == null || token.tag == DomainTag.UNKNOWN) {
                compiler.addMessage(true,cur,"Token: unrecognized token");
                cur = cur.next();
            }
            else {
                cur = token.coords.following;
                return token;
            }
        }
        return new UnknownToken(DomainTag.END_OF_PROGRAM,cur,cur);
    }

    private Token readComment(Position cur) {
        Position p = cur.next().next();
        StringBuilder sb = new StringBuilder();
        while (true) {
            if (p.isNewLine())
                break;
            else {
                sb.append(Character.toChars(p.getCode()));
                p = p.next();
            }
        }
        return new CommentToken(sb.toString(),cur,p.next());
    }

    private Token readLabel(Position cur) {
        StringBuilder sb = new StringBuilder();
        Position p = cur.next();
        if (p.isLetter() || p.isDecimalDigit()) {
            while (p.isLetter() || p.isDecimalDigit()) {
                sb.append(Character.toChars(p.getCode()));
                p = p.next();
            }
            return new LabelToken(sb.toString(), cur, p.next());
        }
        compiler.addMessage(false,cur,"Label must start only with numbers or letters");
        return new LabelToken(sb.toString(),cur,p.next());
    }

    private Token readWord(Position cur,boolean isQuote) {
        if (!isQuote) {
            Position p = cur.next();
            StringBuilder sb = new StringBuilder();
            sb.append(Character.toChars(cur.getCode()));
            while (!p.isEOF() && !p.isWhitespace()) {
                sb.append(Character.toChars(p.getCode()));
                p = p.next();
            }
            return new WordToken(sb.toString(),cur,p);
        }
        else {
            Position p = cur.next();
            StringBuilder sb = new StringBuilder();
            while (!p.isEOF() && !(p.getCode()=='\"')) {
                if (p.getCode() == '\r' || p.getCode() == '\n')
                    break;
                else {
                    sb.append(Character.toChars(p.getCode()));
                    p = p.next();
                }
            }
            if (p.getCode() == '\"')
                return new WordToken(sb.toString(),cur,p.next());
            else if (p.getCode() == '\r') {
                compiler.addMessage(false,p,"words don't contains whitespaces, \" expected");
                return new WordToken(sb.toString(),cur,p);
            }
            else {
                compiler.addMessage(false,p,"end of program found, \" expected");
                return new WordToken(sb.toString(),cur,p);
            }
        }
    }
}
