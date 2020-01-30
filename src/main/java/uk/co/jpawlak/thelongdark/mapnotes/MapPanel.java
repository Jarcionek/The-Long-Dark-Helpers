package uk.co.jpawlak.thelongdark.mapnotes;

import uk.co.jpawlak.thelongdark.mapnotes.serializable.Map;
import uk.co.jpawlak.thelongdark.mapnotes.serializable.MapSerialiser;
import uk.co.jpawlak.thelongdark.mapnotes.serializable.Marker;
import uk.co.jpawlak.thelongdark.mapnotes.serializable.Note;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MapPanel extends JLabel {

    private final Map map;

    public MapPanel(Map map) {
        this.map = map;

        BufferedImage image = loadImage(map.getImageLocation());
        ImageIcon imageIcon = new ImageIcon(image);
        setIcon(imageIcon);
        setVerticalAlignment(SwingConstants.TOP);
        //TODO nice to have: if frame is bigger than image, center the image; this requires making sure that markers position are relative to the image

        setLayout(null);
        for (Marker marker : map.getMarkers()) {
            createMarkerLabel(marker);
        }
        for (Note note : map.getNotes()) {
            createNoteLabel(note);
        }

        addMouseListener(new MapMouseListener(this));
    }

    //TODO remove all that duplication between creating notes and markers

    private void createMarkerLabel(Marker marker) {
        MarkerLabel markerLabel = new MarkerLabel(marker);
        markerLabel.setLocation(
                (int) (marker.getX() * getIcon().getIconWidth() - markerLabel.getWidth() / 2.0d),
                (int) (marker.getY() * getIcon().getIconHeight() - markerLabel.getHeight() / 2.0d)
        );
        markerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON1) {
                    JTextArea textArea = new JTextArea(marker.getNote(), 40, 60); //TODO this should be more flexible
                    JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "Note", JOptionPane.PLAIN_MESSAGE); //TODO should be relative to frame
                    marker.setNote(textArea.getText());
                    MapSerialiser.save(map);
                }
                if (event.getButton() == MouseEvent.BUTTON3) {
                    JMenuItem deleteMarkerMenuItem = new JMenuItem("Delete");
                    deleteMarkerMenuItem.addActionListener(action -> {
                        MapPanel.this.remove(markerLabel);
                        map.removeMarker(marker);
                        repaint();
                        MapSerialiser.save(map);
                    });

                    JPopupMenu popup = new JPopupMenu();
                    popup.add(deleteMarkerMenuItem);
                    popup.show(markerLabel, event.getX(), event.getY());
                }
            }
        });

        add(markerLabel);
        repaint();
    }

    private void createNoteLabel(Note note) {
        NoteLabel noteLabel = new NoteLabel(note);
        noteLabel.setLocation(
                (int) (note.getX() * getIcon().getIconWidth() - noteLabel.getWidth() / 2.0d),
                (int) (note.getY() * getIcon().getIconHeight() - noteLabel.getHeight() / 2.0d)
        );
        noteLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON1) {
                    JTextArea textArea = new JTextArea(note.getText(), 40, 60); //TODO this should be more flexible
                    JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "Note", JOptionPane.PLAIN_MESSAGE); //TODO should be relative to frame
                    note.setText(textArea.getText());
                    MapSerialiser.save(map);
                }
                if (event.getButton() == MouseEvent.BUTTON3) {
                    JMenuItem deleteMarkerMenuItem = new JMenuItem("Delete");
                    deleteMarkerMenuItem.addActionListener(action -> {
                        MapPanel.this.remove(noteLabel);
                        map.removeNote(note);
                        repaint();
                        MapSerialiser.save(map);
                    });

                    JPopupMenu popup = new JPopupMenu();
                    popup.add(deleteMarkerMenuItem);
                    popup.show(noteLabel, event.getX(), event.getY());
                }
            }
        });

        add(noteLabel);
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
            popup.add(newNoteMenuItem(event.getX(), event.getY()));
            popup.add(newMarkerMenuItem("Done", Marker.Type.TICK, event.getX(), event.getY()));
            popup.add(newMarkerMenuItem("Warning", Marker.Type.WARNING, event.getX(), event.getY()));
            popup.add(newMarkerMenuItem("Unknown", Marker.Type.UNKNOWN, event.getX(), event.getY()));
            popup.add(newMarkerMenuItem("Cross", Marker.Type.CROSS, event.getX(), event.getY()));
            popup.show(mapPanel, event.getX(), event.getY());
        }

        private JMenuItem newNoteMenuItem(int x, int y) {
            JMenuItem newMarkerItem = new JMenuItem("Note");
            newMarkerItem.addActionListener(action -> {
                Note note = new Note(
                        1.0d * x / mapPanel.getIcon().getIconWidth(),
                        1.0d * y / mapPanel.getIcon().getIconHeight()
                );
                mapPanel.map.addNote(note);
                MapSerialiser.save(mapPanel.map);
                mapPanel.createNoteLabel(note);
            });
            return newMarkerItem;
        }

        private JMenuItem newMarkerMenuItem(String name, Marker.Type markerType, int x, int y) {
            JMenuItem newMarkerItem = new JMenuItem(name);
            newMarkerItem.addActionListener(action -> {
                Marker marker = new Marker(
                        markerType,
                        1.0d * x / mapPanel.getIcon().getIconWidth(),
                        1.0d * y / mapPanel.getIcon().getIconHeight()
                );
                mapPanel.map.addMarker(marker);
                MapSerialiser.save(mapPanel.map);
                mapPanel.createMarkerLabel(marker);
            });
            return newMarkerItem;
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
