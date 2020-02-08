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
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NoteWindowManager {

    private static final Dimension DEFAULT_SIZE = new Dimension(400, 300);

    private final SettingsSerialiser settingsSerialiser;
    private final MapSerialiser mapSerialiser;

    private JFrame frame;
    private Marker marker;

    public NoteWindowManager(SettingsSerialiser settingsSerialiser, MapSerialiser mapSerialiser) {
        this.settingsSerialiser = settingsSerialiser;
        this.mapSerialiser = mapSerialiser;
    }

    //TODO ugly code: this class should not have access to the entire Map object
    public synchronized void openNote(Marker marker, Map map) {
        if (frame != null) {
            this.frame.dispose();
        }
        this.frame = createFrame(marker, map);
        this.marker = marker;
    }

    public synchronized void closeNoteIfOpen(Marker marker) {
        if (this.marker == marker) {
            this.frame.dispose();
            this.frame = null;
        }
    }

    //TODO nice to have: help in frame menu bar informing about CTRL+S and ESC?
    private JFrame createFrame(Marker marker, Map map) {
        JFrame frame = new JFrame(Main.APPLICATION_NAME + " - Note");

        JTextArea textArea = new JTextArea();
        textArea.setText(marker.getNote());
        textArea.setTabSize(4);

        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    frame.dispose();
                }
                if (e.getModifiers() == InputEvent.CTRL_MASK && e.getKeyCode() == KeyEvent.VK_S) {
                    saveMap(frame, textArea, marker, map);
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                //TODO bug: current textArea text is before this event is actually applied
                if (!textArea.getText().equals(marker.getNote())) {
                    setTitleAsteriskVisible(frame, true);
                }
            }
        });

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
                //TODO ugly code: this is saving far too often - if there was no change or if the marker was deleted
                saveMap(frame, textArea, marker, map);
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
        frame.setAlwaysOnTop(true); //TODO to consider: setting?

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

    private void saveMap(JFrame frame, JTextArea textArea, Marker marker, Map map) {
        String newText = textArea.getText();
        marker.setNote(newText.trim().isEmpty() ? null : newText);
        mapSerialiser.save(map);
        setTitleAsteriskVisible(frame, false);
    }

    private void saveNoteWindowSettings(Point location, Dimension size) {
        Settings settings = settingsSerialiser.load();
        settings.setNoteWindowSettings(new WindowSettings(location.x, location.y, size.width, size.height));
        settingsSerialiser.save(settings);
    }

    private static void setTitleAsteriskVisible(JFrame frame, boolean showAsterisk) {
        String titleWithoutAsterisk = frame.getTitle().startsWith("*") ? frame.getTitle().substring(1) : frame.getTitle();
        String newTitle = showAsterisk ? "*" + titleWithoutAsterisk : titleWithoutAsterisk;
        frame.setTitle(newTitle);
    }

}
