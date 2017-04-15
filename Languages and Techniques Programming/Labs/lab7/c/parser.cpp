#include <cctype>
#include "parser.h"
namespace parser {
    
    enum tag_t { IDENT, NUMBER, LPAREN, RPAREN, END, ZVEZDA, TYPE, KLPAREN, KRPAREN };
    
    struct context {
        std::string text;  
        int c;            
        coord pos;         
        coord start;       
        tag_t tag;         
    };
    
    int next_char(context& ctx) {
        if (ctx.pos.offs == ctx.text.length() - 1) return ctx.c = -1;
        ctx.c = ctx.text[++ctx.pos.offs];
        if (ctx.c == '\n') {
            ctx.pos.line++;
            ctx.pos.col = 0;
        } else {
            ctx.pos.col++;
        }
        return ctx.c;
    }
    
    void next_token(context& ctx) throw(coord) {
        while (isspace(ctx.c)) next_char(ctx);
            
            ctx.start = ctx.pos;
            switch (ctx.c) {
                case -1: ctx.tag = END; break;
                case '(': ctx.tag = LPAREN; next_char(ctx); break;
                case ')': ctx.tag = RPAREN; next_char(ctx); break;
                case '*': ctx.tag = ZVEZDA; next_char(ctx); break;
                case '[': ctx.tag = KLPAREN; next_char(ctx); break;
                case ']': ctx.tag = KRPAREN; next_char(ctx); break;
                default:
                    if (isalpha(ctx.c)) {
                        //int flag = 0;
                        //if ( ctx.c == 'i' || ctx.c == 'f' ) flag = 1;
                        while (isalnum(next_char(ctx)));
                        std::string ident;
                        ident = ctx.text.substr(ctx.start.col - 1, ctx.pos.col - ctx.start.col);
                        if (ident == "int" || ident == "float") ctx.tag = TYPE;
                            else ctx.tag = IDENT;
                                } else if (isdigit(ctx.c)) {
                                    while (isdigit(next_char(ctx)));
                                    ctx.tag = NUMBER;
                                } else {
                                    throw ctx.pos;
                                }
            }
    }
    
    
    
    
    void parse_decl(context&) throw(coord);
    void parse_ptr(context&) throw(coord);
    void parse_dims(context&) throw(coord);
    void parse_prim(context&) throw(coord);
    
    void parse(std::string text) throw(coord) {
        context ctx { text };
        ctx.pos = coord{ 0, 1, 1 };
        ctx.c = (ctx.text.length() == 0) ? -1 : ctx.text[0];
        next_token(ctx);
        parse_decl(ctx);
        if (ctx.tag != END) throw ctx.start;
    }
    
    void parse_decl(context& ctx) throw(coord) {
        if (ctx.tag == TYPE) { // || ctx.tag == NUMBER || ctx.tag == LPAREN) {
            next_token(ctx);
            parse_ptr(ctx);
        }
    }
    
    void parse_ptr(context& ctx) throw(coord) {
        if (ctx.tag == ZVEZDA) {
            next_token(ctx);
            parse_ptr(ctx);
        }
        else if ( ctx.tag == IDENT || ctx.tag == LPAREN ) {
            parse_prim(ctx);
        }
    }
    
    void parse_prim(context& ctx) throw(coord) {
        if (ctx.tag == LPAREN) {
            next_token(ctx);
            parse_ptr(ctx);
            if (ctx.tag != RPAREN) throw ctx.start;
            next_token(ctx);
            parse_dims(ctx);
        }
        else {
            if (ctx.tag != IDENT) throw ctx.start;
            next_token(ctx);
            parse_dims(ctx);
        }
    }
    
    void parse_dims (context& ctx) throw(coord) {
        if (ctx.tag == KLPAREN) {
            next_token(ctx);
            if (ctx.tag != NUMBER) throw ctx.start;
            next_token(ctx);
            if (ctx.tag != KRPAREN) throw ctx.start;
            next_token(ctx);
            parse_dims(ctx);
        }
    }
}
