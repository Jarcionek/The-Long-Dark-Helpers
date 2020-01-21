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

        addMouseListener(new MapMouseListener(this));
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
                        1.0d * event.getX() / mapPanel.getIcon().getIconWidth(),
                        1.0d * event.getY() / mapPanel.getIcon().getIconHeight()
                );
                mapPanel.map.addMarker(marker);

                MarkerLabel markerLabel = new MarkerLabel();
                markerLabel.setLocation(
                        event.getX() - markerLabel.getWidth() / 2,
                        event.getY() - markerLabel.getHeight() / 2
                );

                mapPanel.add(markerLabel);
                mapPanel.repaint();
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
