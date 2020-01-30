package uk.co.jpawlak.thelongdark.mapnotes.serializable;

public class Marker {

    @Deprecated // removed after 1.1
    private Type type;

    private final double x;
    private final double y;

    private String imageLocation;

    public Marker(Type type, double x, double y) {
        this.type = type;
        this.x = x;
        this.y = y;
        migrate();
    }

    @Deprecated
    public Type getType() {
        return type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    void migrate() {
        if (type == null) {
            return;
        }
        imageLocation = imageLocationForType();
        type = null;
    }

    private String imageLocationForType() {
        switch (type) {
            case TICK:    return "/Tick.png";
            case WARNING: return "/Exclamation mark.png";
            case UNKNOWN: return "/Question mark.png";
            case CROSS:   return "/Cross.png";
            default: throw new UnsupportedOperationException("No image defined for type " + type);
        }
    }

    @Deprecated // removed after 1.1
    public static enum Type {
        TICK, WARNING, UNKNOWN, CROSS
    }

}
