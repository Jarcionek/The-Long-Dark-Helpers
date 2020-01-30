package uk.co.jpawlak.thelongdark.mapnotes;

import uk.co.jpawlak.thelongdark.mapnotes.serializable.Marker;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;

public class MarkerLabel extends JLabel {

    public MarkerLabel(Marker marker) {
        setOpaque(false);
        setMarkerIcon(marker);
    }

    public void setMarkerIcon(Marker marker) {
        ImageIcon icon = icon(marker.getImageLocation());
        setSize(icon.getIconWidth(), icon.getIconHeight());
        setIcon(icon);
    }

    private ImageIcon icon(String imageLocation) {
        File file = new File(Main.MARKERS_IMAGES_FOLDER, imageLocation);
        if (!file.exists()) {
            setToolTipText("Image not found: " + imageLocation);
            return imageWithTextImageNotFound();
        }

        try {
            return new ImageIcon(file.toURI().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static ImageIcon imageWithTextImageNotFound() {
        final int width = 100;
        final int height = 20;
        final int fontSize = 12;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();

        g2.setFont(new Font("Arial", Font.BOLD, fontSize));

        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(0, 0, width, height);
        g2.setColor(Color.RED);
        g2.drawRect(0, 0, width - 1, height - 1);
        g2.drawRect(1, 1, width - 3, height - 3);

        g2.drawString("image not found", 5, height - fontSize / 2);

        g2.dispose();

        return new ImageIcon(image);
    }

}
