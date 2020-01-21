package uk.co.jpawlak.thelongdark.mapnotes;

public class Note {

    private final String text;
    private final double x;
    private final double y;

    public Note(double x, double y) {
        this.text = "";
        this.x = x;
        this.y = y;
    }

    public String getText() {
        return text;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}
