package ru.bmstu.iu9;

public class Fragment {
    public final Position starting, following;

    public Fragment(Position starting, Position following) {
        this.starting = starting;
        this.following = following;
    }

    public String toString() {
        return starting.toString() + "-" + following.toString();
    }
}

