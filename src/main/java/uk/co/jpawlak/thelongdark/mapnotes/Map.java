package uk.co.jpawlak.thelongdark.mapnotes;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class Map {

    private final String imageLocation;
    private final List<Marker> markers = new ArrayList<>();

    public Map(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public List<Marker> getMarkers() {
        return unmodifiableList(markers);
    }

    public void addMarker(Marker marker) {
        markers.add(marker);
    }

    public void removeMarker(Marker marker) {
        markers.remove(marker);
    }

}
