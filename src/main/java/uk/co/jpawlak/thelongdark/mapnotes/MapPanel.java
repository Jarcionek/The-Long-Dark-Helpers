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

            JMenuItem newMarkerItem = new JMenuItem("New Marker");
            newMarkerItem.addActionListener(action -> {
                Marker marker = new Marker(
                        Marker.Type.TICK,
                        1.0d * event.getX() / mapPanel.getIcon().getIconWidth(),
                        1.0d * event.getY() / mapPanel.getIcon().getIconHeight()
                );
                mapPanel.map.addMarker(marker);
                Serialiser.save(mapPanel.map);
                mapPanel.createMarkerLabel(marker);
            });

            JPopupMenu popup = new JPopupMenu();
            popup.add(newMarkerItem);
            popup.show(mapPanel, event.getX(), event.getY());
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
