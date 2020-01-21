package uk.co.jpawlak.thelongdark.mapnotes;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

        JLabel mapLabel = new JLabel(imageIcon);
        mapLabel.setLayout(null);

        mapLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON3) {
                    return;
                }

                JMenuItem newMarkerItem = new JMenuItem("New Marker");
                newMarkerItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e1) {
                        Marker marker = new Marker(1.0d * e.getX() / imageIcon.getIconWidth(), 1.0d * e.getY() / imageIcon.getIconHeight());
                        MarkerLabel markerLabel = new MarkerLabel();
                        markerLabel.setLocation(e.getX(), e.getY()); //TODO should be centered at mouse clicked position
                        mapLabel.add(markerLabel);
                    }
                });


                JPopupMenu popup = new JPopupMenu();
                popup.add(newMarkerItem);
                popup.show(mapLabel, e.getX(), e.getY());
            }
        });
    }

    private static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(MapNotes.class.getResource(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
