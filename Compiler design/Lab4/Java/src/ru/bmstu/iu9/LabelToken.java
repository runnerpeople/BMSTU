package ru.bmstu.iu9;


public class LabelToken extends Token {
    public final String value;

    protected LabelToken(String value, Position starting, Position following) {
        super(DomainTag.LABEL, starting, following);
        this.value = value;
    }

    @Override
    public String toString() {
        return "LABEL " + super.toString() + ": " + value;
    }
}
