#include <cctype>
#include "parser.h"
namespace parser {
    
    // Теги лексем
    enum tag_t { IDENT, NUMBER, STRING, LPAREN, RPAREN, END };
    
    // Контекст парсера
    struct context {
        std::string text;  // текст для анализа
        int c;             // текущий символ (-1, если конец текста)
        coord pos;         // координаты текущей позиции в тексте
        coord start;       // координаты начала текущей лексемы
        tag_t tag;         // тег текущей лексемы
    };
    
    // next_char считывает следующий символ из текста и возвращает его код.
    // Если достигнут конец текста, next_char возвращает -1.
    int next_char(context& ctx) {
        if (ctx.pos.offs == ctx.text.length()-1) return ctx.c = -1;
        ctx.c = ctx.text[++ctx.pos.offs];
        if (ctx.c == '\n') {
            ctx.pos.line++;
            ctx.pos.col = 0;
        } else {
            ctx.pos.col++;
        }
        return ctx.c;
    }
    
    // next_token распознаёт следующую лексему в тексте.
    void next_token(context& ctx) throw(coord) {
        while (isspace(ctx.c)) next_char(ctx);
            
            ctx.start = ctx.pos;
            switch (ctx.c) {
                case -1: ctx.tag = END; break;
                case '(': ctx.tag = LPAREN; next_char(ctx); break;
                case ')': ctx.tag = RPAREN; next_char(ctx); break;
                default:
                    if (isalpha(ctx.c)) {
                        while (isalnum(next_char(ctx)));
                        ctx.tag = IDENT;
                    } else if (isdigit(ctx.c)) {
                        while (isdigit(next_char(ctx)));
                        ctx.tag = NUMBER;
                    }
                    else if (isdigit(ctx.c))
                    {
                        while (isdigit(next_char(ctx)));
                        ctx.tag = STRING;
                    }
                    else {
                        throw ctx.pos;
                    }
            }
    }
    
    void parse_pal(context&) throw(coord);
    void parse_mid(context&) throw(coord);
    
    
    void parse(std::string text) throw(coord) {
        context ctx { text };
        ctx.pos = coord{ 0, 1, 1 };
        ctx.c = (ctx.text.length() == 0) ? -1 : ctx.text[0];
        next_token(ctx);
        parse_pal(ctx);
        if (ctx.tag != END) throw ctx.start;
    }
    
    void parse_pal(context& ctx) throw(coord)
    {
        switch (ctx.tag)
        {
            case LPAREN:
                next_token(ctx);
                parse_mid(ctx);
                if (ctx.tag != RPAREN) throw ctx.start;
                next_token(ctx);
                break;
            case IDENT:
                next_token(ctx);
                parse_pal(ctx);
                if (ctx.tag != IDENT) throw ctx.start;
                next_token(ctx);
                break;
            case NUMBER:
                next_token(ctx);
                parse_pal(ctx);
                if (ctx.tag != NUMBER) throw ctx.start;
                next_token(ctx);
                break;
            case STRING:
                next_token(ctx);
                parse_pal(ctx);
                if (ctx.tag != STRING) throw ctx.start;
                next_token(ctx);
                break;
                
        }
    }
    
    void parse_mid(context& ctx) throw(coord) {
        
        if (ctx.tag == IDENT || ctx.tag == NUMBER || ctx.tag == LPAREN || ctx.tag == STRING) {
            parse_pal(ctx);
            parse_mid(ctx);
        }
    }
}
