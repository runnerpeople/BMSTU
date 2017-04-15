#include <cctype>
#include "parser.h"

namespace parser {
    
    // Теги лексем
    enum tag_t { NUMBER, LBRACKET, RBRACKET, END, CARET, OF, INTEGER,REAL, POINTS,ARRAY, COMMA};
    
    // Контекст парсера
    struct context {
        std::string text;  // текст для анализа
        coord cur;         // координаты текущей позиции в тексте
        coord next;        // координаты следующей позиции в тексте
        coord start;       // координаты начала текущей лексемы
        tag_t tag;         // тег текущей лексемы
    };
    
    // next_char считывает следующий символ из текста и возвращает его код.
    // Если достигнут конец текста, next_char возвращает -1.
    int next_char(context& ctx) {
        ctx.cur = ctx.next;
        if (ctx.next.offs == ctx.text.length()) return -1;
        char c = ctx.text[ctx.next.offs++];
        if (c == '\n') {
            ctx.next.line++;
            ctx.next.col = 1;
        } else {
            ctx.next.col++;
        }
        return c;
    }
    
    // next_token распознаёт следующую лексему в тексте.
    void next_token(context& ctx) throw(coord) {
        int c;
        while (isspace(c = next_char(ctx)));
        ctx.start = ctx.cur;
        switch (c) {
            case -1: ctx.tag = END; break;
            case '[': ctx.tag = LBRACKET; break;
            case ']': ctx.tag = RBRACKET; break;
            case '^': ctx.tag = CARET; break;
            case ',': ctx.tag = COMMA; break;
            default:
                if (isalpha(c)) {
                    if(ctx.text[ctx.cur.offs]=='a') {
                        std::string str2=ctx.text.substr(ctx.cur.offs,5);
                        if(str2=="array") {
                            ctx.tag = ARRAY;
                            ctx.next.offs+=4;
                            ctx.next.col+=4;
                        }
                        else throw ctx.cur;
                    }
                    else if(ctx.text[ctx.cur.offs]=='i') {
                        std::string str2=ctx.text.substr(ctx.cur.offs,7);
                        if(str2=="integer") {
                            ctx.tag = INTEGER;
                            ctx.next.offs+=6;
                            ctx.next.col+=6;
                        }
                        else throw ctx.cur;
                    }
                    else if(ctx.text[ctx.cur.offs]=='r') {
                        std::string str2=ctx.text.substr(ctx.cur.offs,4);
                        if(str2=="real") {
                            ctx.tag = REAL;
                            ctx.next.offs+=3;
                            ctx.next.col+=3;
                        }
                        else throw ctx.cur;
                    }
                    else if(ctx.text[ctx.cur.offs]=='o') {
                        std::string str2=ctx.text.substr(ctx.cur.offs,2);
                        if(str2=="of") {
                            ctx.tag = OF;
                            ctx.next.offs+=1;
                            ctx.next.col+=1;
                        }
                        else throw ctx.cur;
                    }
                } else if (isdigit(c)) {
                    while (isdigit(c = next_char(ctx)));
                    ctx.tag = NUMBER;
                    ctx.next.offs-=1;
                    ctx.next.col-=1;
                } else if (ctx.text[ctx.cur.offs]=='.') {
                    std::string str2=ctx.text.substr(ctx.cur.offs,2);
                    if(str2=="..") {
                        ctx.tag=POINTS;
                        ctx.next.offs+=1;
                        ctx.next.col+=1;
                    }
                }
                else throw ctx.cur;
        }
    }
    
    void parse_type(context&) throw(coord);
    void parse_dim(context&) throw(coord);
    void parse_rng(context&) throw(coord);
    
    void parse(std::string text) throw(coord) {
        context ctx { text };
        ctx.next = coord{ 0, 1, 1 };
        next_token(ctx);
        parse_type(ctx);
        if (ctx.tag != END) throw ctx.start;
    }
    
    void parse_type(context& ctx) throw(coord) {
        if (ctx.tag == ARRAY) {
            next_token(ctx);
            if(ctx.tag==LBRACKET) {
                next_token(ctx);
                parse_dim(ctx);
            }
            else throw ctx.start;
            if(ctx.tag==RBRACKET)
                next_token(ctx);
                else throw ctx.start;
            if(ctx.tag==OF) {
                next_token(ctx);
                parse_type(ctx);
            }
            else throw ctx.start;
        }
        else if (ctx.tag == CARET) {
            next_token(ctx);
            parse_type(ctx);
        }
        else if (ctx.tag== INTEGER) {
            next_token(ctx);
        }
        else if(ctx.tag == REAL) {
            next_token(ctx);
        }
        else throw ctx.start;
    }
    
    void parse_dim(context& ctx) throw(coord) {
        parse_rng(ctx);
        if (ctx.tag == COMMA) {
            next_token(ctx);
            parse_dim(ctx);
        }
        
    }
    
    void parse_rng(context& ctx) throw(coord) {
        if (ctx.tag == NUMBER) {
            next_token(ctx);
            if (ctx.tag == POINTS)
                next_token(ctx);
                else throw ctx.start;
            if (ctx.tag == NUMBER)
                next_token(ctx);
                else throw ctx.start;        
        }
    }
    
}
