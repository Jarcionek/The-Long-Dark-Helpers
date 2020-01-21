package uk.co.jpawlak.thelongdark.mapnotes;

import javax.swing.JLabel;
import java.awt.Color;

public class NoteLabel extends JLabel {

    public NoteLabel(Note note) {
        setSize(20, 20);
        setOpaque(true);
        setBackground(Color.GRAY);
    }

}
