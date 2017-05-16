package ru.bmstu.iu9;


public class WordToken extends Token {
    public final String value;

    protected WordToken(String value, Position starting, Position following) {
        super(DomainTag.WORD, starting, following);
        this.value = value;
    }

    @Override
    public String toString() {
        return "WORD " + super.toString() + ": " + value;
    }
}
