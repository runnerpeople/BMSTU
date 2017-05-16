package ru.bmstu.iu9;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Position {
    private int line, pos;

    Position(int line,int pos) {
        this.line = line;
        this.pos = pos;
    }

    void next_line() {
        this.line += 1;
        this.pos = 1;
    }

    void next_pos(int pos) {
        this.pos += pos;
    }

    public String toString() {
        return "(" + this.line + "," + this.pos + ")";
    }
}

public class Main {

    private static void test_match(String line,Position pos) {
        String letterOrDigit = "[\\p{L}|\\d]+";
        String ident = "[$@%]" + letterOrDigit;
        String name_function = "\\p{L}" + letterOrDigit;
        String keywords = "sub|if|unless";
        
        String pattern = "(^" + ident + ")|(^" + keywords + ")|(^" + name_function + ")";

        Pattern p = Pattern.compile(pattern);
        Matcher m;

        while (!line.equals("")) {
            m = p.matcher(line);
            if (m.find()) {
                if (m.group(3) != null) {
                    String item = m.group(3);
                    System.out.println("NAME_FUNC " + pos.toString() + ": " + item);
                    pos.next_pos(item.length());
                    line = line.substring(line.indexOf(item) + item.length());
                } else
                if (m.group(2) != null) {
                    String item = m.group(2);
                    System.out.println("KEYWORD " + pos.toString() + ": " + item);
                    pos.next_pos(item.length());
                    line = line.substring(line.indexOf(item) + item.length());
                } else {
                    String item= m.group(1);
                    System.out.println("IDENT " + pos.toString() + ": " + item);
                    pos.next_pos(item.length());
                    line = line.substring(line.indexOf(item) + item.length());
                }
            }
            else {
                if (Character.isWhitespace(line.charAt(0))) {
                    while (Character.isWhitespace(line.charAt(0))) {
                        line = line.substring(1);
                        pos.next_pos(1);
                    }
                }
                else {
                    System.out.println("syntax error " + pos.toString());
                    while (!m.find() && !line.equals("")) {
                        line = line.substring(1);
                        pos.next_pos(1);
                        m = p.matcher(line);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("You should give 1 argument: javac -encoding utf8 <file>");
            System.exit(1);
        }
        else {
            Position pos = new Position(1,1);
            List<String> lines;
            String file_name = args[2];
            try {
                lines = Files.readAllLines(Paths.get(file_name), StandardCharsets.UTF_8);
                for (String line : lines) {
                    test_match(line,pos);
                    pos.next_line();
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
