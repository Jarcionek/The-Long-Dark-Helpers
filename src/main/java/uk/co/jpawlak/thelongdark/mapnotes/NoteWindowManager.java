package uk.co.jpawlak.thelongdark.mapnotes;

import uk.co.jpawlak.thelongdark.mapnotes.serializable.Map;
import uk.co.jpawlak.thelongdark.mapnotes.serializable.MapSerialiser;
import uk.co.jpawlak.thelongdark.mapnotes.serializable.Marker;
import uk.co.jpawlak.thelongdark.mapnotes.serializable.SettingsSerialiser;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NoteWindowManager {

    private final SettingsSerialiser settingsSerialiser; //TODO  load and save position/size of the note frame
    private final MapSerialiser mapSerialiser;

    private JFrame frame;

    public NoteWindowManager(SettingsSerialiser settingsSerialiser, MapSerialiser mapSerialiser) {
        this.settingsSerialiser = settingsSerialiser;
        this.mapSerialiser = mapSerialiser;
    }

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
                save(textArea, marker, map);
            }
        });

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setMinimumSize(new Dimension(400, 300));
        frame.setSize(400, 300);
        frame.setAlwaysOnTop(true);

        //TODO open at last position
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);

        return frame;
    }

    private void save(JTextArea textArea, Marker marker, Map map) {
        String newText = textArea.getText();
        marker.setNote(newText.trim().isEmpty() ? null : newText);
        mapSerialiser.save(map);
    }




    //TODO window closing or resizing -> write to settings

    //TODO save note: ctrl+S, focus lost, window closing, every 5s? or 3s after stopped typing?
    //TODO how to save note? callback? NoteWindowManager should't have access to Map object
    //TODO file modified indicator (check whether pasting needs special handling)

}
