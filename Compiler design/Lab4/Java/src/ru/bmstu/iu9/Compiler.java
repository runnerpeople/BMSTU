package ru.bmstu.iu9;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Compiler {
    private List<Message> messages;
    private Map<String, Integer> nameCodes;
    private List<String> names;

    public Compiler() {
        messages = new ArrayList<Message>();
        nameCodes = new HashMap<String, Integer>();
        names = new ArrayList<String>();
    }

    public int addName(String name) {
        if (nameCodes.containsKey(name))
            return nameCodes.get(name);
        else {
            int code = names.size();
            names.add(name);
            nameCodes.put(name,code);
            return code;
        }
    }

    public boolean errors() {
        return messages.size() > 0;
    }

    public String getName(int code) {
        return names.get(code);
    }

    public void addMessage(boolean isErr, Position c, String text) {
        messages.add(new Message(isErr, text, c));
    }

    public void outputMessages() {
        for (Message m : messages) {
            System.out.print(m.isError ? "Error" : "Warning");
            System.out.print(" " + m.position + ": ");
            System.out.println(m.text);
        }
    }

    public Scanner getScanner(String program) {
        return new Scanner(program, this);
    }
}
