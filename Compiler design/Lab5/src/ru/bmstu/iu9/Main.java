package ru.bmstu.iu9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {

    static Integer[][] table = {
                 /* Wh,Di, |, +, g, s, e, t,Lt,X */
             /*0*/ { 1, 7, 9, 8, 3, 5, 2, 2, 2,15},
             /*1*/ { 1,-1,-1,-1,-1,-1,-1,-1,-1,15},
             /*2*/ {-1, 2,-1,-1, 2, 2, 2, 2, 2,15},
             /*3*/ {-1, 2,-1,-1, 2, 2, 4, 2, 2,15},
             /*4*/ {-1, 2,-1,-1, 2, 2, 2, 6, 2,15},
             /*5*/ {-1, 2,-1,-1, 2, 2, 4, 2, 2,15},
             /*6*/ {-1, 2,-1,-1, 2, 2, 2, 2, 2,15},
             /*7*/ {-1, 7,-1,-1,-1,-1,-1,-1,-1,15},
             /*8*/ {-1,-1,-1,-1,-1,-1,-1,-1,-1,15},
             /*9*/ {-1,-1, 8,10,-1,-1,-1,-1,-1,15},
            /*10*/ {10,10,11,13,10,10,10,10,10,10},
            /*11*/ {10,10,10,12,10,10,10,10,10,10},
            /*12*/ {-1,-1,-1,-1,-1,-1,-1,-1,-1,15},
            /*13*/ {10,10,14,13,10,10,10,10,10,10},
            /*14*/ {-1,-1,-1,-1,-1,-1,-1,-1,-1,15},
            /*15*/ {-1,-1,-1,-1,-1,-1,-1,-1,-1,15},
    };

    static int numberColumn(Position ch){
        if (ch.isEOF())
            return -1;
        else if (ch.isWhitespace()) {
            return 0;
        }
        else if (Character.isDigit(ch.getCode())) {
            return 1;
        }
        else if (ch.getCode() == '|') {
            return 2;
        }
        else if (ch.getCode() == '+') {
            return 3;
        }
        else if (ch.getCode() == 'g') {
            return 4;
        }
        else if (ch.getCode() == 's') {
            return 5;
        }
        else if (ch.getCode() == 'e') {
            return 6;
        }
        else if (ch.getCode() == 't') {
            return 7;
        }
        else if (Character.isLetter(ch.getCode())) {
            return 8;
        }
        else {
            return 9;
        }
    }

    static final ArrayList<DomainTag> final_state = new ArrayList<DomainTag>() {{
        add(DomainTag.WHITESPACE);
        add(DomainTag.IDENT_1);
        add(DomainTag.IDENT_2);
        add(DomainTag.IDENT_3);
        add(DomainTag.IDENT_4);
        add(DomainTag.KEYWORD);
        add(DomainTag.NUM_LITER);
        add(DomainTag.OPERATION);
        add(DomainTag.ERROR);
        add(DomainTag.COMMENTARY);
    }};


    public static void main(String[] args) throws IOException {

        String text_program = new String(Files.readAllBytes(Paths.get(args[0])));

        Position cur = new Position(text_program);

        ArrayList<Token> tokens = new ArrayList<>();
        ArrayList<ErrorMessage> errorMessages = new ArrayList<>();

        Position start,end;
        int state,prev_state;

        System.out.println("TOKENS:");
        while(!cur.isEOF()){
            while (cur.isWhitespace())
                cur = cur.next();
            state = 0;
            prev_state = 0;
            start = new Position(cur);
            while (state != -1) {
                int symbols = numberColumn(cur);
                prev_state=state;
                state = (symbols == -1) ? -1: table[state][symbols];
                if(state!=-1){
                    cur = cur.next();
                }
            }
            if (prev_state!=1 && prev_state!=0 && prev_state!= 15){
                end=new Position(cur);
                if (prev_state==12)
                    errorMessages.add(new ErrorMessage(true,"error: \"|+\" within comment",cur));
                else if (!final_state.contains(DomainTag.values()[prev_state]) && DomainTag.values()[prev_state] == DomainTag.OPERATION_OR_COMMENT)
                    errorMessages.add(new ErrorMessage(true,"error: expected \"|\" or \"+\" for token \"|",cur));
                else if (!final_state.contains(DomainTag.values()[prev_state]) && DomainTag.values()[prev_state] == DomainTag.COMMENT)
                    errorMessages.add(new ErrorMessage(true,"error: expected \"+|\" for commentary",cur));
                else if (!final_state.contains(DomainTag.values()[prev_state]))
                    errorMessages.add(new ErrorMessage(true,"error: unknown token \"" + text_program.substring(start.getIndex(),end.getIndex()) + "\"",cur));
                else
                    tokens.add(new Token(DomainTag.values()[prev_state],text_program.substring(start.getIndex(),end.getIndex()),start,end));
            }
            else {
                while (!cur.isEOF() && numberColumn(cur) == -1) {
                    cur = cur.next();
                }
                end=new Position(cur);
                errorMessages.add(new ErrorMessage(true,"error: unknown token \"" + text_program.substring(start.getIndex(),end.getIndex()) + "\"",cur));
            }
        }

        for(Token token: tokens)
            System.out.println(token);

        if (!errorMessages.isEmpty()) {
            System.out.println("Errors:");
            for(ErrorMessage mes: errorMessages)
                System.out.println(mes.toString());
        }
    }
}


