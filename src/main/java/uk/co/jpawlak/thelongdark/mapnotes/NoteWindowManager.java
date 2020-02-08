package uk.co.jpawlak.thelongdark.mapnotes;

import uk.co.jpawlak.thelongdark.mapnotes.serializable.Map;
import uk.co.jpawlak.thelongdark.mapnotes.serializable.MapSerialiser;
import uk.co.jpawlak.thelongdark.mapnotes.serializable.Marker;
import uk.co.jpawlak.thelongdark.mapnotes.serializable.Settings;
import uk.co.jpawlak.thelongdark.mapnotes.serializable.SettingsSerialiser;
import uk.co.jpawlak.thelongdark.mapnotes.serializable.WindowSettings;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NoteWindowManager {

    private static final Dimension DEFAULT_SIZE = new Dimension(400, 300);

    private final SettingsSerialiser settingsSerialiser;
    private final MapSerialiser mapSerialiser;

    private JFrame frame;

    public NoteWindowManager(SettingsSerialiser settingsSerialiser, MapSerialiser mapSerialiser) {
        this.settingsSerialiser = settingsSerialiser;
        this.mapSerialiser = mapSerialiser;
    }

    //TODO ugly code: this class should not have access to the entire Map object
    public synchronized void openNote(Marker marker, Map map) {
        if (frame != null) {
            frame.dispose();
        }
        frame = createFrame(marker, map);
    }

    private JFrame createFrame(Marker marker, Map map) {
        JFrame frame = new JFrame(Main.APPLICATION_NAME + " - Note");

        JTextArea textArea = new JTextArea();
        textArea.setText(marker.getNote());
        textArea.setTabSize(4);

        JScrollPane textAreaScrollPane = new JScrollPane(textArea);
        SwingUtilities.invokeLater(() -> {
            textAreaScrollPane.getHorizontalScrollBar().setValue(0);
            textAreaScrollPane.getVerticalScrollBar().setValue(0);
        });

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(action -> {
            frame.dispose();
        });

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.add(closeButton);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(textAreaScrollPane, BorderLayout.CENTER);
        contentPane.add(buttonsPanel, BorderLayout.SOUTH);
        frame.setContentPane(contentPane);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                saveMap(textArea, marker, map);
            }
        });
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                saveNoteWindowSettings(frame.getLocation(), frame.getSize());
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                saveNoteWindowSettings(frame.getLocation(), frame.getSize());
            }
        });

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setMinimumSize(DEFAULT_SIZE);
        frame.setAlwaysOnTop(true);

        WindowSettings noteWindowSettings = settingsSerialiser.load().getNoteWindowSettings();
        if (noteWindowSettings == null) {
            frame.setSize(DEFAULT_SIZE);
            frame.setLocationRelativeTo(null);
        } else {
            frame.setSize(noteWindowSettings.getWidth(), noteWindowSettings.getHeight());
            frame.setLocation(noteWindowSettings.getX(), noteWindowSettings.getY());
        }

        frame.setVisible(true);

        return frame;
    }
    private void saveMap(JTextArea textArea, Marker marker, Map map) {
        String newText = textArea.getText();
        marker.setNote(newText.trim().isEmpty() ? null : newText);
        mapSerialiser.save(map);
    }

    private void saveNoteWindowSettings(Point location, Dimension size) {
        Settings settings = settingsSerialiser.load();
        settings.setNoteWindowSettings(new WindowSettings(location.x, location.y, size.width, size.height));
        settingsSerialiser.save(settings);
    }

    //TODO nice to have: save note on ctrl+S and periodically? (every 5s? or 3s after stopped typing?)
    //TODO nice to have: file modified indicator (check whether pasting needs special handling)

}
