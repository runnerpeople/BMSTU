package ru.bmstu.iu9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        Compiler compiler = new Compiler();
        String program = new String(Files.readAllBytes(Paths.get(args[0])));
        Scanner scanner = new Scanner(program,compiler);
        System.out.println("TOKENS:");
        while (true) {
            Token token = scanner.nextToken();
            if (token.tag != DomainTag.END_OF_PROGRAM) {
                System.out.print(token.tag.name()+" "+token.coords.toString() + ": " );
                if (token instanceof WordToken) {
                    System.out.print(((WordToken) token).value);
                }
                else if (token instanceof LabelToken) {
                    System.out.print(((LabelToken) token).value);
                }
                else if (token instanceof CommentToken) {
                    System.out.print(((CommentToken) token).value);
                }
                System.out.println();
            }
            else {
                break;
            }
        }
        if (compiler.errors()) {
            System.out.println("MESSAGES:");
            compiler.outputMessages();
        }
    }
}
