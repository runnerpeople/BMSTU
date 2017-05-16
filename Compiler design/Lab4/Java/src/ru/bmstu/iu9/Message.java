package ru.bmstu.iu9;

public class Message {
    public final boolean isError;
    public final String text;
    public final Position position;

    public Message(boolean isError, String text, Position position) {
        this.isError = isError;
        this.text = text;
        this.position = position;
    }
}
