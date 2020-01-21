package uk.co.jpawlak.thelongdark.mapnotes;

import javax.swing.JLabel;
import java.awt.Color;

public class MarkerLabel extends JLabel {

    public MarkerLabel(Marker marker) {
        setSize(20, 20);
        setOpaque(true);
        setBackground(color(marker));
    }

    private Color color(Marker marker) {
        switch (marker.getType()) {
            case TICK: return Color.GREEN;
            case WARNING: return Color.YELLOW;
            case UNKNOWN: return Color.BLUE;
            case CROSS: return Color.RED;
            default: throw new UnsupportedOperationException("No color defined for type " + marker.getType());
        }
    }

}
