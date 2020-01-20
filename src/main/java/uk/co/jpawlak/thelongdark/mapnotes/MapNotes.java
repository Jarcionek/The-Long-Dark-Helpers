package uk.co.jpawlak.thelongdark.mapnotes;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MapNotes {

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame();

        BufferedImage image = ImageIO.read(MapNotes.class.getResource("/regionimages/Ravine.png"));
        JLabel panel = new JLabel(new ImageIcon(image));

        frame.setContentPane(panel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
