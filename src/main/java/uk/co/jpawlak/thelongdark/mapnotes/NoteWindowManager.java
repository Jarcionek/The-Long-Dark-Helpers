package uk.co.jpawlak.thelongdark.mapnotes;

import uk.co.jpawlak.thelongdark.mapnotes.serializable.SettingsSerialiser;

public class NoteWindowManager {

    private final SettingsSerialiser settingsSerialiser; //TODO  load and save position/size of the frame

    public NoteWindowManager(SettingsSerialiser settingsSerialiser) {
        this.settingsSerialiser = settingsSerialiser;
    }

    //TODO create relative to the frame (if no settings) - calculations needed

    //TODO ensure singleton


    //TODO window closing or resizing -> write to settings

    //TODO save note: ctrl+S, focus lost, window closing, every 5s? or 3s after stopped typing?
    //TODO how to save note? callback? NoteWindowManager should't have access to Map object
    //TODO file modified indicator (check whether pasting needs special handling)

}
