package uk.co.jpawlak.thelongdark.mapnotes;

public class Marker {

    private final Type type;
    private final double x;
    private final double y;

    public Marker(Type type, double x, double y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public Type getType() {
        return type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public static enum Type {
        TICK, WARNING, UNKNOWN, CROSS
    }

}
