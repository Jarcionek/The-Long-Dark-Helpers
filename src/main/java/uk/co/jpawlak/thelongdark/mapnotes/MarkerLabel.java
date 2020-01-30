package uk.co.jpawlak.thelongdark.mapnotes;

import uk.co.jpawlak.thelongdark.mapnotes.serializable.Marker;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.io.File;
import java.net.MalformedURLException;

public class MarkerLabel extends JLabel {

    public MarkerLabel(Marker marker) {
        ImageIcon icon = icon(marker.getImageLocation());
        setSize(icon.getIconWidth(), icon.getIconHeight());
        setIcon(icon);
        setOpaque(false);
    }

    private ImageIcon icon(String imageLocation) {
        try {
            return new ImageIcon(new File(Main.MARKERS_IMAGES_FOLDER, imageLocation).toURI().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}
