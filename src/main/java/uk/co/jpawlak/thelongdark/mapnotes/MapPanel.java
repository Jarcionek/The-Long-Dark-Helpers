package uk.co.jpawlak.thelongdark.mapnotes;

import uk.co.jpawlak.thelongdark.mapnotes.serializable.Map;
import uk.co.jpawlak.thelongdark.mapnotes.serializable.MapSerialiser;
import uk.co.jpawlak.thelongdark.mapnotes.serializable.Marker;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class MapPanel extends JLabel {

    private final Map map;

    private final MapSerialiser mapSerialiser;

    public MapPanel(Map map, MapSerialiser mapSerialiser) {
        this.map = map;
        this.mapSerialiser = mapSerialiser;

        BufferedImage image = loadImage(map.getImageLocation());
        ImageIcon imageIcon = new ImageIcon(image);
        setIcon(imageIcon);
        setVerticalAlignment(SwingConstants.TOP);
        //TODO nice to have: if frame is bigger than image, center the image; this requires making sure that markers position are relative to the image

        setLayout(null);
        for (Marker marker : map.getMarkers()) {
            createMarkerLabel(marker);
        }

        addMouseListener(new MapMouseListener(this));
    }

    private void createMarkerLabel(Marker marker) {
        MarkerLabel markerLabel = new MarkerLabel(marker);
        markerLabel.setLocation(calculatedLocationFor(marker, markerLabel));
        markerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON1) {
                    JTextArea textArea = new JTextArea(marker.getNote(), 40, 60); //TODO this should be more flexible
                    JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "Note", JOptionPane.PLAIN_MESSAGE); //TODO should be relative to frame
                    String newText = textArea.getText();
                    marker.setNote(newText.trim().isEmpty() ? null : newText);
                    mapSerialiser.save(map);
                }
                if (event.getButton() == MouseEvent.BUTTON3) {
                    JMenuItem deleteMarkerMenuItem = new JMenuItem("Delete");
                    deleteMarkerMenuItem.addActionListener(action -> {
                        //TODO to consider: confirmation popup if there is a note?
                        MapPanel.this.remove(markerLabel);
                        map.removeMarker(marker);
                        repaint();
                        mapSerialiser.save(map);
                    });

                    JMenu changeIconMenu = new JMenu("Change icon");
                    Consumer<String> clickCallback = imageLocation -> {
                        marker.setImageLocation(imageLocation);
                        markerLabel.setMarkerIcon(marker);
                        markerLabel.setLocation(calculatedLocationFor(marker, markerLabel));
                        mapSerialiser.save(map);
                    };
                    MarkersMenuItemsLoader.createFromFiles(clickCallback).forEach(changeIconMenu::add);

                    JPopupMenu popup = new JPopupMenu();
                    popup.add(deleteMarkerMenuItem);
                    popup.add(changeIconMenu);
                    popup.show(markerLabel, event.getX(), event.getY());
                }
            }
        });

        add(markerLabel);
        repaint();
    }

    private Point calculatedLocationFor(Marker marker, MarkerLabel markerLabel) {
        return new Point(
                (int) (marker.getX() * getIcon().getIconWidth() - markerLabel.getWidth() / 2.0d),
                (int) (marker.getY() * getIcon().getIconHeight() - markerLabel.getHeight() / 2.0d)
        );
    }

    private class MapMouseListener extends MouseAdapter {

        private MapPanel mapPanel;

        public MapMouseListener(MapPanel mapPanel) {
            this.mapPanel = mapPanel;
        }

        @Override
        public void mousePressed(MouseEvent event) {
            if (event.getButton() != MouseEvent.BUTTON3) {
                return;
            }

            Consumer<String> clickCallback = imageLocation -> {
                Marker marker = new Marker(
                        1.0d * event.getX() / mapPanel.getIcon().getIconWidth(),
                        1.0d * event.getY() / mapPanel.getIcon().getIconHeight(),
                        imageLocation,
                        null
                );
                mapPanel.map.addMarker(marker);
                mapSerialiser.save(mapPanel.map);
                mapPanel.createMarkerLabel(marker);
            };

            JPopupMenu popup = new JPopupMenu();
            MarkersMenuItemsLoader.createFromFiles(clickCallback).forEach(popup::add);
            popup.show(mapPanel, event.getX(), event.getY());
        }

    }

    private static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(path, e);
        }
    }

}
