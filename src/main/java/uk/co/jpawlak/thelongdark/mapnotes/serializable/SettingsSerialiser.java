package uk.co.jpawlak.thelongdark.mapnotes.serializable;

import uk.co.jpawlak.thelongdark.mapnotes.Main;

import java.io.File;

public class SettingsSerialiser {

    private static final File SETTINGS_FILE = new File(Main.MAIN_FOLDER, "settings.json");

    public void save(Settings settings) {
        Serialiser.save(settings, Main.MAIN_FOLDER, "settings");
    }

    public boolean settingsExist() {
        return SETTINGS_FILE.exists();
    }

    public Settings load() {
        return Serialiser.load(SETTINGS_FILE, Settings.class);
    }

}
