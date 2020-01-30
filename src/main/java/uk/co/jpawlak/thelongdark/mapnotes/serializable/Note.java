package uk.co.jpawlak.thelongdark.mapnotes.serializable;

@SuppressWarnings("unused") // used by GSON serialiser to load maps 1.1
@Deprecated // removed after 1.1
public class Note {

    private double x;
    private double y;
    private String text;

    String getText() {
        return text;
    }

    double getX() {
        return x;
    }

    double getY() {
        return y;
    }

}
