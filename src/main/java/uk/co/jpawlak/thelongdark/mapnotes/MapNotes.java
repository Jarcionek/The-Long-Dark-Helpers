package uk.co.jpawlak.thelongdark.mapnotes;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class MapNotes {

    public static final int SCROLL_SENSITIVITY = 15;

    public static void main(String[] args) {
        JFrame frame = new JFrame();

        MapPanel mapPanel = new MapPanel(new Map("/regionimages/Ravine.png"));

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
