package uk.co.jpawlak.thelongdark.mapnotes.serializable;

public class Note {

    private final double x;
    private final double y;

    private String text;

    public Note(double x, double y) {
        this.x = x;
        this.y = y;
        this.text = "";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}
