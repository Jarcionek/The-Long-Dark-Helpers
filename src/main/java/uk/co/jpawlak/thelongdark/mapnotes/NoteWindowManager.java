package uk.co.jpawlak.thelongdark.mapnotes;

import uk.co.jpawlak.thelongdark.mapnotes.serializable.Map;
import uk.co.jpawlak.thelongdark.mapnotes.serializable.MapSerialiser;
import uk.co.jpawlak.thelongdark.mapnotes.serializable.Marker;
import uk.co.jpawlak.thelongdark.mapnotes.serializable.SettingsSerialiser;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class NoteWindowManager {

    private final SettingsSerialiser settingsSerialiser; //TODO  load and save position/size of the note frame
    private final MapSerialiser mapSerialiser;

    public NoteWindowManager(SettingsSerialiser settingsSerialiser, MapSerialiser mapSerialiser) {
        this.settingsSerialiser = settingsSerialiser;
        this.mapSerialiser = mapSerialiser;
    }

    //TODO create relative to the frame (if no settings) - calculations needed
    public void openNote(Marker marker, Map map) {
        JTextArea textArea = new JTextArea(marker.getNote(), 40, 60); //TODO this should be more flexible
        JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "Note", JOptionPane.PLAIN_MESSAGE); //TODO should be relative to frame
        String newText = textArea.getText();
        marker.setNote(newText.trim().isEmpty() ? null : newText);
        mapSerialiser.save(map);
    }



    //TODO ensure singleton


    //TODO window closing or resizing -> write to settings

    //TODO save note: ctrl+S, focus lost, window closing, every 5s? or 3s after stopped typing?
    //TODO how to save note? callback? NoteWindowManager should't have access to Map object
    //TODO file modified indicator (check whether pasting needs special handling)

}
