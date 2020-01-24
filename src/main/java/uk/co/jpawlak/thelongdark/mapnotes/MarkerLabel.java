package uk.co.jpawlak.thelongdark.mapnotes;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class MarkerLabel extends JLabel {

    public MarkerLabel(Marker marker) {
        ImageIcon icon = icon(path(marker));
        setSize(icon.getIconWidth(), icon.getIconHeight());
        setIcon(icon);
        setOpaque(false);
    }

    private String path(Marker marker) {
        switch (marker.getType()) {
            case TICK: return "/icons/Tick.png";
            case WARNING: return "/icons/Exclamation mark.png";
            case UNKNOWN: return "/icons/Question mark.png";
            case CROSS: return "/icons/Cross.png";
            default: throw new UnsupportedOperationException("No image defined for type " + marker.getType());
        }
    }

    private ImageIcon icon(String path) {
        try {
            return new ImageIcon(getClass().getResource(path).toURI().toURL());
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
