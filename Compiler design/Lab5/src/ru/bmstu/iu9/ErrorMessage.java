package ru.bmstu.iu9;

public class ErrorMessage {
    public final boolean isError;
    public final String text;
    public final Position position;

    public ErrorMessage(boolean isError, String text, Position position) {
        this.isError = isError;
        this.text = text;
        this.position = position;
    }

    @Override
    public String toString() {
        return this.text + " - " + this.position;
    }
}