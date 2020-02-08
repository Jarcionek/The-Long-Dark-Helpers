package uk.co.jpawlak.thelongdark.mapnotes.serializable;

public class Settings {

    private final String version;

    private WindowSettings mapWindowSettings;
    private WindowSettings noteWindowSettings;
    private WindowSettings noteSearchWindowSettings;

    public Settings(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public WindowSettings getMapWindowSettings() {
        return mapWindowSettings;
    }

    public void setMapWindowSettings(WindowSettings mapWindowSettings) {
        this.mapWindowSettings = mapWindowSettings;
    }

    public WindowSettings getNoteWindowSettings() {
        return noteWindowSettings;
    }

    public void setNoteWindowSettings(WindowSettings noteWindowSettings) {
        this.noteWindowSettings = noteWindowSettings;
    }

    public WindowSettings getNoteSearchWindowSettings() {
        return noteSearchWindowSettings;
    }

    public void setNoteSearchWindowSettings(WindowSettings noteSearchWindowSettings) {
        this.noteSearchWindowSettings = noteSearchWindowSettings;
    }

}
