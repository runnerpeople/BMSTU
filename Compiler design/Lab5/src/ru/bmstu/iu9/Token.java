package ru.bmstu.iu9;


public class Token {
    public final DomainTag tag;
    public final Fragment coords;

    public String value;

    protected Token(DomainTag tag, String value, Position starting, Position following) {
        this.tag = tag;
        this.value = value;
        this.coords = new Fragment(starting, following);
    }

    @Override
    public String toString() {
        if (tag.name().contains("IDENT"))
            return "IDENT" + " " + coords.toString() + ": " + value;
        else
            return tag.name() + " " + coords.toString() + ": " + value;
    }
}
