package uk.co.jpawlak.thelongdark.mapnotes;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MapPanel extends JLabel {

    private final Map map;

    public MapPanel(Map map) {
        this.map = map;

        BufferedImage image = loadImage(map.getImageLocation());
        ImageIcon imageIcon = new ImageIcon(image);
        setIcon(imageIcon);

        setLayout(null);
        for (Marker marker : map.getMarkers()) {
            createMarkerLabel(marker);
        }

        addMouseListener(new MapMouseListener(this));
    }

    private void createMarkerLabel(Marker marker) {
        MarkerLabel markerLabel = new MarkerLabel(marker);
        markerLabel.setLocation(
                (int) (marker.getX() * getIcon().getIconWidth()  - markerLabel.getWidth()  / 2.0d),
                (int) (marker.getY() * getIcon().getIconHeight() - markerLabel.getHeight() / 2.0d)
        );

        add(markerLabel);
        repaint();
    }

    private static class MapMouseListener extends MouseAdapter {

        private MapPanel mapPanel;

        public MapMouseListener(MapPanel mapPanel) {
            this.mapPanel = mapPanel;
        }

        @Override
        public void mousePressed(MouseEvent event) {
            if (event.getButton() != MouseEvent.BUTTON3) {
                return;
            }

            JPopupMenu popup = new JPopupMenu();
            popup.add(newMenuItem("Done",    Marker.Type.TICK,    event.getX(), event.getY()));
            popup.add(newMenuItem("Warning", Marker.Type.WARNING, event.getX(), event.getY()));
            popup.add(newMenuItem("Unknown", Marker.Type.UNKNOWN, event.getX(), event.getY()));
            popup.add(newMenuItem("Cross",   Marker.Type.CROSS,   event.getX(), event.getY()));
            popup.show(mapPanel, event.getX(), event.getY());
        }

        private JMenuItem newMenuItem(String name, Marker.Type markerType, int x, int y) {
            JMenuItem newMarkerItem = new JMenuItem(name);
            newMarkerItem.addActionListener(action -> {
                Marker marker = new Marker(
                        markerType,
                        1.0d * x / mapPanel.getIcon().getIconWidth(),
                        1.0d * y / mapPanel.getIcon().getIconHeight()
                );
                mapPanel.map.addMarker(marker);
                Serialiser.save(mapPanel.map);
                mapPanel.createMarkerLabel(marker);
            });
            return newMarkerItem;
        }

    }

    private static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(MapPanel.class.getResource(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
