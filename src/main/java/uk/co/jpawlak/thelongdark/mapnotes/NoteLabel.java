package uk.co.jpawlak.thelongdark.mapnotes;

import uk.co.jpawlak.thelongdark.mapnotes.serializable.Note;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class NoteLabel extends JLabel {

    public NoteLabel(Note note) {
        ImageIcon icon = icon();
        setSize(icon.getIconWidth(), icon.getIconHeight());
        setIcon(icon);
        setOpaque(false);
    }

    private ImageIcon icon() {
        try {
            return new ImageIcon(getClass().getResource("/icons/Note.png").toURI().toURL());
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
