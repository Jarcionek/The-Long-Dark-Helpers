package uk.co.jpawlak.thelongdark.mapnotes.serializable;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class Map {

    private final String name;
    private final String imageLocation;
    private final List<Marker> markers = new ArrayList<>();

    @Deprecated // removed after 1.1
    private List<Note> notes = new ArrayList<>();

    public Map(String name, String imageLocation) {
        this.name = name;
        this.imageLocation = imageLocation;
    }

    public String getName() {
        return name;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public List<Marker> getMarkers() {
        return unmodifiableList(markers);
    }

    public List<Note> getNotes() {
        return unmodifiableList(notes);
    }


    public void addMarker(Marker marker) {
        markers.add(marker);
    }

    public void removeMarker(Marker marker) {
        markers.remove(marker);
    }


    public void addNote(Note note) {
        notes.add(note);
    }

    public void removeNote(Note note) {
        notes.remove(note);
    }

    void migrate() {
        markers.forEach(Marker::migrate);
        notes.stream()
                .map(note -> new Marker(note.getX(), note.getY(), "Note.png", note.getText()))
                .forEach(markers::add);
        notes = null;
    }

}
