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
    private JTextArea textArea;
    private JScrollPane textAreaScrollPane;
    private Marker marker;
    private Map map;

    public NoteWindowManager(SettingsSerialiser settingsSerialiser, MapSerialiser mapSerialiser) {
        this.settingsSerialiser = settingsSerialiser;
        this.mapSerialiser = mapSerialiser;
    }

    //TODO ensure singleton
    public void openNote(Marker marker, Map map) {
        this.marker = marker;
        this.map = map;
        if (frame == null) {
            frame = createFrame();
        }
        textArea.setText(marker.getNote());
        frame.setVisible(true);
        //TODO ugly code: cannot execute it here normally..
        SwingUtilities.invokeLater(() -> {
            textAreaScrollPane.getHorizontalScrollBar().setValue(0);
            textAreaScrollPane.getVerticalScrollBar().setValue(0);
        });
    }

    private JFrame createFrame() {
        JFrame frame = new JFrame(Main.APPLICATION_NAME + " - Note");

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                System.out.println("window deactivated");
                save();
            }
        });

        textArea = new JTextArea();
        textArea.setTabSize(4);

        textAreaScrollPane = new JScrollPane(textArea);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(action -> {
            System.out.println("close button pressed");
            frame.setVisible(false);
        });

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.add(closeButton);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(textAreaScrollPane, BorderLayout.CENTER);
        contentPane.add(buttonsPanel, BorderLayout.SOUTH);
        frame.setContentPane(contentPane);

        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setMinimumSize(new Dimension(400, 300));
        frame.setSize(400, 300);

        //TODO open at last position
        frame.setLocationRelativeTo(null);

        return frame;
    }

    private void save() {
        System.out.println("saving");
        String newText = textArea.getText();
        marker.setNote(newText.trim().isEmpty() ? null : newText);
        mapSerialiser.save(map);
    }




    //TODO window closing or resizing -> write to settings

    //TODO save note: ctrl+S, focus lost, window closing, every 5s? or 3s after stopped typing?
    //TODO how to save note? callback? NoteWindowManager should't have access to Map object
    //TODO file modified indicator (check whether pasting needs special handling)

}
