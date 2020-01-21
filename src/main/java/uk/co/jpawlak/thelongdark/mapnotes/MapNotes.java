package uk.co.jpawlak.thelongdark.mapnotes;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MapNotes {

    public static final int SCROLL_SENSITIVITY = 15;

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame();

        BufferedImage image = ImageIO.read(MapNotes.class.getResource("/regionimages/Ravine.png"));
        ImageIcon imageIcon = new ImageIcon(image);
        JLabel mapLabel = new JLabel(imageIcon);

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
