package uk.co.jpawlak.thelongdark.mapnotes;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MapNotes {

    public static final int SCROLL_SENSITIVITY = 15;

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame();

        BufferedImage image = ImageIO.read(MapNotes.class.getResource("/regionimages/Ravine.png"));
        ImageIcon imageIcon = new ImageIcon(image);
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

        JScrollPane scrollPane = new JScrollPane(mapLabel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_SENSITIVITY);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(SCROLL_SENSITIVITY);

        frame.setContentPane(scrollPane);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
