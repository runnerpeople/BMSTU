package ru.bmstu.iu9;

abstract public class Token {
    public final DomainTag tag;
    public final Fragment coords;

    protected Token(DomainTag tag, Position starting, Position following) {
        this.tag = tag;
        this.coords = new Fragment(starting, following);
    }

    @Override
    public String toString() {
        return coords.toString();
    }
}
