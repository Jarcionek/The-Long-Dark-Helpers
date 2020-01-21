package uk.co.jpawlak.thelongdark.mapnotes;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.io.File;

public class Main {

    public static final int SCROLL_SENSITIVITY = 15;

    public static final File MAIN_FOLDER = new File(System.getProperty("user.dir"), "The Long Dark Helpers");
    public static final File SAVED_MAPS_FOLDER = new File(MAIN_FOLDER, "Maps");

    public static void main(String[] args) {
        MAIN_FOLDER.mkdirs();
        SAVED_MAPS_FOLDER.mkdirs();

        JFrame frame = new JFrame();

        MapPanel mapPanel = new MapPanel(new Map("Ravine", "/regionimages/Ravine.png"));

        JScrollPane scrollPane = new JScrollPane(mapPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_SENSITIVITY);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(SCROLL_SENSITIVITY);

        frame.setContentPane(scrollPane);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
