package ru.bmstu.iu9;


public class CommentToken extends Token {
    public final String value;

    protected CommentToken(String value, Position starting, Position following) {
        super(DomainTag.COMMENT, starting, following);
        this.value = value;
    }

    @Override
    public String toString() {
        return "COMMENT " + super.toString() + ": " + value;
    }
}
