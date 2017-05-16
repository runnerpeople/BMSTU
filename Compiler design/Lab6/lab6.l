%option noyywrap bison-bridge bison-locations
%{
#include <stdio.h>
#include <stdlib.h>

#define TAG_STRING_LITERAL 1
#define TAG_NUMERICAL_LITERAL 2
#define TAG_IDENT 3

char *tag_names[] = { "END_OF_PROGRAM", "STRING_LITERAL", "NUMERICAL_LITERAL", "IDENT" };

typedef struct Position Position;

struct Position {
	int line,pos,index;
};

void print_pos(Position *p) {
	printf("(%d,%d)",p->line,p->pos);
}

struct Fragment {
	Position starting, following;
};

typedef struct Fragment YYLTYPE;
typedef struct Fragment Fragment;

void print_frag(Fragment* f) {
	print_pos(&(f->starting));
	printf("-");
	print_pos(&(f->following));
}

union Token {
	char *str_literal;
    char *num_literal;
    char *ident;
};

typedef union Token YYSTYPE;

int continued;
struct Position cur;
#define YY_USER_ACTION {             \
    int i;                           \
    if (!continued)                  \
        yylloc->starting = cur;      \
    continued = 0;                   \
    for ( i = 0; i < yyleng; i++){   \
        if ( yytext[i] == '\n'){     \
            cur.line++;              \
            cur.pos = 1;             \
        }                            \
        else                         \
            cur.pos++;               \
        cur.index++;                 \
    }                                \
    yylloc->following = cur;         \
} 

long size_comment;

void init_scanner (char *program){
    continued = 0;
    cur.line = 1;
    cur.pos = 1;
    cur.index = 0;
    yy_scan_string(program);
}

void err (char *msg){
    printf ("Error");
    print_pos(&cur);
    printf(":%s\n",msg);
}
%}

NUMERICAL_LITERAL ([0-9]+)|(\$[0-9a-fA-F]+)
IDENT [a-zA-Z][a-zA-Z0-9\$]*

%x STRING

%%
[\n\t ]+
\"          	  	  {
						 BEGIN(STRING); 
						 continued = 1; 
						 size_comment = 2;
						 yylval->str_literal=(char*)malloc(size_comment*sizeof(char));
						 strcpy(yylval->str_literal,"\"");
					  }
<STRING>[^\\\"]*      { 
						 continued = 1;
						 size_comment += yyleng;
						 yylval->str_literal=(char*)realloc(yylval->str_literal,size_comment*sizeof(char));
						 strcat(yylval->str_literal,yytext);
					  }
<STRING>\\\n          {
						 continued = 1;
						 size_comment += 2;	
						 yylval->str_literal=(char*)realloc(yylval->str_literal,size_comment*sizeof(char));
						 strcat(yylval->str_literal,"\\\n");
					  }     
<STRING>\"\"	      {
						 continued = 1;
						 size_comment += 2;	
						 yylval->str_literal=(char*)realloc(yylval->str_literal,size_comment*sizeof(char));
						 strcat(yylval->str_literal,"\"\"");
					  }
<STRING>\"		      { 
						 size_comment += 1;
						 yylval->str_literal=(char*)realloc(yylval->str_literal,size_comment*sizeof(char));
						 strcat(yylval->str_literal,"\"");
						 BEGIN(0);
						 return TAG_STRING_LITERAL; 
					  }
<STRING><<EOF>>       {
						 err("end of program found, \'\"\' expected");
						 return 0;
                      }
{NUMERICAL_LITERAL}   {
                         yylval->num_literal = yytext;
                         return TAG_NUMERICAL_LITERAL;
                      }
                      
{IDENT}               {
                         yylval->ident = yytext;
                         return TAG_IDENT;
                      }

.                     err ("ERROR");

<<EOF>>               return 0;


%%


int main(){
    int tag;
    YYSTYPE value;
    YYLTYPE coords;
   	FILE *inputfile;
	long size_str;
	char *str;
	union Token token;
	inputfile = fopen("test.txt","r");
	if (inputfile == NULL) {
		fputs("File not found", stderr);
		exit(1);
	}
	fseek(inputfile, 0,SEEK_END);
	size_str = ftell(inputfile);
    rewind(inputfile);

    str=(char*)malloc(sizeof(char)*(size_str+1));
    if (str == NULL) {
		fputs("Memory error",stderr);
		exit(2);
	}    
    size_t n = fread(str,sizeof(char),size_str,inputfile);
    if (n != size_str) {
		fputs ("Reading error",stderr);
		exit (3);
	}
    str[size_str] = '\0';
    fclose (inputfile);
    init_scanner(str);
    do{
        tag = yylex(&value,&coords);
        if (tag != 0){
           printf("%s ",tag_names[tag]);
           print_frag(&coords);
           printf(":\n%s\n", value.ident); 
           if (tag == 1)
				free(value.str_literal);   
        }
    }
    while (tag != 0); 
	free(str);
    return 0;
}


